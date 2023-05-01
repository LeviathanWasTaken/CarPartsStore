package leviathan.CarPartsStore.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Data;

@Entity
@Table(name = "reviews")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reviewUUID;
    @ManyToOne
    private User author;
    @ManyToOne
    private Product product;
    private int mark;
    private int relevance;
    private String body;

    public Review(User author, int mark, String body, Product product) {
        this.author = author;
        this.mark = mark;
        this.body = body;
        this.product = product;
        relevance = 0;
    }

    public Review() {
    }
}
