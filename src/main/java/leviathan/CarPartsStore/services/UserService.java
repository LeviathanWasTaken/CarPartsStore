package leviathan.CarPartsStore.services;

import jakarta.transaction.Transactional;
import leviathan.CarPartsStore.domain.CartDTO;
import leviathan.CarPartsStore.domain.UserDTO;
import leviathan.CarPartsStore.entity.Cart;
import leviathan.CarPartsStore.entity.Roles;
import leviathan.CarPartsStore.entity.User;
import leviathan.CarPartsStore.repos.CartItemRepo;
import leviathan.CarPartsStore.repos.CartRepo;
import leviathan.CarPartsStore.repos.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductsService productsService;
    private final CartRepo cartRepo;
    private final CartService cartService;

    public UserService(CartService cartService,
                       UserRepo userRepo,
                       CartItemRepo cartItemRepo,
                       ProductsService productsService,
                       CartRepo cartRepo) {
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

    @Transactional
    public UserDTO createNewUserByGithubAttributes(Map<String, Object> attributes) {
        Cart cart = new Cart();
        cartRepo.save(cart);
        User user = new User((int) attributes.get("id"),
                             (String) attributes.get("avatar_url"),
                             (String) attributes.get("login"),
                             cart,
                             Collections.singleton(Roles.USER)
        );
        userRepo.save(user);
        user = userRepo.findByGithubId((int) attributes.get("id")).orElseThrow(
                () -> new RuntimeException("Unable to create new user")
        );
        return new UserDTO(
                user.getUuid(),
                user.getName(),
                user.getLogin(),
                user.getAvatar(),
                user.getEmail(),
                user.getDeliveryAddress(),
                user.getRoles()
        );
    }

    public Iterable<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void save(User user) {
        userRepo.save(user);
    }

    public UserDTO getUserByGithubId(int id) {
        User user = userRepo.findByGithubId(id).orElse(null);
        return
                user==null ? null : new UserDTO(
                    user.getUuid(),
                    user.getName(),
                    user.getLogin(),
                    user.getAvatar(),
                    user.getEmail(),
                    user.getDeliveryAddress(),
                    user.getRoles()
                );
    }

    public Optional<User> getByUUID(UUID userUUID) {
        return userRepo.findById(userUUID);
    }

    public CartDTO getUserCartByUserUUID(UUID userUUID) {
        User user = userRepo.findById(userUUID).orElseThrow(
                () -> new IllegalArgumentException("There is no user with UUID: " + userUUID)
        );
        CartDTO cart = new CartDTO();
        cart.setCartUUID(user.getCart().getCartUUID());
        cart.setTotalQuantityOfItemsInCart(cartService.calculateTotalPriceOfActiveElementsInCart(user.getCart().getCartUUID()));
        cart.setTotalQuantityOfItemsInCart(cartService.calculateTotalAmountOfActiveElementsInCart(user.getCart().getCartUUID()));
        return cart;
    }

}
