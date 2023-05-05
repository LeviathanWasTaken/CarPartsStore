package leviathan.CarPartsStore.controllers;

import jakarta.servlet.http.HttpServletResponse;
import leviathan.CarPartsStore.domain.CatalogDTO;
import leviathan.CarPartsStore.domain.UserDTO;
import leviathan.CarPartsStore.services.AuthorizationService;
import leviathan.CarPartsStore.services.CatalogService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class AdminController {
    private final AuthorizationService authorizationService;
    private final CatalogService catalogService;

    public AdminController(AuthorizationService authorizationService, CatalogService catalogService) {
        this.authorizationService = authorizationService;
        this.catalogService = catalogService;
    }
    /*
    private final UserService userService;
    private final CatalogService catalogService;
    private final ProductsService productsService;

    public AdminController(UserService userService, CatalogService catalogService, ProductsService productsService) {
        this.userService = userService;
        this.catalogService = catalogService;
        this.productsService = productsService;
    }


    @GetMapping("/admin")
    public ModelAndView admin(OAuth2AuthenticationToken auth2AuthenticationToken, HttpServletResponse response) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");

        if (!userService.getUser(auth2AuthenticationToken).getAuthorities().contains(Roles.ADMIN)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return modelAndView;
        }

        modelAndView.addObject("isAdmin", userService.getUser(auth2AuthenticationToken).getAuthorities().contains(Roles.ADMIN));
        Iterable<Catalog> catalogs = catalogService.getAllCatalogs();
        modelAndView.addObject("catalogs", catalogs(catalogs));
        Iterable<Product> products = productsService.getAllProducts();
        modelAndView.addObject("products", products(products, catalogs));
        return modelAndView;
    }
/*
    @GetMapping("/admin/remove/catalog/{catalogUUID}")
    public String removeCatalog(@PathVariable UUID catalogUUID, OAuth2AuthenticationToken auth2AuthenticationToken, HttpServletResponse response) throws IOException {
        boolean isAuthorized = auth2AuthenticationToken != null;
        if (isAuthorized) {
            if (!userService.getUser(auth2AuthenticationToken).getAuthorities().contains(Roles.ADMIN)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            else {
                catalogService.removeCatalog(catalogUUID);
            }
        }
        else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/remove/product/{productUUID}")
    public String removeProduct(@PathVariable UUID productUUID, OAuth2AuthenticationToken auth2AuthenticationToken, HttpServletResponse response) throws IOException {
        boolean isAuthorized = auth2AuthenticationToken != null;
        if (isAuthorized) {
            if (!userService.getUser(auth2AuthenticationToken).getAuthorities().contains(Roles.ADMIN)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            else {
                productsService.removeProduct(productUUID);
            }
        }
        else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/restore/catalog/{catalogUUID}")
    public String restoreCatalog(@PathVariable UUID catalogUUID, OAuth2AuthenticationToken auth2AuthenticationToken, HttpServletResponse response) throws IOException {
        boolean isAuthorized = auth2AuthenticationToken != null;
        if (isAuthorized) {
            if (!userService.getUser(auth2AuthenticationToken).getAuthorities().contains(Roles.ADMIN)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            else {
                catalogService.restoreCatalog(catalogUUID);
            }
        }
        else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/restore/product/{productUUID}")
    public String restoreProduct(@PathVariable UUID productUUID, OAuth2AuthenticationToken auth2AuthenticationToken, HttpServletResponse response) throws IOException {
        boolean isAuthorized = auth2AuthenticationToken != null;
        if (isAuthorized) {
            if (!userService.getUser(auth2AuthenticationToken).getAuthorities().contains(Roles.ADMIN)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            else {
                productsService.restoreProduct(productUUID);
            }
        }
        else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/edit/catalog")
    public String removeCatalog(OAuth2AuthenticationToken auth2AuthenticationToken,
                                HttpServletResponse response,
                                @RequestParam UUID catalogUUID,
                                @RequestParam String catalogName,
                                @RequestParam String imgSource,
                                @RequestParam(required = false) UUID parentCatalogUUID) throws IOException {
        boolean isAuthorized = auth2AuthenticationToken != null;
        if (isAuthorized) {
            if (!userService.getUser(auth2AuthenticationToken).getAuthorities().contains(Roles.ADMIN)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            else {
                catalogService.(catalogUUID);
            }
        }
        else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/edit/product/{productUUID}")
    public String removeProduct(@PathVariable UUID productUUID, OAuth2AuthenticationToken auth2AuthenticationToken, HttpServletResponse response) throws IOException {
        boolean isAuthorized = auth2AuthenticationToken != null;
        if (isAuthorized) {
            if (!userService.getUser(auth2AuthenticationToken).getAuthorities().contains(Roles.ADMIN)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            else {
                productsService.removeProduct(productUUID);
            }
        }
        else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return "redirect:/admin";
    }

    /*
    @PostMapping("/admin/edit/add-product")
    public String addProduct(@RequestParam String productName, @RequestParam String imgSource, @RequestParam int priceInPennies, @RequestParam String catalogName) {
        productsService.addProduct(productName, imgSource, priceInPennies, catalogName);
        return "redirect:/admin";
    }

    @PostMapping("/admin/edit/modify-product/{UUID}")
    public String modifyProduct(@PathVariable UUID UUID,
                                @RequestParam String catalogName,
                                @RequestParam String productName,
                                @RequestParam String imgSource,
                                @RequestParam int priceInPennies,
                                @RequestParam Status status) {
        productsService.modifyProduct(UUID, catalogName, productName, imgSource, priceInPennies, status);
        return "redirect:/admin";
    }

    @PostMapping("/admin/edit/add-catalog")
    public String addCatalog(@RequestParam String catalogName, @RequestParam String imgSource) {
        catalogService.addNewCatalog(catalogName, imgSource);
        return "redirect:/admin";
    }

    @PostMapping("/admin/edit/modify-catalog/{UUID}")
    public String modifyCatalog(@PathVariable UUID UUID,
                                @RequestParam String imgSource,
                                @RequestParam String catalogName,
                                @RequestParam long popularity,
                                @RequestParam Status status) {
        catalogService.modifyCatalog(UUID, imgSource, catalogName, popularity, status);
        return "redirect:/admin";
    }
    @GetMapping("/admin/modify")
    public String modifySpecification(OAuth2AuthenticationToken auth2AuthenticationToken, @RequestParam(name = "p") UUID productUUID, Map<String, Object> model) {
        model.put("isAdmin", userService.getUser(auth2AuthenticationToken).getAuthorities().contains(Roles.ADMIN));
        return "product";
    }



    private List<Map<String, Object>> products(Iterable<Product> products, Iterable<Catalog> catalogs) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Product product : products) {
            Map<String, Object> map = new HashMap<>();
            map.put("product", product);
            List<Catalog> catalogsUnchecked = new ArrayList<>();
            map.put("statusUnselected", product.getStatus().equals(Status.ACTIVE)?
                    Status.REMOVED : product.getStatus().equals(Status.CATALOG_REMOVED)? Status.REMOVED : Status.ACTIVE);
            for (Catalog catalog : catalogs) {
                if (!product.getCatalog().equals(catalog)) {
                    catalogsUnchecked.add(catalog);
                }
            }
            map.put("catalogs", catalogsUnchecked);
            result.add(map);
        }
        return result;
    }

    private List<Map<String, Object>> catalogs(Iterable<Catalog> catalogs) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Catalog catalog : catalogs) {
            Map<String, Object> map = new HashMap<>();
            map.put("catalog", catalog);
            map.put("statusUnselected", catalog.getStatus().equals(Status.ACTIVE)? Status.CATALOG_REMOVED : Status.ACTIVE);
            result.add(map);
        }
        return result;
    }

     */

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
        mav.addObject("top5Catalogs", catalogService.getTop5ActiveByPopularity());
        mav.addObject("message", message);
        mav.addObject("messageType", messageType);
        mav.addObject("catalogs", catalogService.getAllCatalogs());
        return mav;
    }

    @PostMapping("/admin/catalog")
    public String addCatalog(OAuth2AuthenticationToken oAuth2AuthenticationToken,
                             @RequestParam String catalogName,
                             @RequestParam String catalogPicture,
                             @RequestParam String parentCatalogName) {
        String message;
        String messageType;
        UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
        if(authorizationService.isUserAdmin(user.getUserUUID())) {
            CatalogDTO newCatalog = new CatalogDTO(catalogName, catalogPicture, parentCatalogName);
            catalogService.addNewCatalog(newCatalog);
            message = "Catalog " + catalogName + " successfully created.";
            messageType = "success";
        }
        else {
            message = "Unauthorized request";
            messageType = "error";
        }
        return "redirect:/admin?message=" + message + "&messageType=" + messageType;
    }
}
