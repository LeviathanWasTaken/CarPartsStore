package leviathan.CarPartsStore.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Data
// TODO: 01.05.2023 Нельзя в Entity использовать бизнес логику и имплементировать UserDetails,
//  надо ссделать отдельный класс и мапить данные на него
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private int githubId;
    private String avatar;
    private String login;
    private String email;
    private String deliveryAddress;
    private String name;
    @OneToMany(mappedBy = "author")
    private List<Review> reviews;
    @OneToOne
    private Cart cart;
    @ElementCollection(targetClass = Roles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_uuid"))
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles;

    public User(int githubId, String avatar, String login, Cart cart, Set<Roles> roles) {
        this.githubId = githubId;
        this.avatar = avatar;
        this.login = login;
        this.cart = cart;
        this.roles = roles;
        name = login;
        deliveryAddress = "";
        email = "";
    }

    public User() {
    }

    // TODO: 01.05.2023 Убрать в другой класс
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
