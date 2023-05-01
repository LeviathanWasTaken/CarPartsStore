package leviathan.CarPartsStore;

import leviathan.CarPartsStore.services.CatalogService;
import leviathan.CarPartsStore.services.ProductsService;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
class CarPartsStoreApplicationTests {

	@Autowired
	CatalogService catalogService;
	private static final String DOCKER_IMAGE_POSTGRES = "postgres:11.2";
	@BeforeClass
	public static void setUp() {
		Network network = Network.newNetwork();

		// Postgres database
		new PostgreSQLContainer(DOCKER_IMAGE_POSTGRES)
			.withInitScript("init.sql") // create schema and add test data
			.withDatabaseName("CarPartsStore")
			.withUsername("postgres")
			.withPassword("postgres")
			.withNetwork(network)
			.withNetworkAliases("testcontainers-demo-postgres")
			.start();


	}
	@Test
	void contextLoads() {
	}

	@Test
	void productServiceTest(){
		productsService.addProduct();
	}

}
