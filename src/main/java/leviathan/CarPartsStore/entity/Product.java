package leviathan.CarPartsStore.entity;

import jakarta.persistence.*;
import leviathan.CarPartsStore.domain.RemovalStatus;
import leviathan.CarPartsStore.services.ListToStringConverter;
import leviathan.CarPartsStore.services.MapConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    @Convert(converter = ListToStringConverter.class)
    private List<String> productPictures;
    private int priceInPennies;
    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;
    @Column(name = "removal_status")
    private RemovalStatus removalStatus;
    @OneToMany(mappedBy = "product")
    private List<Review> reviews;
    private int productRating;
    @ManyToMany(mappedBy = "discountProducts")
    private List<Discount> discounts;
    @OneToMany(mappedBy = "product")
    private List<ProductAttribute> productAttributes;

    public Product(Catalog catalog) {
        this.catalog = catalog;
        removalStatus = RemovalStatus.ACTIVE;
        productRating = 0;
    }

}
