package leviathan.CarPartsStore.controllers;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "redirect:/oauth2/authorization/github";
    }

    @GetMapping("/oauth2/authorization/github")
    public String authorizeWithGithub(OAuth2AuthorizedClient authorizedClient) {
        return "redirect:" + authorizedClient.getClientRegistration().getProviderDetails()
              .getAuthorizationUri() + "?client_id=" + authorizedClient.getClientRegistration().getClientId()
              + "&redirect_uri=" + authorizedClient.getClientRegistration().getRedirectUri()
              + "&state=" + authorizedClient.getAccessToken().getTokenValue();
    }

}
