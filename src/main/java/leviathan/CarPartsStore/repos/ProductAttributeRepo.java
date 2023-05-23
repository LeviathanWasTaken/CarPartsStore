package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.entity.ProductAttribute;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface ProductAttributeRepo extends CrudRepository<ProductAttribute, UUID> {
}
