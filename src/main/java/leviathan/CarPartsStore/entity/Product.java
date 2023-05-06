package leviathan.CarPartsStore.entity;

import jakarta.persistence.*;
import leviathan.CarPartsStore.domain.RemovalStatus;
import leviathan.CarPartsStore.services.MapConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productUUID;
    @ManyToOne
    private Catalog catalog;
    @Convert(converter = MapConverter.class)
    private Map<String, Object> details;
    private String productName;
    private String imgSource;
    private int priceInPennies;
    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;
    @Column(name = "status")
    private RemovalStatus removalStatus;
    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

    public Product(Catalog catalog) {
        this.catalog = catalog;
        removalStatus = RemovalStatus.ACTIVE;
    }

}
