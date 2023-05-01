package leviathan.CarPartsStore.controllers;

import leviathan.CarPartsStore.services.CartService;
import leviathan.CarPartsStore.services.ProductsService;
import leviathan.CarPartsStore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CartController {

    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductsService productsService;
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
}
