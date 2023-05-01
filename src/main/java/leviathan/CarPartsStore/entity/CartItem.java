package leviathan.CarPartsStore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Data;

@Entity
@Table(name = "cartItems")
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartItemUUID;
    @ManyToOne
    private Cart cart;
    private int quantity;
    @ManyToOne
    private Product product;

    public CartItem() {
    }

    public CartItem(Cart cart, int quantity, Product product) {
        this.cart = cart;
        this.quantity = quantity;
        this.product = product;
    }

}
