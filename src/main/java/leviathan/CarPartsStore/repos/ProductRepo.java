package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.domain.RemovalStatus;
import leviathan.CarPartsStore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepo extends CrudRepository<Product, UUID> {

    List<Product> findAllByCatalogCatalogUUIDAndRemovalStatusOrderByPopularityDesc(UUID catalogUUID, RemovalStatus removalStatus);

    List<Product> findAllByCatalogCatalogUUIDAndRemovalStatusOrderByPopularityAsc(UUID catalogUUID, RemovalStatus removalStatus);

    List<Product> findAllByCatalogCatalogUUIDAndRemovalStatusOrderByPriceInPenniesDesc(UUID catalogUUID, RemovalStatus removalStatus);

    List<Product> findAllByCatalogCatalogUUIDAndRemovalStatusOrderByPriceInPenniesAsc(UUID catalogUUID, RemovalStatus removalStatus);


    //Page<Product> findProductsByCatalogCatalogUUIDAndRemovalStatus(UUID catalogUUID, RemovalStatus removalStatus, Pageable pageable);
}
