package leviathan.CarPartsStore.services;

import leviathan.CarPartsStore.entity.Product;
import leviathan.CarPartsStore.entity.Review;
import leviathan.CarPartsStore.entity.User;
import leviathan.CarPartsStore.repos.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepo reviewRepo;

    public Review getReviewByUUID(UUID reviewUUID) {
        return reviewRepo.findById(reviewUUID)
              .orElseThrow(() -> new IllegalArgumentException("Incorrect review uuid" + reviewUUID));
    }

    public void postAReview(Product product, User author, String body, int mark) {
        Review newReview = new Review(author, mark, body, product);
        reviewRepo.save(newReview);
    }


    public List<Review> getAllReviewsByProduct(Product product) {
        return product.getReviews();
    }
}
