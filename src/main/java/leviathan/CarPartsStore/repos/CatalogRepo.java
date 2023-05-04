package leviathan.CarPartsStore.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import leviathan.CarPartsStore.domain.RemovalStatus;
import leviathan.CarPartsStore.entity.Catalog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CatalogRepo extends CrudRepository<Catalog, UUID> {

    Optional<Catalog> findByName(@Param("name") String name);

    List<Catalog> findFirst5ByRemovalStatusOrderByPopularityDesc(RemovalStatus removalStatus);

    List<Catalog> findAllByLeftGreaterThanAndRightLessThan(int left, int right);

    List<Catalog> findFirst5ByRemovalStatusAndLeftGreaterThanAndRightLessThanOrderByPopularityDesc(RemovalStatus removalStatus,
                                                                                            int left,
                                                                                            int right);
    List<Catalog> findAllByParentAndRemovalStatus(Catalog parent, RemovalStatus removalStatus);
}
