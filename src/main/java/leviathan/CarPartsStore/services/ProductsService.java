package leviathan.CarPartsStore.services;

import jakarta.transaction.Transactional;
import leviathan.CarPartsStore.domain.*;
import leviathan.CarPartsStore.entity.*;
import leviathan.CarPartsStore.repos.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductsService {

    private final ProductRepo productRepo;
    private final CatalogService catalogService;
    private final CatalogRepo catalogRepo;
    private final UserRepo userRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductAttributeRepo productAttributeRepo;

    public ProductsService(ProductRepo productRepo, CatalogService catalogService, CatalogRepo catalogRepo, UserRepo userRepo, CartItemRepo cartItemRepo, ProductAttributeRepo productAttributeRepo) {
        this.productRepo = productRepo;
        this.catalogService = catalogService;
        this.catalogRepo = catalogRepo;
        this.userRepo = userRepo;
        this.cartItemRepo = cartItemRepo;
        this.productAttributeRepo = productAttributeRepo;
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
        productDTO.setProductPictures(product.getProductPictures());
        productDTO.setProductPriceInPennies(product.getPriceInPennies());
        productDTO.setProductDetails(product.getDetails());
        productDTO.setProductRemovalStatus(product.getRemovalStatus());
        productDTO.setProductCatalogUUID(product.getCatalog().getCatalogUUID());
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

    public List<ProductDTO> getAllActiveProductsByCatalogUUID(UUID catalogUUID, SortingType sortingType, UserDTO user) throws IllegalArgumentException {
        List<Product> productsFromDB;
        switch (sortingType) {
            case PRICE_ASC -> productsFromDB = productRepo.findAllByCatalogCatalogUUIDAndRemovalStatusOrderByPriceInPenniesAsc(catalogUUID, RemovalStatus.ACTIVE);
            case PRICE_DESC -> productsFromDB = productRepo.findAllByCatalogCatalogUUIDAndRemovalStatusOrderByPriceInPenniesDesc(catalogUUID, RemovalStatus.ACTIVE);
            case POPULARITY_ASC -> productsFromDB = productRepo.findAllByCatalogCatalogUUIDAndRemovalStatusOrderByProductRatingAsc(catalogUUID, RemovalStatus.ACTIVE);
            case POPULARITY_DESC -> productsFromDB = productRepo.findAllByCatalogCatalogUUIDAndRemovalStatusOrderByProductRatingDesc(catalogUUID, RemovalStatus.ACTIVE);
            default -> throw new IllegalArgumentException("SortingType error. Can't resolve: " + sortingType);
        }
        List<ProductDTO> products = new ArrayList<>();
        for (Product product : productsFromDB) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductUUID(product.getProductUUID());
            productDTO.setPreviewPicture(product.getProductPictures().get(0));
            productDTO.setProductName(product.getProductName());
            productDTO.setProductPriceInPennies(product.getPriceInPennies());
            productDTO.setProductRating(product.getProductRating());
            List<ProductAttributeDTO> productAttributes = new ArrayList<>();
            for (ProductAttribute productAttribute : product.getProductAttributes()) {
                ProductAttributeDTO productAttributeDTO = new ProductAttributeDTO();
                productAttributeDTO.setAttributeName(productAttribute.getAttributeName());
                productAttributeDTO.setAttributeValue(productAttribute.getAttributeValue());
                productAttributeDTO.setAttributePicture(productAttribute.getAttributePicture());
                productAttributes.add(productAttributeDTO);
            }
            productAttributes.sort(Comparator.comparing(ProductAttributeDTO::getAttributeName));
            productDTO.setProductAttributes(productAttributes);
            if (user != null) {
                User userFromBD = userRepo.findById(user.getUserUUID()).orElse(null);
                if (userFromBD != null) {
                    for (CartItem cartItem : userFromBD.getCart().getCartItems()) {
                        if (cartItem.getProduct().equals(product)) {
                            productDTO.setProductInCart(true);
                        }
                    }
                }
            }
            productDTO.setProductOnSale(!product.getDiscounts().isEmpty());
            if (productDTO.isProductOnSale()) productDTO.setProductDiscountInPercents(12);
            products.add(productDTO);
        }
        return products;
    }

    @Transactional
    public void createProduct(ProductDTO productDTO) throws IllegalArgumentException {
        Product product = new Product(catalogRepo.findByCatalogName(productDTO.getCatalogName()).orElseThrow(
                () -> new IllegalArgumentException("There is no catalog with name: " + productDTO.getCatalogName())
        ));
        product.setProductName(productDTO.getProductName());
        product.setProductPictures(List.of(productDTO.getPreviewPicture()));
        product.setPriceInPennies(productDTO.getProductPriceInPennies());
        HashMap<String, Object> details = new HashMap<>();
        details.put("test", "test");
        product.setDetails(details);
        productRepo.save(product);
        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setAttributeName("Fuel efficiency");
        productAttribute.setAttributeValue("A");
        productAttribute.setAttributePicture("/static/img/favicon.svg");
        productAttribute.setProduct(product);
        productAttributeRepo.save(productAttribute);
    }
}
