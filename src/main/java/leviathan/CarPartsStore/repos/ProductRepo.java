package leviathan.CarPartsStore.repos;

import java.util.Optional;
import java.util.UUID;
import leviathan.CarPartsStore.entity.Product;
import leviathan.CarPartsStore.domain.RemovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepo extends CrudRepository<Product, UUID> {

    Optional<Product> findByUniqueTag(String uniqueTag);

    Page<Product> findProductsByCatalogUUIDAndStatus(UUID catalogUUID, RemovalStatus removalStatus, Pageable pageable);
}
