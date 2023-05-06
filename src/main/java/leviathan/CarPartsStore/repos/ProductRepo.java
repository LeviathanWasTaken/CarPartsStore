package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.domain.RemovalStatus;
import leviathan.CarPartsStore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepo extends CrudRepository<Product, UUID> {

    Page<Product> findProductsByCatalogUUIDAndRemovalStatus(UUID catalogUUID, RemovalStatus removalStatus, Pageable pageable);
}
