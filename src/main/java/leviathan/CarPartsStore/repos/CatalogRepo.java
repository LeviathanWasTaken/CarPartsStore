package leviathan.CarPartsStore.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import leviathan.CarPartsStore.domain.RemovalStatus;
import leviathan.CarPartsStore.entity.Catalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CatalogRepo extends CrudRepository<Catalog, UUID> {

    Optional<Catalog> findByCatalogName(String catalogName);

    List<Catalog> findFirst5ByRemovalStatusAndCatalogNameIsNotOrderByPopularityDesc(
            RemovalStatus removalStatus, String rootCatalogName);

    List<Catalog> findAllByLeftGreaterThanAndRightLessThan(int left, int right);

    List<Catalog> findAllByParentAndRemovalStatus(Catalog parent, RemovalStatus removalStatus);
}
