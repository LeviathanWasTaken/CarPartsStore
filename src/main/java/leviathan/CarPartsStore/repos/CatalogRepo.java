package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.domain.RemovalStatus;
import leviathan.CarPartsStore.entity.Catalog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CatalogRepo extends CrudRepository<Catalog, UUID> {

    Optional<Catalog> findByCatalogName(String catalogName);

    List<Catalog> findFirst5ByRemovalStatusAndCatalogUUIDIsNotOrderByPopularityDesc(
            RemovalStatus removalStatus, UUID catalogUUID);

    List<Catalog> findAllByLeftGreaterThanAndRightLessThan(int left, int right);

    List<Catalog> findAllByParentAndRemovalStatus(Catalog parent, RemovalStatus removalStatus);

    List<Catalog> findAllByRightGreaterThanEqual(int right);

    List<Catalog> findAllByRightLessThan(int right);

    List<Catalog> findAllByLeftLessThan(int left);
}
