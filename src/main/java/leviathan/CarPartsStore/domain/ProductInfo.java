package leviathan.CarPartsStore.domain;

import jakarta.persistence.*;
import leviathan.CarPartsStore.services.MapConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
public class ProductInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productInfoUUID;
    @OneToOne
    private Product product;
    @Convert(converter = MapConverter.class)
    private Map<String, Object> details;
    private String productName;
    private String imgSource;
    private int priceInPennies;

    public ProductInfo() {
    }

    public ProductInfo(Product product, String productName, String imgSource, int priceInPennies) {
        this.product = product;
        this.productName = productName;
        this.imgSource = imgSource;
        this.priceInPennies = priceInPennies;
        details = new HashMap<>();
    }

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getImgSource() {
        return imgSource;
    }
    public void setImgSource(String imgSource) {
        this.imgSource = imgSource;
    }
    public int getPriceInPennies() {
        return priceInPennies;
    }
    public void setPriceInPennies(int priceInPennies) {
        this.priceInPennies = priceInPennies;
    }
    public UUID getProductInfoUUID() {
        return productInfoUUID;
    }
    public void setProductInfoUUID(UUID productInfoUUID) {
        this.productInfoUUID = productInfoUUID;
    }
    public Map<String, Object> getDetails() {
        return details;
    }
    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}
