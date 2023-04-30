package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.domain.CartItem;
import leviathan.CarPartsStore.domain.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CartItemRepo extends CrudRepository<CartItem, UUID> {
    Iterable<CartItem> findAllByProduct(Product product);
}
