package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.entity.Product;
import leviathan.CarPartsStore.entity.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepo extends CrudRepository<Review, UUID> {

    List<Review> findAllByProduct(Product product);
}
