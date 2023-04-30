package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.domain.Catalog;
import leviathan.CarPartsStore.domain.Product;
import leviathan.CarPartsStore.model.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepo extends CrudRepository<Product, UUID> {
}
