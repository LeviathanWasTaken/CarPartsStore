package leviathan.CarPartsStore.controllers;

import org.springframework.stereotype.Controller;

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
