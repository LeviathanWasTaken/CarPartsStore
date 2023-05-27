package leviathan.CarPartsStore.controllers.apis;

import leviathan.CarPartsStore.domain.UserDTO;
import leviathan.CarPartsStore.services.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserAPIController {

    private final AuthorizationService authorizationService;

    public UserAPIController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public ResponseEntity<UserDTO> sendUserInfo(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        if (authorizationService.isUserAuthenticated(oAuth2AuthenticationToken)) {
            UserDTO user = authorizationService.authorize(oAuth2AuthenticationToken);
            return ResponseEntity.ok().body(user);
        }
        else return ResponseEntity.ok(new UserDTO());
    }

    @GetMapping("/api/user/is-logged-in")
    public ResponseEntity<Boolean> isUserLoggedIn(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return ResponseEntity.ok().body(oAuth2AuthenticationToken != null);
    }
}
