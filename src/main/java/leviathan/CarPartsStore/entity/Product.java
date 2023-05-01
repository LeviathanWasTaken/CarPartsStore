package leviathan.CarPartsStore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import leviathan.CarPartsStore.model.Status;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productUUID;
    @ManyToOne
    private Catalog catalog;
    @Column(unique = true)
    private String uniqueTag;
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

    public Product(Catalog catalog, String uniqueTag) {
        this.catalog = catalog;
        status = Status.ACTIVE;
    }

}
