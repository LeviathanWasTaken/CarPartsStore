package leviathan.CarPartsStore.repos;

import java.util.UUID;
import leviathan.CarPartsStore.entity.CartItem;
import leviathan.CarPartsStore.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface CartItemRepo extends CrudRepository<CartItem, UUID> {

    Iterable<CartItem> findAllByProduct(Product product);
}
