package leviathan.CarPartsStore.services;

import jakarta.transaction.Transactional;
import leviathan.CarPartsStore.domain.*;
import leviathan.CarPartsStore.model.Status;
import leviathan.CarPartsStore.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductsService {
    private final ProductRepo productRepo;
    private final ProductInfoRepo productInfoRepo;

    public ProductsService(ProductRepo productRepo, ProductInfoRepo productInfoRepo) {
        this.productRepo = productRepo;
        this.productInfoRepo = productInfoRepo;
    }

    public Product getProduct(String uniqueTag) {
        return productRepo.findByUniqueTag(uniqueTag).orElse(null);
    }

    public Product getProduct(UUID productUUID) {
        return productRepo.findById(productUUID).orElse(null);
    }

    public Iterable<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Page<Product> getActiveProductsByCatalogUUIDPaginated(UUID catalogUUID, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return productRepo.findProductsByCatalogUUIDAndStatus(catalogUUID, Status.ACTIVE, pageable);
    }
    @Transactional
    public void addProduct(Catalog catalog, String uniqueTag, String productName, String imgSource, int priceInPennies) {
        Product product = new Product(catalog, uniqueTag);
        ProductInfo productInfo = new ProductInfo(product, productName, imgSource, priceInPennies);
        productRepo.save(product);
        productInfoRepo.save(productInfo);
    }
    @Transactional
    public void removeProduct(Product product) {
        product.setStatus(Status.PRODUCT_REMOVED);
        productRepo.save(product);
    }

    @Transactional
    public void modifyProduct(Product product, Catalog catalog, String productName, String imgSource, int priceInPennies) {
        product.setCatalog(catalog);
        ProductInfo productInfo = product.getProductInfo();
        productInfo.setProductName(productName);
        productInfo.setImgSource(imgSource);
        productInfo.setPriceInPennies(priceInPennies);
        productRepo.save(product);
        productInfoRepo.save(productInfo);
    }

    @Transactional
    public void editProductDetails(ProductInfo productInfo, String key, Object value) {
        Map<String, Object> details = productInfo.getDetails();
        details.put(key, value);
        productInfo.setDetails(details);
        productInfoRepo.save(productInfo);
    }

    @Transactional
    public void removeProductDetails(ProductInfo productInfo, String key) {
        productInfo.getDetails().remove(key);
        productInfoRepo.save(productInfo);
    }
}
