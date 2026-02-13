package disco.service;

import java.time.Instant;
import java.util.Optional;

import disco.domain.user.User;
import disco.domain.user.UserId;
import disco.repo.UserRepository;
import disco.util.Ids;

public final class UserService {
    private final UserRepository users;

    public UserService(UserRepository repo) {
        this.users = repo;
    }

    public User register(String username, String passwordHash) {
        var user = new User(
            new UserId(Ids.newId()),
            username,
            passwordHash,
            Instant.now()
        );
        return users.save(user);
    }

    public Optional<User> findById(UserId id) {
        return users.findById(id);
    }

}
