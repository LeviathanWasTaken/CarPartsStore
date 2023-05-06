package leviathan.CarPartsStore.controllers;

import leviathan.CarPartsStore.domain.UserDTO;
import leviathan.CarPartsStore.services.AuthorizationService;
import leviathan.CarPartsStore.services.CatalogService;
import leviathan.CarPartsStore.services.ProductsService;
import leviathan.CarPartsStore.services.UserService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CatalogController {

    private final AuthorizationService authorizationService;
    private final CatalogService catalogService;
    private final UserService userService;
    private final ProductsService productsService;

    public CatalogController(AuthorizationService authorizationService,
                             CatalogService catalogService,
                             UserService userService, ProductsService productsService) {
        this.authorizationService = authorizationService;
        this.catalogService = catalogService;
        this.userService = userService;
        this.productsService = productsService;
    }
/*
    @GetMapping("/catalog/{catalogTag}")
    public ModelAndView getCatalog(@PathVariable UUID catalogTag, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("catalog");
        mav = userService.putMainUserInfo(mav, oAuth2AuthenticationToken, authorizationService);

        //mav.addObject("top5Catalogs", catalogService.getTop5ByPopularity());

        return mav;
    }

 */
    @GetMapping("/catalog/{catalogName}")
    public ModelAndView catalog(@PathVariable String catalogName,
                                @RequestParam(required = false, defaultValue = "0") int page,
                                OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("catalog");
        mav.addObject("top5Catalogs", catalogService.getTop5ActiveByPopularity());
        boolean isAuthenticated = authorizationService.isUserAuthenticated(oAuth2AuthenticationToken);
        mav.addObject("isAuthenticated", isAuthenticated);
        if (isAuthenticated) {
            UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
            mav.addObject("user", user);
            mav.addObject("cart", userService.getUserCartByUserUUID(user.getUserUUID()));
        }
        mav.addObject("childrenCatalogs", catalogService.getActiveChildCatalogs(catalogName));
        mav.addObject("products", productsService.getPageOfActiveProductsByCatalogUUID(
                catalogService.getCatalogByName(catalogName).getCatalogUUID(), page));
        return mav;
    }
}
