package leviathan.CarPartsStore.domain;

import leviathan.CarPartsStore.entity.ProductAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private UUID productUUID;
    private UUID productCatalogUUID;
    private String catalogName;
    private String productName;
    private int productPriceInPennies;
    private int productDiscountInPercents;
    private String previewPicture;
    private List<String> productPictures;
    private String productDescription;
    private int productRating;
    private RemovalStatus productRemovalStatus;
    private List<ProductStatus> productStatuses;
    private boolean isProductOnSale;
    private boolean isProductInCart;
    private Map<String, Object> productDetails;
    private List<ProductAttributeDTO> productAttributes = new ArrayList<>();
}
