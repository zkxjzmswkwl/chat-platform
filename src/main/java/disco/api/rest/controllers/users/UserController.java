package disco.api.rest.controllers.users;

import disco.api.Json;
import disco.api.HaytchTeeTeePee;
import disco.api.dto.UserResponse;
import disco.api.dto.RegisterRequest;
import disco.domain.user.User;
import disco.domain.user.UserId;
import disco.service.UserService;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.Optional;
import java.util.UUID;

public final class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void register(HttpServerExchange exchange) {
        if (!exchange.getRequestMethod().equalToString("POST")) {
            exchange.setStatusCode(405);
            exchange.endExchange();
            return;
        }

        exchange.getRequestReceiver().receiveFullBytes((ex, data) -> {
            try {
                RegisterRequest req = Json.MAPPER.readValue(data, RegisterRequest.class);

                User user = userService.register(
                        req.username(),
                        req.password());

                UserResponse resp = new UserResponse(
                        user.id().value(),
                        user.username(),
                        user.createdAt());

                byte[] out = Json.MAPPER.writeValueAsBytes(resp);
                ex.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                ex.setStatusCode(201);
                ex.getResponseSender().send(new String(out, StandardCharsets.UTF_8));
            } catch (Exception e) {
                ex.setStatusCode(400);
                ex.getResponseSender().send("bad request");
            }
        });
    }

    public void get(HttpServerExchange exchange) {
        if (!exchange.getRequestMethod().equalToString("GET")) {
            exchange.setStatusCode(405);
            exchange.endExchange();
            return;
        }

        Deque<String> ids = exchange.getQueryParameters().get("id");

        if (ids == null || ids.isEmpty()) {
            exchange.setStatusCode(400);
            exchange.endExchange();
            return;
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(ids.getFirst());
        } catch (IllegalArgumentException e) {
            exchange.setStatusCode(400);
            exchange.endExchange();
            return;
        }

        Optional<User> user = userService.findById(new UserId(uuid));

        if (user.isEmpty()) {
            exchange.setStatusCode(404);
            exchange.endExchange();
            return;
        }

        User u = user.get();

        UserResponse resp = new UserResponse(
            u.id().value(),
            u.username(),
            u.createdAt()
        );

        HaytchTeeTeePee.write(exchange, 200, resp);
    }
}
