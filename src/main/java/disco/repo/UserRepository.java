package disco.repo;

import java.util.Optional;

import disco.domain.user.User;
import disco.domain.user.UserId;

public interface UserRepository {
    User save(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findById(UserId id);
}
