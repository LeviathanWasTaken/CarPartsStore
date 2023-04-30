package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.domain.Product;
import leviathan.CarPartsStore.domain.ProductInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProductInfoRepo extends CrudRepository<ProductInfo, UUID> {
}
