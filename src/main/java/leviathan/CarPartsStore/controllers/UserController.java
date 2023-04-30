package leviathan.CarPartsStore.controllers;

import leviathan.CarPartsStore.domain.User;
import leviathan.CarPartsStore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class UserController {
    /*
    @Autowired
    private UserService userService;
    @GetMapping("/profile")
    public String profile(OAuth2AuthenticationToken auth2AuthenticationToken, Map<String, Object> model) {
        User user = userService.getUser(auth2AuthenticationToken);
        model.put("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String editProfile(OAuth2AuthenticationToken auth2AuthenticationToken,
                              @RequestParam String name,
                              @RequestParam String email,
                              @RequestParam String deliveryAddress) {
        User user = userService.getUser(auth2AuthenticationToken);
        user.setName(name);
        user.setEmail(email);
        user.setDeliveryAddress(deliveryAddress);
        userService.save(user);
        return "redirect:/profile";
    }

     */
}
