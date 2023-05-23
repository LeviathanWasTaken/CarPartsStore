package leviathan.CarPartsStore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private UUID cartItemUUID;
    private UUID cartItemProductUUID;
    private int cartItemQuantity;
    private int cartItemTotalPriceInPennies;
    private String cartItemProductName;
    private String cartItemProductPreviewPicture;
    private int cartItemProductPriceInPennies;
}
