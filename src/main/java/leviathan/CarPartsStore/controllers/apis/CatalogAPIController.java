package leviathan.CarPartsStore.controllers.apis;

import leviathan.CarPartsStore.domain.CatalogDTO;
import leviathan.CarPartsStore.services.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogAPIController {
    private final CatalogService catalogService;

    public CatalogAPIController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/get-top-4")
    public List<CatalogDTO> sendTop4Catalogs() {
        return catalogService.getTop4ActiveByPopularity();
    }
}
