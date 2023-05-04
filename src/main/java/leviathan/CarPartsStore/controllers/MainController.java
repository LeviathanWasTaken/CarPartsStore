package leviathan.CarPartsStore.controllers;

import leviathan.CarPartsStore.services.AuthorizationService;
import leviathan.CarPartsStore.services.CartService;
import leviathan.CarPartsStore.services.CatalogService;
import leviathan.CarPartsStore.services.UserService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    private final CatalogService catalogService;
    private final AuthorizationService authorizationService;
    private final CartService cartService;
    private final UserService userService;


    public MainController(AuthorizationService authorizationService,
                          CatalogService catalogService,
                          CartService cartService,
                          UserService userService) {
        this.authorizationService = authorizationService;
        this.catalogService = catalogService;
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView homePage(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("home");
        mav.addObject("top5Catalogs", catalogService.getTop5ActiveByPopularity());
        mav.addObject("allCatalogs", catalogService.getActiveChildCatalogs("ROOT"));

        /*
        mav = userService.putMainUserInfo(mav, oAuth2AuthenticationToken, authorizationService);
        mav.addObject("top5Catalogs", catalogService.getTop5ActiveByPopularity());
        mav.addObject("allCatalogs", catalogService.getAllActiveChildCatalogs("ROOT"));

         */
        return mav;
    }
}
