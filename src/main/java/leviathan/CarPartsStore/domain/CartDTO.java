package leviathan.CarPartsStore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private UUID cartUUID;
    private int totalPriceInPennies;
    private int totalQuantityOfItemsInCart;
}
