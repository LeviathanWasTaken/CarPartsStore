package leviathan.CarPartsStore.repos;

import java.util.UUID;
import leviathan.CarPartsStore.domain.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepo extends CrudRepository<Cart, UUID> {

}
