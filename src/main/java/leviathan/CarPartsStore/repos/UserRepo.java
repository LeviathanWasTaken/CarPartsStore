package leviathan.CarPartsStore.repos;

import java.util.Optional;
import java.util.UUID;
import leviathan.CarPartsStore.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends CrudRepository<User, UUID> {

    Optional<User> findByGithubId(@Param("githubId") int githubId);
}
