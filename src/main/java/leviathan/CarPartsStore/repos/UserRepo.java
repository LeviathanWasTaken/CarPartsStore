package leviathan.CarPartsStore.repos;

import leviathan.CarPartsStore.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends CrudRepository<User, UUID> {
    Optional<User> findByGithubId(@Param("githubId") int githubId);
}
