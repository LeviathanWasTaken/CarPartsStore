package leviathan.CarPartsStore.services;

import leviathan.CarPartsStore.domain.UserDTO;
import leviathan.CarPartsStore.entity.Roles;
import leviathan.CarPartsStore.entity.User;
import leviathan.CarPartsStore.repos.UserRepo;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class AuthorizationService {

    private final UserService userService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final UserRepo userRepo;

    public AuthorizationService(UserService userService, ClientRegistrationRepository clientRegistrationRepository, UserRepo userRepo) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.userService = userService;
        this.userRepo = userRepo;
    }

    public UserDTO authorize(OAuth2AuthenticationToken oAuth2AuthenticationToken) throws ClientAuthorizationException {
        String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
        if (clientRegistration != null) {
            String provider = clientRegistration.getProviderDetails().getAuthorizationUri();
            if (provider.contains("google")) {
                return null;
            } else if (provider.contains("github")) {
                return authorizeViaGithub(oAuth2AuthenticationToken.getPrincipal());
            }
        } else {
            throw new ClientAuthorizationException(new OAuth2Error("Invalid client registration"), registrationId);
        }
        return null;
    }

    public boolean isUserAuthenticated(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return oAuth2AuthenticationToken != null;
    }

    public UserDTO authorizeViaGithub(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        UserDTO user = userService.getUserByGithubId((int) attributes.get("id"));
        if (user == null) {
            user = userService.createNewUserByGithubAttributes(attributes);
        }
        return user;
    }

    public boolean isUserAdmin(UUID userUUID) {
        User user = userRepo.findById(userUUID).orElseThrow(
                () -> new IllegalArgumentException("There is no user with UUID: " + userUUID)
        );
        return user.getRoles().contains(Roles.ADMIN);
    }
}
