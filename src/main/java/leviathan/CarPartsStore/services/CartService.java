package leviathan.CarPartsStore.services;

import jakarta.transaction.Transactional;
import leviathan.CarPartsStore.domain.CartItemDTO;
import leviathan.CarPartsStore.domain.RemovalStatus;
import leviathan.CarPartsStore.domain.UserDTO;
import leviathan.CarPartsStore.entity.Cart;
import leviathan.CarPartsStore.entity.CartItem;
import leviathan.CarPartsStore.entity.Product;
import leviathan.CarPartsStore.entity.User;
import leviathan.CarPartsStore.repos.CartItemRepo;
import leviathan.CarPartsStore.repos.CartRepo;
import leviathan.CarPartsStore.repos.ProductRepo;
import leviathan.CarPartsStore.repos.UserRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CartService {
    private final CartRepo cartRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final CartItemRepo cartItemRepo;

    public CartService(CartRepo cartRepo, ProductRepo productRepo, UserRepo userRepo, CartItemRepo cartItemRepo) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.cartItemRepo = cartItemRepo;
    }
    /*
    private final UserService userService;
    private final CartItemRepo cartItemRepo;
    private final CartRepo cartRepo;

    public CartService(UserService userService, CartItemRepo cartItemRepo, CartRepo cartRepo) {
        this.userService = userService;
        this.cartItemRepo = cartItemRepo;
        this.cartRepo = cartRepo;
    }

    public int getAmountOfItemsInCart(UUID userUUID) {
        return userService.getByUUID(userUUID).map(user -> getAmountOfActiveItemsInCart(user.getCart())).orElse(0);
    }

    public int getTotalPriceOfItemsInCart(UUID userUUID) {
        return userService.getByUUID(userUUID).map(user -> getTotalPriceOfActiveItemsInCart(user.getCart())).orElse(0);
    }

    public void addItemInCart(User user, Product product, int quantity) {
        Cart cart = user.getCart();
        List<CartItem> cartItems = cart.getCartItems();
        boolean alreadyInCart = false;
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().getProductUUID().equals(product.getProductUUID())) {
                alreadyInCart = true;
                cartItem.setQuantity(cartItem.getQuantity()+quantity);
                cartItem.setTotalPrice(cartItem.getTotalPrice()+product.getProductInfo().getPriceInPennies()*quantity);
                cartItemRepo.save(cartItem);
                break;
            }
        }
        if (!alreadyInCart) {
            CartItem cartItem = new CartItem(cart, quantity, product);
            cartItemRepo.save(cartItem);
        }
        cartRepo.save(cart);
    }

    public void removeCartItem(UUID cartItemUUID) {
        cartItemRepo.deleteById(cartItemUUID);
    }

    public void setNewQuantity(UUID cartItemUUID, int quantity) {
        CartItem cartItem = cartItemRepo.findById(cartItemUUID).orElseThrow(
                () -> new IllegalArgumentException("Incorrect cart item UUID " + cartItemUUID)
        );
        long diff = cartItem.getTotalPrice() - (cartItem.getTotalPrice()/cartItem.getQuantity())*quantity;
        cartItem.setTotalPrice(cartItem.getTotalPrice() - diff);
        cartItem.setQuantity(quantity);
        cartItemRepo.save(cartItem);
    }

    public void recalculateTotalPricesForProduct(Product product) {
        Iterable<CartItem> cartItems = cartItemRepo.findAllByProduct(product);
        for (CartItem cartItem : cartItems) {
            cartItem.setTotalPrice(cartItem.getProduct().getProductInfo().getPriceInPennies()*cartItem.getQuantity());
            cartItemRepo.save(cartItem);
        }
    }

    public int getAmountOfActiveItemsInCart(Cart cart) {
        List<CartItem> cartItems = cart.getCartItems();
        int result = 0;
        for (CartItem cartItem : cartItems) {
            if(cartItem.getProduct().getStatus().equals(Status.ACTIVE)) {
                result += cartItem.getQuantity();
            }
        }
        return result;
    }

    public int getTotalPriceOfActiveItemsInCart(Cart cart) {
        List<CartItem> cartItems = cart.getCartItems();
        int result = 0;
        for (CartItem cartItem : cartItems) {
            if(cartItem.getProduct().getStatus().equals(Status.ACTIVE)) {
                result += cartItem.getTotalPrice();
            }
        }
        return result;
    }


    public List<CartItem> getRemovedCartItems(Cart cart) {
        return cart.getCartItems().stream().filter(
                cartItem ->
                        cartItem.getProduct().getStatus().equals(Status.REMOVED) ||
                                cartItem.getProduct().getStatus().equals(Status.CATALOG_REMOVED)
        ).collect(Collectors.toList());
    }

    public List<CartItem> getActiveCartItems(Cart cart) {
        return cart.getCartItems().stream().filter(
                cartItem -> cartItem.getProduct().getStatus().equals(Status.ACTIVE)
        ).collect(Collectors.toList());
    }

    public boolean isProductInCart(Cart cart, Product product) {
        return cart.getCartItems().stream().anyMatch(cartItem -> cartItem.getProduct().equals(product));
    }

 */

    public Cart getCartFromDBByUUID(UUID cartUUID) {
        return cartRepo.findById(cartUUID).orElseThrow(
                () -> new IllegalArgumentException("There is no cart with cartUUID: " + cartUUID)
        );
    }

    public List<CartItemDTO> getCartItems(UserDTO user) {
        User userFromDB = userRepo.findById(user.getUserUUID()).orElseThrow(
                () -> new IllegalArgumentException("There is no user with UUID: " + user.getUserUUID())
        );
        List<CartItemDTO> cartItems = new ArrayList<>();
        List<CartItem> cartItemsFromDB = userFromDB.getCart().getCartItems();
        for (CartItem cartItemFromDB : cartItemsFromDB) {
            CartItemDTO cartItem = new CartItemDTO();
            cartItem.setCartItemUUID(cartItemFromDB.getCartItemUUID());
            cartItem.setCartItemQuantity(cartItemFromDB.getQuantity());
            cartItem.setCartItemTotalPriceInPennies(cartItemFromDB.getQuantity()*cartItemFromDB.getProduct().getPriceInPennies());
            cartItem.setCartItemProductName(cartItemFromDB.getProduct().getProductName());
            cartItem.setCartItemProductUUID(cartItemFromDB.getProduct().getProductUUID());
            cartItem.setCartItemProductPreviewPicture(cartItemFromDB.getProduct().getProductPictures().get(0));
            cartItem.setCartItemProductPriceInPennies(cartItemFromDB.getProduct().getPriceInPennies());
            cartItems.add(cartItem);
        }
        return cartItems;
    }


    public int calculateTotalPriceOfActiveElementsInCart(UUID cartUUID) {
        Cart cart = getCartFromDBByUUID(cartUUID);
        int totalPrice = 0;
        for (CartItem cartItem : cart.getCartItems().stream().filter(
              cartItem -> cartItem.getProduct().getRemovalStatus().equals(RemovalStatus.ACTIVE)).toList()) {
            totalPrice += cartItem.getProduct().getPriceInPennies() * cartItem.getQuantity();
        }
        return totalPrice;
    }

    public int calculateTotalAmountOfActiveElementsInCart(UUID cartUUID) {
        Cart cart = getCartFromDBByUUID(cartUUID);
        int totalAmount = 0;
        for (CartItem cartItem : cart.getCartItems().stream().filter(
              cartItem -> cartItem.getProduct().getRemovalStatus().equals(RemovalStatus.ACTIVE)).toList()) {
            totalAmount += cartItem.getQuantity();
        }
        return totalAmount;
    }

    @Transactional
    public void addProductToCart(UserDTO user, UUID productUUID) throws IllegalArgumentException {
        Product productFromDB = productRepo.findById(productUUID).orElseThrow(
                () -> new IllegalArgumentException("There is no product with UUID: " + productUUID)
        );
        User userFromDB = userRepo.findById(user.getUserUUID()).orElseThrow(
                () -> new IllegalArgumentException("There is no user with UUID: " + user.getUserUUID())
        );
        Cart cart = userFromDB.getCart();
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(productFromDB);
        newCartItem.setQuantity(1);
        newCartItem.setCart(cart);
        cartItemRepo.save(newCartItem);
        cartRepo.save(cart);
    }

    public boolean hasUserCartCartItem(UserDTO user, UUID cartItemUUID) throws IllegalArgumentException {
        User userFromDB = userRepo.findById(user.getUserUUID()).orElseThrow(
                () -> new IllegalArgumentException("There is no user with UUID: " + user.getUserUUID())
        );
        Cart userCart = userFromDB.getCart();
        for (CartItem cartItem : userCart.getCartItems()) {
            if (cartItem.getCartItemUUID().equals(cartItemUUID)) return true;
        }
        return false;
    }

    public void increaseCartItemQuantity(UserDTO user, UUID cartItemUUID) throws IllegalArgumentException {
        User userFromDB = userRepo.findById(user.getUserUUID()).orElseThrow(
                () -> new IllegalArgumentException("There is no user with UUID: " + user.getUserUUID())
        );
        Cart userCart = userFromDB.getCart();
        CartItem cartItem = userCart.getCartItems().stream().filter(item -> item.getCartItemUUID().equals(cartItemUUID)).findFirst().orElseThrow(
                () -> new IllegalArgumentException("There is no cart item with UUID: " + cartItemUUID)
        );
        cartItem.setQuantity(cartItem.getQuantity()+1);
        cartItemRepo.save(cartItem);
    }

    public void decreaseCartItemQuantity(UserDTO user, UUID cartItemUUID) throws IllegalArgumentException {
        User userFromDB = userRepo.findById(user.getUserUUID()).orElseThrow(
                () -> new IllegalArgumentException("There is no user with UUID: " + user.getUserUUID())
        );
        Cart userCart = userFromDB.getCart();
        CartItem cartItem = userCart.getCartItems().stream().filter(item -> item.getCartItemUUID().equals(cartItemUUID)).findFirst().orElseThrow(
                () -> new IllegalArgumentException("There is no cart item with UUID: " + cartItemUUID)
        );
        if (cartItem.getQuantity() == 1) {
            removeCartItem(user, cartItemUUID);
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity()-1);
            cartItemRepo.save(cartItem);
        }
    }

    public void removeCartItem(UserDTO user, UUID cartItemUUID) throws IllegalArgumentException {
        User userFromDB = userRepo.findById(user.getUserUUID()).orElseThrow(
                () -> new IllegalArgumentException("There is no user with UUID: " + user.getUserUUID())
        );
        Cart userCart = userFromDB.getCart();
        CartItem cartItem = userCart.getCartItems().stream().filter(item -> item.getCartItemUUID().equals(cartItemUUID)).findFirst().orElseThrow(
                () -> new IllegalArgumentException("There is no cart item with UUID: " + cartItemUUID)
        );
        cartItemRepo.delete(cartItem);
    }
}
