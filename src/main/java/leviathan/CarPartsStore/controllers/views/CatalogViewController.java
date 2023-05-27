package leviathan.CarPartsStore.controllers.views;

import jakarta.servlet.http.HttpServletResponse;
import leviathan.CarPartsStore.domain.CatalogDTO;
import leviathan.CarPartsStore.domain.ProductDTO;
import leviathan.CarPartsStore.domain.SortingType;
import leviathan.CarPartsStore.domain.UserDTO;
import leviathan.CarPartsStore.services.AuthorizationService;
import leviathan.CarPartsStore.services.CatalogService;
import leviathan.CarPartsStore.services.ProductsService;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/catalog")
public class CatalogViewController {
    private final String rootCatalogUUID = "2ba366e2-3f87-4f52-8931-76918748557d";
    private final CatalogService catalogService;
    private final AuthorizationService authorizationService;
    private final ProductsService productsService;

    public CatalogViewController(CatalogService catalogService, AuthorizationService authorizationService, ProductsService productsService) {
        this.catalogService = catalogService;
        this.authorizationService = authorizationService;
        this.productsService = productsService;
    }

    @GetMapping
    public String redirectToMainPage(@RequestParam(required = false, name = "s") String searchQuery) {
        if (searchQuery != null) {
            searchQuery = "?s=" + searchQuery;
        }
        else searchQuery = "";
        return "redirect:/catalog/" + rootCatalogUUID + searchQuery;
    }

    @GetMapping("/{catalogUUID}")
    public ModelAndView catalog(@PathVariable UUID catalogUUID,
                                @RequestParam(name = "s", required = false, defaultValue = "") String searchRequest,
                                @RequestParam(name = "sort", required = false, defaultValue = "POPULARITY_DESC") SortingType sortingType,
                                OAuth2AuthenticationToken oAuth2AuthenticationToken,
                                HttpServletResponse httpServletResponse) throws IOException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("catalog");
        mav.addObject("searchRequest", searchRequest);
        mav.addObject("top4Catalogs", catalogService.getTop4ActiveByPopularity());
        try {
            mav.addObject("currentCatalog", catalogService.getCatalogByUUID(catalogUUID));

            boolean isAuthenticated = authorizationService.isUserAuthenticated(oAuth2AuthenticationToken);
            mav.addObject("isAuthenticated", isAuthenticated);
            UserDTO user = null;
            if (isAuthenticated) {
                user = authorizationService.authorize(oAuth2AuthenticationToken);
            }

            List<CatalogDTO> catalogs = catalogService.getActiveChildCatalogs(catalogUUID);
            mav.addObject("childrenCatalogs", catalogs);
            mav.addObject("isChildrenCatalogsEmpty", catalogs.isEmpty());
            List<ProductDTO> products = productsService.getAllActiveProductsByCatalogUUID(catalogUUID, sortingType, user);
            mav.addObject("products", products);
            mav.addObject("isProductsEmpty", products.isEmpty());
            mav.addObject("amountOfProducts", products.size());

            List<CatalogDTO> catalogPath = catalogService.getCatalogPath(catalogUUID);
            mav.addObject("catalogPath", catalogPath);
            mav.addObject("isCatalogPathEmpty", catalogPath.isEmpty());

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
}
