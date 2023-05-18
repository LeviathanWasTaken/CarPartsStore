package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.domain.RemovalStatus;
import leviathan.CarPartsStore.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepo extends CrudRepository<Product, UUID> {

    List<Product> findAllByCatalogCatalogUUIDAndRemovalStatusOrderByProductRatingDesc(UUID catalogUUID, RemovalStatus removalStatus);

    List<Product> findAllByCatalogCatalogUUIDAndRemovalStatusOrderByProductRatingAsc(UUID catalogUUID, RemovalStatus removalStatus);

    List<Product> findAllByCatalogCatalogUUIDAndRemovalStatusOrderByPriceInPenniesDesc(UUID catalogUUID, RemovalStatus removalStatus);

    List<Product> findAllByCatalogCatalogUUIDAndRemovalStatusOrderByPriceInPenniesAsc(UUID catalogUUID, RemovalStatus removalStatus);


    //Page<Product> findProductsByCatalogCatalogUUIDAndRemovalStatus(UUID catalogUUID, RemovalStatus removalStatus, Pageable pageable);
}
