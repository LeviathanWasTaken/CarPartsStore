package leviathan.CarPartsStore.controllers;

import leviathan.CarPartsStore.domain.Cart;
import leviathan.CarPartsStore.domain.CartItem;
import leviathan.CarPartsStore.domain.User;
import leviathan.CarPartsStore.model.Status;
import leviathan.CarPartsStore.services.AuthorizationService;
import leviathan.CarPartsStore.services.CartService;
import leviathan.CarPartsStore.services.CatalogService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

@Controller
public class MainController {
    private final CatalogService catalogService;
    private final AuthorizationService authorizationService;
    private final CartService cartService;


    public MainController(AuthorizationService authorizationService,
                          CatalogService catalogService,
                          CartService cartService) {
        this.authorizationService = authorizationService;
        this.catalogService = catalogService;
        this.cartService = cartService;
    }

    @GetMapping("/")
    public ModelAndView homePage(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("home");
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
        //mav.addObject("allCatalogs", catalogService.getAllActiveCatalogs());
        return mav;
    }
}
