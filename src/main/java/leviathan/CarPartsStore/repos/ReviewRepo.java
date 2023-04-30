package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.domain.Product;
import leviathan.CarPartsStore.domain.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepo extends CrudRepository<Review, UUID> {
    public List<Review> findAllByProduct(Product product);
}
