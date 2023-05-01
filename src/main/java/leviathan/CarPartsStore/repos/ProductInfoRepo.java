package leviathan.CarPartsStore.repos;

import java.util.UUID;
import leviathan.CarPartsStore.domain.ProductInfo;
import org.springframework.data.repository.CrudRepository;

public interface ProductInfoRepo extends CrudRepository<ProductInfo, UUID> {

}
