package leviathan.CarPartsStore.repos;

import java.util.UUID;
import leviathan.CarPartsStore.domain.CartItem;
import leviathan.CarPartsStore.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface CartItemRepo extends CrudRepository<CartItem, UUID> {

    Iterable<CartItem> findAllByProduct(Product product);
}
