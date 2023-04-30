package leviathan.CarPartsStore.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "cartItems")
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

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public UUID getCartItemUUID() {
        return cartItemUUID;
    }

    public void setCartItemUUID(UUID cartItemUUID) {
        this.cartItemUUID = cartItemUUID;
    }
}
