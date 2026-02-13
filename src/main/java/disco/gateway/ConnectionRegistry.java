package disco.gateway;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import disco.domain.user.UserId;
import io.undertow.websockets.core.WebSocketChannel;

public final class ConnectionRegistry {

    private final Map<UserId, WebSocketChannel> users = new ConcurrentHashMap<>();

    public void add(UserId id, WebSocketChannel ch) {
        users.put(id, ch);
    }

    public void remove(UserId id) {
        users.remove(id);
    }

    public Optional<WebSocketChannel> get(UserId id) {
        return Optional.ofNullable(users.get(id));
    }

    public Collection<WebSocketChannel> all() {
        return users.values();
    }
}
