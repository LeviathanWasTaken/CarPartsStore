package leviathan.CarPartsStore.services;

import java.util.Map;
import leviathan.CarPartsStore.entity.User;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final UserService userService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public AuthorizationService(UserService userService, ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.userService = userService;
    }

    public User authorize(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
        if (clientRegistration != null) {
            String provider = clientRegistration.getProviderDetails().getAuthorizationUri();
            if (provider.contains("google")) {
                return null;
            } else if (provider.contains("github")) {
                return authorizeViaGithub(oAuth2AuthenticationToken.getPrincipal().getAttributes());
            }
        } else {
            throw new ClientAuthorizationException(new OAuth2Error("Invalid"), registrationId);
        }
        return null;
    }

    public boolean isUserAuthorized(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return oAuth2AuthenticationToken != null;
    }

    public User authorizeViaGithub(Map<String, Object> attributes) {
        return userService.getUserByGithubId((int) attributes.get("id"))
              .orElseGet(() -> userService.createNewUserByGithubAttributes(attributes));
    }
}
