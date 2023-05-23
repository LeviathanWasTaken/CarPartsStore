package leviathan.CarPartsStore.controllers;

import jakarta.servlet.http.HttpServletResponse;
import leviathan.CarPartsStore.domain.CartItemDTO;
import leviathan.CarPartsStore.domain.ProductDTO;
import leviathan.CarPartsStore.domain.UserDTO;
import leviathan.CarPartsStore.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class CartController {

    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private CatalogService catalogService;
    private String rootCatalogUUID = "2ba366e2-3f87-4f52-8931-76918748557d";

    /*
        @GetMapping("/cart")
        public String cart(OAuth2AuthenticationToken auth2AuthenticationToken, Map<String, Object> model) {
            User user = userService.getUser(auth2AuthenticationToken);
            if (!user.getCart().getCartItems().isEmpty()) {
                model.put("activeCartItems", cartService.getActiveCartItems(user.getCart()));
                model.put("removedCartItems", cartService.getRemovedCartItems(user.getCart()));
                model.put("total", cartService.getTotalPriceOfItemsInCart(user.getUuid()));
                model.put("isEmpty", false);
            }
            else {
                model.put("isEmpty", true);
            }
            return "cart";
        }

        @GetMapping("/cart-remove/{cartItemUUID}")
        public String remove(@PathVariable UUID cartItemUUID) {
            cartService.removeCartItem(cartItemUUID);
            return "redirect:/cart";
        }

        @PostMapping("/cart-editQuantity/{cartItemUUID}")
        public String edit(@PathVariable UUID cartItemUUID, @RequestParam int quantity) {
            if (quantity < 1) {
                return "redirect:/cart-remove/" + cartItemUUID;
            }
            cartService.setNewQuantity(cartItemUUID, quantity);
            return "redirect:/cart";
        }

        @PostMapping("/cart/add-to-cart")
        public String addToCart(OAuth2AuthenticationToken oAuth2AuthenticationToken,
                                @RequestParam int quantity,
                                @RequestParam(name = "p") UUID productUUID) {
            Product product = productsService.getProductFromBD(productUUID);
            User user = userService.getUser(oAuth2AuthenticationToken);
            cartService.addItemInCart(user, product, quantity);
            return "redirect:/catalog/" + product.getCatalog().getUUID();
        }

     */
    @GetMapping("/cart")
    public ModelAndView cart(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("cart");
        mav.addObject("top4Catalogs", catalogService.getTop4ActiveByPopularity());
        mav.addObject("rootCatalog", catalogService.getCatalogByUUID(UUID.fromString(rootCatalogUUID)));
        mav.addObject("catalogsList", catalogService.getActiveChildCatalogs(UUID.fromString(rootCatalogUUID)));
        UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
        List<CartItemDTO> cartItems = cartService.getCartItems(user);
        mav.addObject("cartItems", cartItems);
        mav.addObject("isCartEmpty", cartItems.isEmpty());
        return mav;
    }

    @GetMapping("/cart/add/{productUUID}")
    public String addProductToCart(@PathVariable UUID productUUID, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
        ProductDTO product = productsService.getProductByUUID(productUUID);
        UUID catalogUUID = product.getProductCatalogUUID();
        cartService.addProductToCart(user, productUUID);
        return "redirect:/catalog/" + catalogUUID;
    }

    @GetMapping("/cart/increase-quantity/{cartItemUUID}")
    public String increaseQuantity(
            @PathVariable UUID cartItemUUID,
            OAuth2AuthenticationToken oAuth2AuthenticationToken,
            HttpServletResponse httpServletResponse) throws IOException {
        UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
        if (cartService.hasUserCartCartItem(user, cartItemUUID)) {
            cartService.increaseCartItemQuantity(user, cartItemUUID);
            return "redirect:/cart";
        } else {
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return "redirect:/";
        }
    }

    @GetMapping("/cart/decrease-quantity/{cartItemUUID}")
    public String decreaseQuantity(
            @PathVariable UUID cartItemUUID,
            OAuth2AuthenticationToken oAuth2AuthenticationToken,
            HttpServletResponse httpServletResponse) throws IOException {
        UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
        if (cartService.hasUserCartCartItem(user, cartItemUUID)) {
            cartService.decreaseCartItemQuantity(user, cartItemUUID);
            return "redirect:/cart";
        } else {
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return "redirect:/";
        }
    }

    @GetMapping("/cart/remove-item/{cartItemUUID}")
    public String removeCartItem(
            @PathVariable UUID cartItemUUID,
            OAuth2AuthenticationToken oAuth2AuthenticationToken,
            HttpServletResponse httpServletResponse) throws IOException {
        UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
        if (cartService.hasUserCartCartItem(user, cartItemUUID)) {
            cartService.removeCartItem(user, cartItemUUID);
            return "redirect:/cart";
        } else {
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return "redirect:/";
        }
    }
}
