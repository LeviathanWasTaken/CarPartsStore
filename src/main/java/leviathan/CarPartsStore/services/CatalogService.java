package leviathan.CarPartsStore.services;

import jakarta.transaction.Transactional;
import leviathan.CarPartsStore.domain.CatalogDTO;
import leviathan.CarPartsStore.domain.RemovalStatus;
import leviathan.CarPartsStore.entity.Catalog;
import leviathan.CarPartsStore.entity.Product;
import leviathan.CarPartsStore.repos.CatalogRepo;
import leviathan.CarPartsStore.repos.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CatalogService {

    private final CatalogRepo catalogRepo;
    private final ProductRepo productRepo;


    public CatalogService(CatalogRepo catalogRepo, ProductRepo productRepo) {
        this.catalogRepo = catalogRepo;
        this.productRepo = productRepo;
    }

    /**
     *
     * @param parentName name of the parent
     * @return returns children that have direct connection to the given parent
     */
    public List<CatalogDTO> getActiveChildCatalogs(String parentName) throws IllegalArgumentException {
        Catalog parent = catalogRepo.findByCatalogName(parentName).orElseThrow(
                () -> new IllegalArgumentException("There is no catalog with name: " + parentName)
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
    public List<CatalogDTO> getTop5ActiveByPopularity() {
        List<Catalog> catalogsFromDB = catalogRepo.findFirst5ByRemovalStatusAndCatalogNameIsNotOrderByPopularityDesc(
                RemovalStatus.ACTIVE,
                "ROOT");
        List<CatalogDTO> catalogs = new ArrayList<>();
        for (Catalog catalog : catalogsFromDB) {
            catalogs.add(convertCatalogToCatalogDTO(catalog));
        }
        return catalogs;
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
        if (catalogDTO.getCatalogName().equals("ROOT")) {
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
                catalog.getUUID(),
                catalog.getCatalogName(),
                catalog.getImgSource()
        );
    }

    /**
     * Maps list of Catalog to list of AdminCatalogDTO
     * @param catalog list of Catalog
     * @return List<AdminCatalogDTO>
     */
    public CatalogDTO convertCatalogToAdminCatalogDTO(Catalog catalog) {
        return new CatalogDTO(
                catalog.getUUID(),
                catalog.getCatalogName(),
                catalog.getImgSource(),
                catalog.getPopularity(),
                catalog.getParent() == null ? "" : catalog.getParent().getCatalogName(),
                catalog.getRemovalStatus().equals(RemovalStatus.REMOVED),
                catalog.getRemovalStatus()
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
            catalog.setRight(catalog.getRight()+2);
            catalogRepo.save(catalog);
        }
        child.setLeft(parentRight);
        child.setRight(parentRight+1);
        catalogRepo.save(child);
    }
}
