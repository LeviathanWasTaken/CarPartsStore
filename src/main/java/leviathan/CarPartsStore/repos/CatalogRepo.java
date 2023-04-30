package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.domain.Catalog;
import leviathan.CarPartsStore.model.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CatalogRepo extends CrudRepository<Catalog, UUID> {
    Optional<Catalog> findByUniqueTag(@Param("uniqueTag") String uniqueTag);
    List<Catalog> findFirst5ByStatusOrderByPopularityDesc(Status status);

    List<Catalog> findAllByLeftGreaterThanAndRightLessThan(int left, int right);

    List<Catalog> findFirst5ByStatusAndLeftGreaterThanAndRightLessThanOrderByPopularityDesc(Status status, int left, int right);
}
