package leviathan.CarPartsStore.controllers;

import jakarta.servlet.http.HttpServletResponse;
import leviathan.CarPartsStore.domain.CatalogDTO;
import leviathan.CarPartsStore.domain.ProductDTO;
import leviathan.CarPartsStore.domain.UserDTO;
import leviathan.CarPartsStore.services.AuthorizationService;
import leviathan.CarPartsStore.services.CatalogService;
import leviathan.CarPartsStore.services.ProductsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.UUID;

@Controller
public class AdminController {
    private final AuthorizationService authorizationService;
    private final CatalogService catalogService;
    private final ProductsService productsService;
    private final String rootCatalogUUID = "2ba366e2-3f87-4f52-8931-76918748557d";

    public AdminController(AuthorizationService authorizationService, CatalogService catalogService, ProductsService productsService) {
        this.authorizationService = authorizationService;
        this.catalogService = catalogService;
        this.productsService = productsService;
    }

    @GetMapping("/admin")
    public ModelAndView admin(@RequestParam(required = false) String message,
                              @RequestParam(required = false, defaultValue = "default") String messageType,
                              OAuth2AuthenticationToken oAuth2AuthenticationToken,
                              HttpServletResponse httpServletResponse) throws IOException {
        ModelAndView mav = new ModelAndView("admin");
        UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
        boolean isUserAdmin = authorizationService.isUserAdmin(user.getUserUUID());
        if (!isUserAdmin) {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return mav;
        }
        mav.addObject("rootCatalog", catalogService.getCatalogByUUID(UUID.fromString(rootCatalogUUID)));
        mav.addObject("top5Catalogs", catalogService.getTop5ActiveByPopularity());
        mav.addObject("message", message);
        mav.addObject("messageType", messageType);
        mav.addObject("catalogs", catalogService.getAllCatalogs());
        return mav;
    }

    @PostMapping("/admin/catalog")
    public String addCatalog(OAuth2AuthenticationToken oAuth2AuthenticationToken,
                             CatalogDTO newCatalog,
                             HttpServletResponse httpServletResponse) throws IOException {
        String message;
        String messageType;
        UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
        if(authorizationService.isUserAdmin(user.getUserUUID())) {
            try {
                catalogService.addNewCatalog(newCatalog);
            } catch (IllegalArgumentException e) {
                return "redirect:/admin?message=" + e.getMessage() + "&messageType=error";
            } finally {
                message = "Catalog " + newCatalog.getCatalogName() + " successfully created.";
                messageType = "success";
            }
        }
        else {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:/admin";
        }
        return "redirect:/admin?message=" + message + "&messageType=" + messageType;
    }

    @PostMapping("/admin/catalog/edit")
    public String editCatalog(OAuth2AuthenticationToken oAuth2AuthenticationToken,
                              CatalogDTO catalog,
                              HttpServletResponse httpServletResponse) throws IOException {
        String message;
        String messageType;
        UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
        if(authorizationService.isUserAdmin(user.getUserUUID())) {
            try {
                catalogService.modifyCatalog(catalog);
            } catch (IllegalArgumentException e) {
                return "redirect:/admin?message=" + e.getMessage() + "&messageType=error";
            } finally {
                message = "Catalog " + catalog.getCatalogName() + " successfully modified";
                messageType = "success";
            }
        }
        else {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:/admin";
        }
        return "redirect:/admin?message=" + message + "&messageType=" + messageType;
    }

    @GetMapping("/admin/catalog/{catalogUUID}/remove")
    public String removeCatalog(@PathVariable UUID catalogUUID,
                                OAuth2AuthenticationToken oAuth2AuthenticationToken,
                                HttpServletResponse httpServletResponse) throws IOException {
        String message;
        String messageType;
        UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
        if(authorizationService.isUserAdmin(user.getUserUUID())) {
            try {
                catalogService.removeCatalog(catalogUUID);
            } catch (IllegalArgumentException e) {
                return "redirect:/admin?message=" + e.getMessage() + "&messageType=error";
            } finally {
                message = "Catalog successfully removed";
                messageType = "success";
            }
        }
        else {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:/admin";
        }
        return "redirect:/admin?message=" + message + "&messageType=" + messageType;
    }

    @GetMapping("/admin/catalog/{catalogUUID}/restore")
    public String restoreCatalog(@PathVariable UUID catalogUUID,
                                OAuth2AuthenticationToken oAuth2AuthenticationToken,
                                HttpServletResponse httpServletResponse) throws IOException {
        String message;
        String messageType;
        UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
        if(authorizationService.isUserAdmin(user.getUserUUID())) {
            try {
                catalogService.restoreCatalog(catalogUUID);
            } catch (IllegalArgumentException e) {
                return "redirect:/admin?message=" + e.getMessage() + "&messageType=error";
            } finally {
                message = "Catalog successfully restored";
                messageType = "success";
            }
        }
        else {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:/admin";
        }
        return "redirect:/admin?message=" + message + "&messageType=" + messageType;
    }

    @PostMapping("/admin/product")
    public String addProduct(ProductDTO product,
                             OAuth2AuthenticationToken oAuth2AuthenticationToken,
                             HttpServletResponse httpServletResponse) throws IOException {
        String message;
        String messageType;
        UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
        if(authorizationService.isUserAdmin(user.getUserUUID())) {
            try {
                productsService.createProduct(product);
            } catch (IllegalArgumentException e) {
                return "redirect:/admin?message=" + e.getMessage() + "&messageType=error";
            } finally {
                message = "Product successfully created";
                messageType = "success";
            }
        }
        else {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:/admin";
        }
        return "redirect:/admin?message=" + message + "&messageType=" + messageType;
    }
}
