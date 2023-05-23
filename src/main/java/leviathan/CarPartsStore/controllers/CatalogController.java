package leviathan.CarPartsStore.controllers;

import jakarta.servlet.http.HttpServletResponse;
import leviathan.CarPartsStore.domain.*;
import leviathan.CarPartsStore.services.AuthorizationService;
import leviathan.CarPartsStore.services.CatalogService;
import leviathan.CarPartsStore.services.ProductsService;
import leviathan.CarPartsStore.services.UserService;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class CatalogController {

    private final String rootCatalogUUID = "2ba366e2-3f87-4f52-8931-76918748557d";
    private final AuthorizationService authorizationService;
    private final CatalogService catalogService;
    private final UserService userService;
    private final ProductsService productsService;

    public CatalogController(AuthorizationService authorizationService, CatalogService catalogService, UserService userService, ProductsService productsService) {
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

            //mav.addObject("top4Catalogs", catalogService.getTop5ByPopularity());

            return mav;
        }

     */
    @GetMapping("/catalog/{catalogUUID}")
    public ModelAndView catalog(@PathVariable UUID catalogUUID, @RequestParam(name = "s", required = false, defaultValue = "") String searchRequest, @RequestParam(name = "sort", required = false, defaultValue = "POPULARITY_DESC") SortingType sortingType, OAuth2AuthenticationToken oAuth2AuthenticationToken, HttpServletResponse httpServletResponse) throws IOException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("catalog");
        mav.addObject("top4Catalogs", catalogService.getTop4ActiveByPopularity());
        mav.addObject("searchRequest", searchRequest);
        mav.addObject("rootCatalog", catalogService.getCatalogByUUID(UUID.fromString(rootCatalogUUID)));
        try {
            mav.addObject("currentCatalog", catalogService.getCatalogByUUID(catalogUUID));

            boolean isAuthenticated = authorizationService.isUserAuthenticated(oAuth2AuthenticationToken);
            mav.addObject("isAuthenticated", isAuthenticated);
            UserDTO user = null;
            if (isAuthenticated) {
                user = authorizationService.authorize(oAuth2AuthenticationToken);
                mav.addObject("userInfo", user);
                mav.addObject("cart", userService.getUserCartByUserUUID(user.getUserUUID()));
            }

            List<CatalogDTO> catalogs = catalogService.getActiveChildCatalogs(catalogUUID);
            mav.addObject("childrenCatalogs", catalogs);
            mav.addObject("isChildrenCatalogsEmpty", catalogs.isEmpty());
            List<ProductDTO> products = productsService.getAllActiveProductsByCatalogUUID(catalogUUID, sortingType, user);
            mav.addObject("products", products);
            mav.addObject("isProductsEmpty", products.isEmpty());

            List<CatalogDTO> catalogPath = catalogService.getCatalogPath(catalogUUID);
            mav.addObject("catalogPath", catalogPath);
            mav.addObject("isCatalogPathEmpty", catalogPath.isEmpty());

            mav.addObject("amountOfProducts", products.size());

            mav.addObject("isProductInCart", true);

            mav.addObject("isProductOnSale", true);
        } catch (IllegalArgumentException e) {
            mav.setViewName("error.mustaches");
            mav.addObject("errorStatus", HttpStatusCode.valueOf(HttpServletResponse.SC_BAD_REQUEST));
            mav.addObject("errorMessage", e.getMessage());
            return mav;
        } catch (ClientAuthorizationException e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        return mav;
    }

    @GetMapping("/catalog")
    public String redirectToMainPage(@RequestParam(required = false, name = "s") String searchQuery) {
        if (searchQuery != null) {
            searchQuery = "?s=" + searchQuery;
        }
        else searchQuery = "";
        return "redirect:/catalog/" + rootCatalogUUID + searchQuery;
    }
}
