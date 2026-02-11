package disco.repo;

import java.util.List;
import java.util.Optional;

import disco.domain.server.Server;
import disco.domain.server.ServerId;
import disco.domain.user.UserId;

public interface ServerRepository {
    Server save(Server server);
    Optional<Server> findById(ServerId id);
    List<Server> listByUserId(UserId userId);
}
