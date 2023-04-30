package leviathan.CarPartsStore.controllers;

import leviathan.CarPartsStore.domain.Cart;
import leviathan.CarPartsStore.domain.User;
import leviathan.CarPartsStore.services.AuthorizationService;
import leviathan.CarPartsStore.services.CartService;
import leviathan.CarPartsStore.services.CatalogService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class CatalogController {
    private final AuthorizationService authorizationService;
    private final CartService cartService;
    private final CatalogService catalogService;

    public CatalogController(AuthorizationService authorizationService, CartService cartService, CatalogService catalogService) {
        this.authorizationService = authorizationService;
        this.cartService = cartService;
        this.catalogService = catalogService;
    }

    @GetMapping("/catalog/{catalogUUID}")
    public ModelAndView getCatalog(@PathVariable UUID catalogUUID, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("catalog");
        boolean isUserAuthorized = oAuth2AuthenticationToken != null;
        mav.addObject("isUserAuthorized", isUserAuthorized);
        if (isUserAuthorized) {
            User user = authorizationService.authorize(oAuth2AuthenticationToken);
            Cart cart = user.getCart();
            mav.addObject("user", user);
            mav.addObject("totalPriceOfItemsInCart", cartService.calculateTotalPriceOfActiveElementsInCart(cart));
            mav.addObject("totalAmountOfItemsInCart", cartService.calculateTotalAmountOfActiveElementsInCart(cart));
        }
        //mav.addObject("top5Catalogs", catalogService.getTop5ByPopularity());



        return mav;
    }
}
