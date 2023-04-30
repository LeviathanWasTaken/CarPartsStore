package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.domain.Catalog;
import leviathan.CarPartsStore.domain.Product;
import leviathan.CarPartsStore.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepo extends CrudRepository<Product, UUID> {
    Optional<Product> findByUniqueTag(String uniqueTag);

    Page<Product> findProductsByCatalogUUIDAndStatus(UUID catalogUUID, Status status, Pageable pageable);
}
