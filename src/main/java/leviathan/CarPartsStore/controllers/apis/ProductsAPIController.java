package leviathan.CarPartsStore.controllers.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import leviathan.CarPartsStore.domain.ProductDTO;
import leviathan.CarPartsStore.domain.SortingType;
import leviathan.CarPartsStore.domain.UserDTO;
import leviathan.CarPartsStore.services.AuthorizationService;
import leviathan.CarPartsStore.services.ProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductsAPIController {
    private final ProductsService productsService;
    private final AuthorizationService authorizationService;

    public ProductsAPIController(ProductsService productsService, AuthorizationService authorizationService) {
        this.productsService = productsService;
        this.authorizationService = authorizationService;
    }

    @GetMapping("/{catalogUUID}")
    public ResponseEntity<String> sendAllActiveProductsByCatalogUUID(@PathVariable UUID catalogUUID,
                                                                              @RequestParam(name = "sort") SortingType sortingType,
                                                                              OAuth2AuthenticationToken oAuth2AuthenticationToken) throws JsonProcessingException {
        System.out.println(sortingType);
        try {
            UserDTO user = null;
            if (authorizationService.isUserAuthenticated(oAuth2AuthenticationToken)) {
                user = authorizationService.authorize(oAuth2AuthenticationToken);
            }
            List<ProductDTO> products = productsService.getAllActiveProductsByCatalogUUID(catalogUUID, sortingType, user);
            ObjectMapper objectMapper = new ObjectMapper();
            String response = objectMapper.writeValueAsString(products);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ClientAuthorizationException e) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(e.getMessage());
        }
    }
}
