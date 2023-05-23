package leviathan.CarPartsStore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "product_attributes")
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID attributeUUID;
    private String attributeName;
    private String attributePicture;
    private String attributeValue;
    @ManyToOne
    private Product product;
}
