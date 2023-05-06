package leviathan.CarPartsStore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private UUID productUUID;
    private String productName;
    private int productPriceInPennies;
    private String productPicture;
    private RemovalStatus productRemovalStatus;
    private Map<String, Object> productDetails;
}
