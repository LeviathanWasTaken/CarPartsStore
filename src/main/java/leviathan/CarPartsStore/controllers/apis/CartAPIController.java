package leviathan.CarPartsStore.controllers.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import leviathan.CarPartsStore.domain.CartItemDTO;
import leviathan.CarPartsStore.domain.ProductDTO;
import leviathan.CarPartsStore.domain.UserDTO;
import leviathan.CarPartsStore.services.AuthorizationService;
import leviathan.CarPartsStore.services.CartService;
import leviathan.CarPartsStore.services.ProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartAPIController {
    private final AuthorizationService authorizationService;
    private final CartService cartService;

    public CartAPIController(AuthorizationService authorizationService, CartService cartService) {
        this.authorizationService = authorizationService;
        this.cartService = cartService;
    }

    @PutMapping("/add-product")
    public ResponseEntity<String> addToCart(@RequestBody String body, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        System.out.println("addToCart called with " + body);
        String productUUIDValue = body.split(":")[1].replaceAll("\"", "").replaceAll("}", "");
        UUID productUUID = UUID.fromString(productUUIDValue);
        System.out.println("UUID " + productUUID);
        try {
            UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
            cartService.addProductToCart(user, productUUID);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ClientAuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Product with UUID " + productUUID + " successfully added to cart");
    }

    @PutMapping("/increase")
    public ResponseEntity<String> increaseQuantity(@RequestBody String cartItemUUIDString, OAuth2AuthenticationToken oAuth2AuthenticationToken) throws JsonProcessingException {
        System.out.println(cartItemUUIDString);
        UUID cartItemUUID = UUID.fromString(cartItemUUIDString.split(":")[1].replaceAll("\"", "").replaceAll("}", ""));
        UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
        if (cartService.hasUserCartCartItem(user, cartItemUUID)) {
            cartService.increaseCartItemQuantity(user, cartItemUUID);
            ObjectMapper objectMapper = new ObjectMapper();
            CartItemDTO cartItem = cartService.getCartItemByUUID(cartItemUUID);
            return ResponseEntity.status(HttpStatus.OK).body(objectMapper.writeValueAsString(cartItem));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/decrease")
    public ResponseEntity<String> decreaseQuantity(@RequestBody String cartItemUUIDString, OAuth2AuthenticationToken oAuth2AuthenticationToken) throws JsonProcessingException {
        System.out.println(cartItemUUIDString);
        UUID cartItemUUID = UUID.fromString(cartItemUUIDString.split(":")[1].replaceAll("\"", "").replaceAll("}", ""));
        UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
        if (cartService.hasUserCartCartItem(user, cartItemUUID)) {
            ObjectMapper objectMapper = new ObjectMapper();
            cartService.decreaseCartItemQuantity(user, cartItemUUID);
            CartItemDTO cartItem = cartService.getCartItemByUUID(cartItemUUID);
            return ResponseEntity.status(HttpStatus.OK).body(objectMapper.writeValueAsString(cartItem));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<String> removeItem(@RequestBody String cartItemUUIDString, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        System.out.println(cartItemUUIDString);
        UUID cartItemUUID = UUID.fromString(cartItemUUIDString.split(":")[1].replaceAll("\"", "").replaceAll("}", ""));
        UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
        if (cartService.hasUserCartCartItem(user, cartItemUUID)) {
            cartService.removeCartItem(user, cartItemUUID);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
