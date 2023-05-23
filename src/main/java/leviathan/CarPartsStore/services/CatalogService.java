package leviathan.CarPartsStore.services;

import jakarta.transaction.Transactional;
import leviathan.CarPartsStore.domain.CatalogDTO;
import leviathan.CarPartsStore.domain.RemovalStatus;
import leviathan.CarPartsStore.entity.Catalog;
import leviathan.CarPartsStore.entity.Product;
import leviathan.CarPartsStore.repos.CatalogRepo;
import leviathan.CarPartsStore.repos.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CatalogService {
    private final String rootCatalogUUID = "2ba366e2-3f87-4f52-8931-76918748557d";
    private final CatalogRepo catalogRepo;
    private final ProductRepo productRepo;


    public CatalogService(CatalogRepo catalogRepo, ProductRepo productRepo) {
        this.catalogRepo = catalogRepo;
        this.productRepo = productRepo;
    }

    /**
     *
     * @param parentUUID name of the parent
     * @return returns children that have direct connection to the given parent
     */
    public List<CatalogDTO> getActiveChildCatalogs(UUID parentUUID) throws IllegalArgumentException {
        Catalog parent = catalogRepo.findById(parentUUID).orElseThrow(
                () -> new IllegalArgumentException("There is no catalog with UUID: " + parentUUID)
        );
        List<Catalog> children = catalogRepo.findAllByParentAndRemovalStatus(parent, RemovalStatus.ACTIVE);
        List<CatalogDTO> catalogs = new ArrayList<>();
        for (Catalog catalog : children) {
            catalogs.add(convertCatalogToCatalogDTO(catalog));
        }
        return catalogs;
    }

    /**
     *
     * @return returns all catalogs from database
     */
    public List<CatalogDTO> getAllCatalogs() {
        List<Catalog> catalogsFromDB = (List<Catalog>) catalogRepo.findAll();
        List<CatalogDTO> catalogs = new ArrayList<>();
        for (Catalog catalog : catalogsFromDB) {
            catalogs.add(convertCatalogToAdminCatalogDTO(catalog));
        }
        return catalogs;
    }

    public CatalogDTO getCatalogByName(String catalogName) throws IllegalArgumentException {
        Catalog catalog = catalogRepo.findByCatalogName(catalogName).orElseThrow(
                () -> new IllegalArgumentException("There is no catalog with UUID: " + catalogName)
        );
        return convertCatalogToCatalogDTO(catalog);
    }

    /**
     * Finds top 5 active catalogs by popularity
     * @return List<CatalogDTO>
     */
    public List<CatalogDTO> getTop4ActiveByPopularity() {
        List<Catalog> catalogsFromDB = catalogRepo.findFirst4ByRemovalStatusAndCatalogUUIDIsNotOrderByPopularityDesc(
                RemovalStatus.ACTIVE,
                UUID.fromString(rootCatalogUUID));
        List<CatalogDTO> catalogs = new ArrayList<>();
        for (Catalog catalog : catalogsFromDB) {
            catalogs.add(convertCatalogToCatalogDTO(catalog));
        }
        return catalogs;
    }

    public CatalogDTO getCatalogByUUID(UUID catalogUUID) throws IllegalArgumentException {
        Catalog catalog = catalogRepo.findById(catalogUUID).orElseThrow(
                () -> new IllegalArgumentException("There is no catalog with UUID: " + catalogUUID)
        );
        return new CatalogDTO(catalog.getCatalogUUID(), catalog.getCatalogName().equals("ROOT")? "Catalog" : catalog.getCatalogName());
    }

    public List<CatalogDTO> getCatalogPath(UUID catalogUUID) throws IllegalArgumentException {
        List<CatalogDTO> result = new LinkedList<>();
        Catalog catalogFromDB = catalogRepo.findById(catalogUUID).orElseThrow(
                () -> new IllegalArgumentException("There is no catalog with UUID: " + catalogUUID)
        );
        List<Catalog> ignoreList = catalogRepo.findAllByRightLessThan(catalogFromDB.getLeft());
        List<Catalog> catalogsFromDB = catalogRepo.findAllByLeftLessThan(catalogFromDB.getLeft());
        Comparator<Catalog> comparator = Comparator.comparing(Catalog::getLeft);
        catalogsFromDB.sort(comparator);
        for (Catalog catalog : catalogsFromDB) {
            if (!ignoreList.contains(catalog)) {
                result.add(new CatalogDTO(catalog.getCatalogUUID(), catalog.getCatalogName().equals("ROOT")? "Catalog" : catalog.getCatalogName()));
            }
        }
        return result;
    }

    /**
     * Maps CatalogDTO to Catalog and calls addChild method
     * @param newCatalogDTO new catalog
     */
    @Transactional
    public void addNewCatalog(CatalogDTO newCatalogDTO) throws IllegalArgumentException {
        if (catalogRepo.findByCatalogName(newCatalogDTO.getCatalogName()).isPresent()) {
            throw new IllegalArgumentException("You cannot create catalog with name " + newCatalogDTO.getCatalogName() + " . Catalog name must be unique.");
        }
        Catalog parent = catalogRepo.findByCatalogName(newCatalogDTO.getParentCatalogName()).orElseThrow(
                () -> new IllegalArgumentException("There is no catalog with name: " + newCatalogDTO.getParentCatalogName())
        );
        Catalog newCatalog = new Catalog(newCatalogDTO.getCatalogName(), newCatalogDTO.getCatalogPicture(), RemovalStatus.ACTIVE);
        catalogRepo.save(newCatalog);
        newCatalog = catalogRepo.findByCatalogName(newCatalog.getCatalogName()).orElseThrow();
        addChild(parent, newCatalog);
    }



    /**
     * Modifies the catalog in database
     * @param catalogDTO
     */
    public void modifyCatalog(CatalogDTO catalogDTO) throws IllegalArgumentException {
        if (catalogDTO.getCatalogUUID().equals(UUID.fromString(rootCatalogUUID))) {
            throw new IllegalArgumentException("You cannot edit ROOT catalog!");
        }
        Catalog catalog = catalogRepo.findByCatalogName(catalogDTO.getCatalogName()).orElseThrow(
                () -> new IllegalArgumentException("There is no catalog with name: " + catalogDTO.getCatalogName())
        );
        catalog.setCatalogName(catalogDTO.getCatalogName());
        catalog.setImgSource(catalogDTO.getCatalogPicture());
        catalog.setPopularity(catalogDTO.getPopularity());
        catalogRepo.save(catalog);
    }


    /**
     * Sets to the given catalog RemovalStatus.REMOVED. Then performs cascade removal of all its children and products.
     * @param catalogUUID
     */
    @Transactional
    public void removeCatalog(UUID catalogUUID) throws IllegalArgumentException {
        Catalog catalog = catalogRepo.findById(catalogUUID).orElseThrow(
                () -> new IllegalArgumentException("There is no catalog with UUID: " + catalogUUID)
        );
        catalog.setRemovalStatus(RemovalStatus.REMOVED);
        childrenCascadeRemoval(catalogRepo.findAllByLeftGreaterThanAndRightLessThan(catalog.getLeft(), catalog.getRight()));
        removeProducts(catalog);
        catalogRepo.save(catalog);
    }

    /**
     * Sets to children catalogs RemovalStatus.PARENT_REMOVED and saves in database. Ignores those catalogs that are already removed.
     * @param catalogs
     */
    private void childrenCascadeRemoval(List<Catalog> catalogs) {
        catalogs = catalogs.stream().filter(
                catalog -> !catalog.getRemovalStatus().equals(RemovalStatus.REMOVED)
        ).toList();
        for (Catalog catalog : catalogs) {
            catalog.setRemovalStatus(RemovalStatus.PARENT_REMOVED);
            removeProducts(catalog);
            catalogRepo.save(catalog);
        }
    }

    /**
     * Sets to all products for given catalog RemovalStatus.PARENT_REMOVED and saves in database. Ignores those that are already removed.
     * @param catalog
     */
    private void removeProducts(Catalog catalog) {
        List<Product> products = catalog.getProducts();
        products = products.stream().filter(
                product -> !product.getRemovalStatus().equals(RemovalStatus.REMOVED)
        ).toList();
        for (Product product : products) {
            product.setRemovalStatus(RemovalStatus.PARENT_REMOVED);
            productRepo.save(product);
        }
    }

    /**
     *
     */
   @Transactional
   public void restoreCatalog(UUID catalogUUID) throws IllegalArgumentException {
       Catalog catalog = catalogRepo.findById(catalogUUID).orElseThrow(
               () -> new IllegalArgumentException("There is no catalog with UUID: " + catalogUUID)
       );
       boolean isRestorable = true;
       if(catalog.getParent() != null) {
           isRestorable = catalog.getParent().getRemovalStatus().equals(RemovalStatus.ACTIVE);
       }
       if (!isRestorable) throw new IllegalArgumentException("Unable to restore this catalog. You need first restore it's parent");
       catalog.setRemovalStatus(RemovalStatus.ACTIVE);
       childrenCascadeRestore(catalogRepo.findAllByLeftGreaterThanAndRightLessThan(catalog.getLeft(), catalog.getRight()));
       restoreProducts(catalog);
       catalogRepo.save(catalog);
   }


   private void childrenCascadeRestore(List<Catalog> children) {
       List<Catalog> removedCatalogs = children.stream().filter(
               catalog -> catalog.getRemovalStatus().equals(RemovalStatus.REMOVED)
       ).toList();
       List<Catalog> unrestorable = new ArrayList<>();
       for (Catalog catalog : removedCatalogs) {
           unrestorable.addAll(catalogRepo.findAllByLeftGreaterThanAndRightLessThan(catalog.getLeft(), catalog.getRight()));
       }
       for (Catalog catalog : children) {
           if (catalog.getRemovalStatus().equals(RemovalStatus.PARENT_REMOVED) && !unrestorable.contains(catalog)) {
               catalog.setRemovalStatus(RemovalStatus.ACTIVE);
               restoreProducts(catalog);
               catalogRepo.save(catalog);
           }
       }
   }

   private void restoreProducts(Catalog catalog) {
       List<Product> products = catalog.getProducts();
       products = products.stream().filter(
               product -> !product.getRemovalStatus().equals(RemovalStatus.REMOVED)
       ).toList();
       for (Product product : products) {
           product.setRemovalStatus(RemovalStatus.ACTIVE);
           productRepo.save(product);
       }
   }
    /**
     * Maps list of Catalog to list of CatalogDTO
     * @param catalog list of Catalog
     * @return List<CatalogDTO>
     */
    public CatalogDTO convertCatalogToCatalogDTO(Catalog catalog) {
        return new CatalogDTO(
                catalog.getCatalogUUID(),
                catalog.getCatalogName(),
                catalog.getImgSource(),
                !catalog.getChildren().isEmpty()
        );
    }

    /**
     * Maps list of Catalog to list of AdminCatalogDTO
     * @param catalog list of Catalog
     * @return List<AdminCatalogDTO>
     */
    public CatalogDTO convertCatalogToAdminCatalogDTO(Catalog catalog) {
        return new CatalogDTO(
                catalog.getCatalogUUID(),
                catalog.getCatalogName(),
                catalog.getImgSource(),
                catalog.getPopularity(),
                catalog.getParent() == null ? "" : catalog.getParent().getCatalogName(),
                catalog.getRemovalStatus(),
                catalog.getRemovalStatus().equals(RemovalStatus.REMOVED)
        );
    }

    /**
     * Adds child to the parent catalog, expands borders of all parents and saves it to database
     * @param parent parent catalog
     * @param child child catalog
     */
    @Transactional
    public void addChild(Catalog parent, Catalog child) {
        parent.getChildren().add(child);
        child.setParent(parent);
        int parentRight = parent.getRight();
        List<Catalog> catalogs = catalogRepo.findAllByRightGreaterThanEqual(parentRight);
        for (Catalog catalog : catalogs) {
            if (catalog.getLeft() >= parentRight) {
                catalog.setLeft(catalog.getLeft()+2);
            }
            catalog.setRight(catalog.getRight()+2);
            catalogRepo.save(catalog);
        }
        child.setLeft(parentRight);
        child.setRight(parentRight+1);
        catalogRepo.save(child);
    }
}
