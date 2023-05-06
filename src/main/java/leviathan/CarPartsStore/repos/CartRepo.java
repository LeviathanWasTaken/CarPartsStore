package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.entity.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CartRepo extends CrudRepository<Cart, UUID> {

}
