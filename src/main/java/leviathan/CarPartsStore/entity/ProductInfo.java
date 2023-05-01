package leviathan.CarPartsStore.entity;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import leviathan.CarPartsStore.services.MapConverter;
import lombok.Data;

@Entity
@Data
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

}
