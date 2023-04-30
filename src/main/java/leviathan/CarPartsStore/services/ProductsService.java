package leviathan.CarPartsStore.services;

import leviathan.CarPartsStore.domain.*;
import leviathan.CarPartsStore.model.Status;
import leviathan.CarPartsStore.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductsService {
    private final CartService cartService;
    private final ProductRepo productRepo;
    private final CatalogRepo catalogRepo;
    private final ProductInfoRepo productInfoRepo;
    private final ReviewRepo reviewRepo;

    public ProductsService(CartService cartService, ProductRepo productRepo, CatalogRepo catalogRepo, ProductInfoRepo productInfoRepo, ReviewRepo reviewRepo) {
        this.cartService = cartService;
        this.productRepo = productRepo;
        this.catalogRepo = catalogRepo;
        this.productInfoRepo = productInfoRepo;
        this.reviewRepo = reviewRepo;
    }
/*
    public void addProduct(String name, String imgPath, int price, UUID catalogUUID) {
        Optional<Catalog> catalog = catalogRepo.findByName(catalogName);
        if (catalog.isPresent()) {
            Product product = new Product(catalog.get());
            ProductInfo productInfo = new ProductInfo(product, name, imgPath, price);
            productRepo.save(product);
            productInfoRepo.save(productInfo);
        }
        else throw new IllegalArgumentException("Invalid catalog name: " + catalogName);
    }
*/
    public Iterable<Product> getAllActiveProductsByCatalogUUID(UUID catalogUUID) {
        return catalogRepo.findById(catalogUUID).map(Catalog::getProducts).orElseThrow(
                ()-> new IllegalArgumentException("Incorrect catalog UUID: " + catalogUUID)
        ).stream().filter(
                product -> product.getStatus().equals(Status.ACTIVE)
        ).collect(Collectors.toList());
    }

    public Iterable<Product> getAllProductsByCatalogUUID(UUID catalogUUID) {
        return catalogRepo.findById(catalogUUID).<Iterable<Product>>map(Catalog::getProducts).orElse(null);
    }

    public Iterable<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public void setNewStatusToAllProducts(Iterable<Product> products, Status status) {
        for (Product product : products) {
            if (!product.getStatus().equals(Status.REMOVED)) {
                product.setStatus(status);
                productRepo.save(product);
            }
        }
    }








    /*
    public void addProduct(String name, String imgPath, int price, String catalogName) {
        Optional<Catalog> catalog = catalogRepo.findByName(catalogName);
        if (catalog.isPresent()) {
            Product product = new Product(catalog.get());
            ProductInfo productInfo = new ProductInfo(product, name, imgPath, price);
            productRepo.save(product);
            productInfoRepo.save(productInfo);
        }
        else throw new IllegalArgumentException("Invalid catalog name: " + catalogName);
    }

    public Product getProductFromBD(UUID productUUID) {
        Optional<Product> product = productRepo.findById(productUUID);
        return product.orElse(null);
    }

    public Iterable<Product> getAllProductsByCatalogUUID(UUID catalogUUID) {
        return catalogRepo.findById(catalogUUID).<Iterable<Product>>map(Catalog::getProducts).orElse(null);
    }

    public Iterable<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public void modifyProduct(UUID productUUID,
                              String catalogName,
                              String productName,
                              String imgSource,
                              int priceInPennies,
                              Status status) {
        Optional<Product> productOptional = productRepo.findById(productUUID);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setCatalog(catalogRepo.findByName(catalogName).orElseThrow(() -> new IllegalArgumentException("Invalid catalog name: " + catalogName)));
            ProductInfo productInfo = product.getProductInfo();
            productInfo.setProductName(productName);
            productInfo.setImgSource(imgSource);
            productInfo.setPriceInPennies(priceInPennies);
            product.setProductInfo(productInfo);
            product.setStatus(status);
            cartService.recalculateTotalPricesForProduct(product);
            productInfoRepo.save(productInfo);
            productRepo.save(product);
        }
        else throw new IllegalArgumentException("Invalid product UUID: " + productUUID);
    }

    public void setNewStatusToAllProducts(Iterable<Product> products, Status status) {
        for (Product product : products) {
            if (!product.getStatus().equals(Status.REMOVED)) {
                product.setStatus(status);
                productRepo.save(product);
            }
        }
    }

    public Iterable<Product> getAllActiveProductsByCatalogUUID(UUID catalogUUID) {
        return catalogRepo.findById(catalogUUID).map(Catalog::getProducts).orElseThrow(
                ()-> new IllegalArgumentException("Incorrect catalog UUID: " + catalogUUID)
        ).stream().filter(
                product -> product.getStatus().equals(Status.ACTIVE)
        ).collect(Collectors.toList());
    }
/*
    public List<Specification> getSpecificationsByProduct(Product product) {
        return product.getProductInfo().getSpecifications();
    }

    public void addSpecification(Product product, String specificationCategory, String name, String value) {
        List<Specification> specifications = product.getProductInfo().getSpecifications();
        for (Specification specification : specifications) {
            if (specification.getSpecificationCategoryName().equals(specificationCategory)) {
                Map<String, Object> data = specification.getData();
                Map<String, String> val = new HashMap<>();
                val.put(name, value);
                data.put(name, val);
                specificationRepo.save(specification);
                return;
            }
        }
        HashMap<String, Object> data = new HashMap<>();
        Map<String, String> val = new HashMap<>();
        val.put(name, value);
        data.put(name, val);
        Specification specification = new Specification(product.getProductInfo(), specificationCategory, data);
        specifications.add(specification);
        specificationRepo.save(specification);
    }

 */
}
