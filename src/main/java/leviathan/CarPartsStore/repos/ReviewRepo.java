package leviathan.CarPartsStore.repos;

import java.util.List;
import java.util.UUID;
import leviathan.CarPartsStore.domain.Product;
import leviathan.CarPartsStore.domain.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepo extends CrudRepository<Review, UUID> {

    List<Review> findAllByProduct(Product product);
}
