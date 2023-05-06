package leviathan.CarPartsStore.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

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
