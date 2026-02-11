package disco.service;

import disco.domain.user.User;
import disco.repo.UserRepository;

public final class UserService {
    private final UserRepository users;

    public UserService(UserRepository repo) {
        this.users = repo;
    }

    public User createUser(String username) {
        var user = new User(username);
        return users.save(user);
    }
}
