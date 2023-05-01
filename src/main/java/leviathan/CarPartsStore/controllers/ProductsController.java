package leviathan.CarPartsStore.controllers;

import org.springframework.stereotype.Controller;

@Controller
public class ProductsController {
    /*
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/catalog/{catalogUUID}")
    public ModelAndView catalog(OAuth2AuthenticationToken oAuth2AuthenticationToken, @PathVariable UUID catalogUUID) {
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
        mav.addObject("top5Catalogs", catalogService.getTop5ByPopularity());

        Catalog currentCatalog = catalogService.getCatalogByUUID(catalogUUID);
        currentCatalog.setPopularity(currentCatalog.getPopularity()+1);
        catalogService.saveCatalog(currentCatalog);
        mav.addObject("catalogName", currentCatalog.getName());
        mav.addObject("catalogUUID", catalogUUID);
        mav.addObject("products", products(productsService.getAllActiveProductsByCatalogUUID(catalogUUID), oAuth2AuthenticationToken));
        return mav;
    }

    @GetMapping("/catalog")
    public String search(@RequestParam(name = "search") String name, OAuth2AuthenticationToken auth2AuthenticationToken) {
        return "catalog";
    }
/*
    @GetMapping("/catalog/{catalogUUID}/product/{productUUID}")
    public String product(OAuth2AuthenticationToken oAuth2AuthenticationToken, @PathVariable UUID catalogUUID, @PathVariable UUID productUUID, Map<String, Object> model) {
        boolean isAuthorised = false;
        if (authorizationService.isUserAuthorized(oAuth2AuthenticationToken)) {
            isAuthorised = true;
            User user = userService.getUser(oAuth2AuthenticationToken);
            model.put("amount", cartService.getAmountOfItemsInCart(user.getUuid()));
            model.put("price", cartService.getTotalPriceOfItemsInCart(user.getUuid()));
            model.put("name", user.getName());
            model.put("id", user.getUuid());
            model.put("pfp", user.getAvatar());
            model.put("user", user);
            model.put("isAdmin", user.getAuthorities().contains(Roles.ADMIN));
        }
        model.put("isAuthorised", isAuthorised);
        Product product = productsService.getProductFromBD(productUUID);
        model.put("product", product);
        model.put("catalogUUID", catalogUUID);
        model.put("catalogName", product.getCatalog().getName());
        model.put("reviews", reviewService.getAllActiveReviewsByProduct(product));
        model.put("top5Catalogs", catalogService.getTop5ByPopularity());
        return "product";
    }

    @PostMapping("/catalog/{catalogUUID}/product/{productUUID}")
    public String addReview(OAuth2AuthenticationToken auth2AuthenticationToken,
                            @PathVariable UUID catalogUUID,
                            @PathVariable UUID productUUID,
                            @RequestParam String body,
                            @RequestParam int mark
    ) {
        if (authorizationService.isUserAuthorized(auth2AuthenticationToken)) {
            User author = userService.getUser(auth2AuthenticationToken);
            Product product = productsService.getProductFromBD(productUUID);
            reviewService.postAReview(product, author, body, mark);
        }
        else return "redirect:/login";
        return "redirect:/catalog/" + catalogUUID + "/product/" + productUUID + "#review-input";
    }

    @PostMapping("/catalog/{catalogUUID}/product/{productUUID}/edit-spec")
    public String addSpecification(OAuth2AuthenticationToken auth2AuthenticationToken,
                                   @PathVariable UUID catalogUUID,
                                   @PathVariable UUID productUUID,
                                   @RequestParam String specificationCategory,
                                   @RequestParam String name,
                                   @RequestParam String value) {
        productsService.addSpecification(productsService.getProductFromBD(productUUID), specificationCategory, name, value);
        return "redirect:/catalog/" + catalogUUID + "/product/" + productUUID;
    }



    @GetMapping("/catalog/{catalogUUID}/product/{productUUID}/remove-review/{reviewUUID}")
    public String removeReview(OAuth2AuthenticationToken auth2AuthenticationToken,
                               @PathVariable UUID catalogUUID,
                               @PathVariable UUID productUUID,
                               @PathVariable UUID reviewUUID
    ) {
        if (authorizationService.isUserAuthorized(auth2AuthenticationToken)) {
            if (userService.getUser(auth2AuthenticationToken).getAuthorities().contains(Roles.ADMIN)) {

            }
        }
        return "redirect:/catalog/" + catalogUUID + "/product/" + productUUID;
    }

    public List<Map<String, Object>> products(Iterable<Product> products, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        List<Map<String, Object>> result = new ArrayList<>();
        if(authorizationService.isUserAuthorized(oAuth2AuthenticationToken)) {
            Cart cart = userService.getUser(oAuth2AuthenticationToken).getCart();
            for (Product product : products) {
                Map<String, Object> map = new HashMap<>();
                map.put("product", product);
                map.put("isInCart", cartService.isProductInCart(cart, product));
                result.add(map);
            }
        }
        else {
            for (Product product : products) {
                Map<String, Object> map = new HashMap<>();
                map.put("product", product);
                map.put("isInCart", false);
                result.add(map);
            }
        }
        return result;
    }

     */
}
