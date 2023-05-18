package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.entity.Discount;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface DiscountRepo extends CrudRepository<Discount, UUID> {
}
