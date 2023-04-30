package leviathan.CarPartsStore.services;

import leviathan.CarPartsStore.domain.*;
import leviathan.CarPartsStore.repos.CartItemRepo;
import leviathan.CarPartsStore.repos.CartRepo;
import leviathan.CarPartsStore.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private CartRepo cartRepo;

    public User getUser(OAuth2AuthenticationToken auth2AuthenticationToken) {
        return authorizationService.authorize(auth2AuthenticationToken);
    }

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
}
