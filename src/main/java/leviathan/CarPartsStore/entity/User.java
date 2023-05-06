package leviathan.CarPartsStore.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

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
}
