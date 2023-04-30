package leviathan.CarPartsStore.controllers;

import leviathan.CarPartsStore.domain.Cart;
import leviathan.CarPartsStore.domain.User;
import leviathan.CarPartsStore.services.AuthorizationService;
import leviathan.CarPartsStore.services.CartService;
import leviathan.CarPartsStore.services.CatalogService;
import leviathan.CarPartsStore.services.UserService;
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
    private final UserService userService;

    public CatalogController(AuthorizationService authorizationService, CartService cartService, CatalogService catalogService, UserService userService) {
        this.authorizationService = authorizationService;
        this.cartService = cartService;
        this.catalogService = catalogService;
        this.userService = userService;
    }

    @GetMapping("/catalog/{catalogTag}")
    public ModelAndView getCatalog(@PathVariable UUID catalogTag, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("catalog");
        mav = userService.putMainUserInfo(mav, oAuth2AuthenticationToken, authorizationService);

        //mav.addObject("top5Catalogs", catalogService.getTop5ByPopularity());



        return mav;
    }
}
