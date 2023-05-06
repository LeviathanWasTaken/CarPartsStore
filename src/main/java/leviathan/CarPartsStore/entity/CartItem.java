package leviathan.CarPartsStore.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

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
