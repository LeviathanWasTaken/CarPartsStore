package leviathan.CarPartsStore.services;

import jakarta.transaction.Transactional;
import leviathan.CarPartsStore.domain.Catalog;
import leviathan.CarPartsStore.domain.Product;
import leviathan.CarPartsStore.model.Status;
import leviathan.CarPartsStore.repos.CatalogRepo;
import leviathan.CarPartsStore.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class CatalogService {
    private final CatalogRepo catalogRepo;
    private final ProductRepo productRepo;


    public CatalogService(CatalogRepo catalogRepo, ProductRepo productRepo) {
        this.catalogRepo = catalogRepo;
        this.productRepo = productRepo;
    }

    public List<Catalog> getAllChildCatalogs(String uniqueTag) {
        return catalogRepo.findByUniqueTag(uniqueTag).orElseThrow(
                () -> new IllegalArgumentException("THERE IS NO " + uniqueTag)
        ).getChildren().stream().toList();
    }

    public List<Catalog> getAllActiveChildCatalogs(String uniqueTag) {
        return catalogRepo.findByUniqueTag(uniqueTag).orElseThrow(
                () -> new IllegalArgumentException("THERE IS NO " + uniqueTag)
        ).getChildren().stream().filter(catalog -> catalog.getStatus().equals(Status.ACTIVE)).toList();
    }

    public List<Catalog> getAllCatalogs() {
        List<Catalog> catalogs = new ArrayList<>();
        Catalog root = catalogRepo.findByUniqueTag("ROOT").orElseThrow(
                () -> new IllegalArgumentException("SHIT! SHIT! SHIT! THERE IS NO ROOT"));
        return catalogRepo.findAllByLeftBoundaryGreaterThanAndRightBoundaryLessThan(root.getLeft(), root.getRight());
    }

    public Catalog getCatalog(String uniqueTag) {
        return catalogRepo.findByUniqueTag(uniqueTag).orElse(null);
    }

    public List<Catalog> getTop5ActiveByPopularity() {
        return catalogRepo.findFirst5ByStatusOrderByPopularityDesc(Status.ACTIVE);
    }

    public void addNewCatalog(Catalog parent, String catalogName, String imgSource) {
        String uniqueTag = catalogName.strip().toUpperCase().replace(" ", "_");
        Catalog newCatalog = new Catalog(catalogName, imgSource, uniqueTag, Status.ACTIVE);
        addChild(parent, newCatalog);
    }
    public void addNewCatalog(Catalog parent, String catalogName, String imgSource, Status status) {
        String uniqueTag = catalogName.strip().toUpperCase().replace(" ", "_");
        Catalog newCatalog = new Catalog(catalogName, imgSource, uniqueTag, status);
        addChild(parent, newCatalog);
    }
    public void addNewCatalog(Catalog parent, String catalogName, String imgSource, String uniqueTag) {
        Catalog newCatalog = new Catalog(catalogName, imgSource, uniqueTag, Status.ACTIVE);
        addChild(parent, newCatalog);
    }
    public void addNewCatalog(Catalog parent, String catalogName, String imgSource, String uniqueTag, Status status) {
        Catalog newCatalog = new Catalog(catalogName, imgSource, uniqueTag, status);
        addChild(parent, newCatalog);
    }

    public void modifyCatalog(Catalog catalog, String catalogName, String imgSource, String uniqueTag, long popularity) {
        if (catalogRepo.findByUniqueTag(uniqueTag).isEmpty()) {
            catalog.setUniqueTag(uniqueTag);
        }
        catalog.setCatalogName(catalogName);
        catalog.setImgSource(imgSource);
        catalog.setPopularity(popularity);
        catalogRepo.save(catalog);
    }

    //cascade removal
    @Transactional
    public void removeCatalog(Catalog catalog) {
        catalog.setStatus(Status.CATALOG_REMOVED);
        catalogRepo.save(catalog);
        List<Catalog> allChildren = catalogRepo.findAllByLeftBoundaryGreaterThanAndRightBoundaryLessThan(
                catalog.getLeft(), catalog.getRight()
        ).stream().filter(
                catalogChild -> catalogChild.getStatus().equals(Status.ACTIVE)
        ).toList();
        for (Catalog child : allChildren) {
            child.setStatus(Status.PARENT_CATALOG_REMOVED);
            catalogRepo.save(child);
            List<Product> products = child.getProducts().stream().filter(
                    product -> product.getStatus().equals(Status.ACTIVE)
            ).toList();
            for (Product product : products) {
                product.setStatus(Status.CATALOG_REMOVED);
                productRepo.save(product);
            }
        }
    }

    //cascade restore
    @Transactional
    public void restoreCatalog(Catalog catalog) {
        catalog.setStatus(Status.ACTIVE);
        catalogRepo.save(catalog);
        List<Catalog> allChildren = catalogRepo.findAllByLeftBoundaryGreaterThanAndRightBoundaryLessThan(
                catalog.getLeft(), catalog.getRight()
        );
        List<Catalog> removedChildren = allChildren.stream().filter(
                child -> child.getStatus().equals(Status.CATALOG_REMOVED)
        ).toList();
        for (Catalog child : allChildren) {
            if (removedChildren.stream().noneMatch(
                    removedChild -> removedChild.getLeft() < child.getLeft() && removedChild.getRight() > child.getRight()
            )) {
                child.setStatus(Status.ACTIVE);
                catalogRepo.save(child);
                List<Product> products = child.getProducts().stream().filter(
                        product -> product.getStatus().equals(Status.CATALOG_REMOVED)
                ).toList();
                for (Product product : products) {
                    product.setStatus(Status.ACTIVE);
                    productRepo.save(product);
                }
            }
        }
    }
    
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
