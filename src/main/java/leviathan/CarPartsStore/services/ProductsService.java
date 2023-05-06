package leviathan.CarPartsStore.services;

import leviathan.CarPartsStore.domain.ProductDTO;
import leviathan.CarPartsStore.domain.RemovalStatus;
import leviathan.CarPartsStore.entity.Product;
import leviathan.CarPartsStore.repos.ProductRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductsService {

    private final ProductRepo productRepo;

    public ProductsService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }
    /*
    public Product getProduct(String uniqueTag) {
        return productRepo.findByUniqueTag(uniqueTag).orElse(null);
    }

    public Product getProduct(UUID productUUID) {
        return productRepo.findById(productUUID).orElse(null);
    }



    public Page<Product> getActiveProductsByCatalogUUIDPaginated(UUID catalogUUID, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return productRepo.findProductsByCatalogUUIDAndRemovalStatus(catalogUUID, RemovalStatus.ACTIVE, pageable);
    }

    @Transactional
    public void addProduct(Catalog catalog,
                           String uniqueTag,
                           String productName,
                           String imgSource,
                           int priceInPennies) {
        Product product = new Product(catalog, uniqueTag);
        ProductInfo productInfo = new ProductInfo(product, productName, imgSource, priceInPennies);
        productRepo.save(product);
        productInfoRepo.save(productInfo);
    }

    @Transactional
    public void removeProduct(Product product) {
        product.setRemovalStatus(RemovalStatus.REMOVED);
        productRepo.save(product);
    }

    @Transactional
    public void modifyProduct(Product product,
                              Catalog catalog,
                              String productName,
                              String imgSource,
                              int priceInPennies) {
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

     */
    private ProductDTO productToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductUUID(product.getProductUUID());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductPicture(product.getImgSource());
        productDTO.setProductPriceInPennies(product.getPriceInPennies());
        productDTO.setProductDetails(product.getDetails());
        productDTO.setProductRemovalStatus(product.getRemovalStatus());
        return productDTO;
    }

    public List<ProductDTO> getAllProducts() {
        Iterable<Product> productsFromDB = productRepo.findAll();
        List<ProductDTO> products = new ArrayList<>();
        for (Product product : productsFromDB) {
            products.add(productToProductDTO(product));
        }
        return products;
    }

    public ProductDTO getProductByUUID(UUID productUUID) throws IllegalArgumentException {
        return productToProductDTO(productRepo.findById(productUUID).orElseThrow(
                () -> new IllegalArgumentException("There is no product with UUID: " + productUUID)
        ));
    }

    public List<ProductDTO> getPageOfActiveProductsByCatalogUUID(UUID catalogUUID, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 2);
        Page<Product> productsFromDB = productRepo.findProductsByCatalogUUIDAndRemovalStatus(catalogUUID, RemovalStatus.ACTIVE, pageable);
        List<ProductDTO> products = new ArrayList<>();
        for (Product product : productsFromDB) {
            products.add(productToProductDTO(product));
        }
        return products;
    }
}
