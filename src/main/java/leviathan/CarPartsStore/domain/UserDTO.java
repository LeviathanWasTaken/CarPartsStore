package leviathan.CarPartsStore.domain;

import leviathan.CarPartsStore.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID userUUID;
    private String name;
    private String login;
    private String avatar;
    private String email;
    private String deliveryAddress;
    private Set<Roles> roles;
    private int totalPriceOfItemsInCartInPennies;
    private int totalQuantityOfItemsInCart;
}
