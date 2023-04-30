package leviathan.CarPartsStore.services;

import leviathan.CarPartsStore.domain.*;
import leviathan.CarPartsStore.repos.CartItemRepo;
import leviathan.CarPartsStore.repos.CartRepo;
import leviathan.CarPartsStore.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductsService productsService;
    private final CartRepo cartRepo;
    private final CartService cartService;

    public UserService(CartService cartService, UserRepo userRepo, CartItemRepo cartItemRepo, ProductsService productsService, CartRepo cartRepo) {
        this.cartService = cartService;
        this.userRepo = userRepo;
        this.cartItemRepo = cartItemRepo;
        this.productsService = productsService;
        this.cartRepo = cartRepo;
    }
/*
    public User getUser(OAuth2AuthenticationToken auth2AuthenticationToken) {
        return authorizationService.authorize(auth2AuthenticationToken);
    }

 */

    public User createNewUserByGithubAttributes(Map<String, Object> attributes) {
        Cart cart = new Cart();
        cartRepo.save(cart);

        User user = new User((int) attributes.get("id"),
                (String) attributes.get("avatar_url"),
                (String) attributes.get("login"),
                cart,
                Collections.singleton(Roles.ADMIN)
        );
        userRepo.save(user);
        return user;
    }

    public Iterable<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void save(User user) {
        userRepo.save(user);
    }

    public Optional<User> getUserByGithubId(int id) {
        return userRepo.findByGithubId(id);
    }

    public Optional<User> getByUUID(UUID userUUID) {
        return userRepo.findById(userUUID);
    }



    public ModelAndView putMainUserInfo(ModelAndView mav, OAuth2AuthenticationToken oAuth2AuthenticationToken, AuthorizationService authorizationService) {
        boolean isUserAuthorized = oAuth2AuthenticationToken != null;
        mav.addObject("isUserAuthorized", isUserAuthorized);
        if (isUserAuthorized) {
            User user = authorizationService.authorize(oAuth2AuthenticationToken);
            Cart cart = user.getCart();
            mav.addObject("user", user);
            mav.addObject("totalPriceOfItemsInCart", cartService.calculateTotalPriceOfActiveElementsInCart(cart));
            mav.addObject("totalAmountOfItemsInCart", cartService.calculateTotalAmountOfActiveElementsInCart(cart));
        }
        return mav;
    }
}
