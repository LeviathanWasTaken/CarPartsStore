package leviathan.CarPartsStore.domain;

import jakarta.persistence.*;
import leviathan.CarPartsStore.model.Status;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productUUID;
    @ManyToOne
    private Catalog catalog;
    @OneToOne(mappedBy = "product")
    private ProductInfo productInfo;
    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;
    @Column(name = "status")
    private Status status;
    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

    public Product() {
    }

    public Product(Catalog catalog) {
        this.catalog = catalog;
        status = Status.ACTIVE;
    }

    public Catalog getCatalog() {
        return catalog;
    }
    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
    public java.util.UUID getProductUUID() {
        return productUUID;
    }
    public void setProductUUID(java.util.UUID UUID) {
        this.productUUID = UUID;
    }
    public ProductInfo getProductInfo() {
        return productInfo;
    }
    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }
    public List<CartItem> getCartItems() {
        return cartItems;
    }
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public List<Review> getReviews() {
        return reviews;
    }
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
