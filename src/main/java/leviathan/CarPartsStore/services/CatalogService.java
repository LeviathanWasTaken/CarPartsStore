package leviathan.CarPartsStore.services;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import leviathan.CarPartsStore.domain.CatalogDTO;
import leviathan.CarPartsStore.domain.RemovalStatus;
import leviathan.CarPartsStore.entity.Catalog;
import leviathan.CarPartsStore.entity.Product;
import leviathan.CarPartsStore.repos.CatalogRepo;
import leviathan.CarPartsStore.repos.ProductRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public List<CatalogDTO> getActiveChildCatalogs(String parentName) {
        Catalog parent = catalogRepo.findByCatalogName(parentName).orElseThrow(
                () -> new IllegalArgumentException("There is no catalog with name: " + parentName)
        );
        List<Catalog> children = catalogRepo.findAllByParentAndRemovalStatus(parent, RemovalStatus.ACTIVE);
        return convertCatalogToCatalogDTO(children);
    }

    /**
     *
     * @return returns all catalogs from database
     */
    public List<CatalogDTO> getAllCatalogs() {
        List<Catalog> catalogsFromDB = (List<Catalog>) catalogRepo.findAll();
        return convertCatalogToAdminCatalogDTO(catalogsFromDB);
    }

    /**
     * Finds top 5 active catalogs by popularity
     * @return List<CatalogDTO>
     */
    public List<CatalogDTO> getTop5ActiveByPopularity() {
        List<Catalog> catalogs = catalogRepo.findFirst5ByRemovalStatusAndCatalogNameIsNotOrderByPopularityDesc(RemovalStatus.ACTIVE, "ROOT");
        return convertCatalogToCatalogDTO(catalogs);
    }

    /**
     * Maps CatalogDTO to Catalog and calls addChild method
     * @param newCatalogDTO new catalog
     */
    public void addNewCatalog(CatalogDTO newCatalogDTO) {
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
    /*
    public void modifyCatalog(CatalogDTO catalogDTO) {
        Catalog catalog = catalogRepo.findById(catalogDTO.getCatalogUUID()).orElseThrow(
                () -> new IllegalArgumentException("There is no catalog with UUID: " + catalogDTO.getCatalogUUID())
        );
        catalog.setCatalogName(catalogDTO.getCatalogName());
        catalog.setImgSource(catalogDTO.getCatalogPicture());
        catalog.setPopularity(catalogDTO.getPopularity());
        catalogRepo.save(catalog);
    }

     */


    /**
     * Sets to the given catalog RemovalStatus.REMOVED. Then performs cascade removal of all its children and products.
     * @param catalogUUID
     */
    @Transactional
    public void removeCatalog(UUID catalogUUID) {
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
   public void restoreCatalog(UUID catalogUUID) {
       Catalog catalog = catalogRepo.findById(catalogUUID).orElseThrow(
               () -> new IllegalArgumentException("There is no catalog with UUID: " + catalogUUID)
       );
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
     * @param catalogs list of Catalog
     * @return List<CatalogDTO>
     */
    public List<CatalogDTO> convertCatalogToCatalogDTO(List<Catalog> catalogs) {
        List<CatalogDTO> result = new ArrayList<>();
        for (Catalog catalog : catalogs) {
            result.add(new CatalogDTO(
                    catalog.getUUID(),
                    catalog.getCatalogName(),
                    catalog.getImgSource()
            ));
        }
        return result;
    }

    /**
     * Maps list of Catalog to list of AdminCatalogDTO
     * @param catalogs list of Catalog
     * @return List<AdminCatalogDTO>
     */
    public List<CatalogDTO> convertCatalogToAdminCatalogDTO(List<Catalog> catalogs) {
        List<CatalogDTO> result = new ArrayList<>();
        for (Catalog catalog : catalogs) {
            result.add(new CatalogDTO(
                    catalog.getUUID(),
                    catalog.getCatalogName(),
                    catalog.getImgSource(),
                    catalog.getPopularity(),
                    catalog.getParent() == null ? "" : catalog.getParent().getCatalogName(),
                    !catalog.getRemovalStatus().equals(RemovalStatus.ACTIVE),
                    catalog.getRemovalStatus()
            ));
        }
        return result;
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
        child.setLeft(parent.getRight());
        child.setRight(parent.getRight() + 1);
        catalogRepo.save(child);

        while (parent.getLeft() >= 1) {
            parent.setRight(child.getRight() + 1);
            catalogRepo.save(parent);
            if (parent.getLeft() == 1) {
                break;
            }
            if (parent.getLeft() > 1) {
                child = parent;
                parent = parent.getParent();
            }
        }
    }
}
