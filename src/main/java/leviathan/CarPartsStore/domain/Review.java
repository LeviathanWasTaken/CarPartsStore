package leviathan.CarPartsStore.domain;

import jakarta.persistence.*;
import leviathan.CarPartsStore.model.Status;

import java.util.UUID;

@Entity
@Table(name = "reviews")
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

    public UUID getReviewUUID() {
        return reviewUUID;
    }
    public void setReviewUUID(UUID reviewUUID) {
        this.reviewUUID = reviewUUID;
    }
    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }
    public int getMark() {
        return mark;
    }
    public void setMark(int mark) {
        this.mark = mark;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public int getRelevance() {
        return relevance;
    }
    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

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
