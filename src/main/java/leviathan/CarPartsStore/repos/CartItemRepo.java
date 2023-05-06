package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.entity.CartItem;
import leviathan.CarPartsStore.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CartItemRepo extends CrudRepository<CartItem, UUID> {

    Iterable<CartItem> findAllByProduct(Product product);
}
