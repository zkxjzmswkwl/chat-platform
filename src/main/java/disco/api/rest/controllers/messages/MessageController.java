package disco.api.rest.controllers.messages;

import disco.api.HaytchTeeTeePee;
import disco.api.dto.CreateMessageRequest;
import disco.api.dto.MessageResponse;
import disco.auth.AuthException;
import disco.auth.AuthTokens;
import disco.domain.server.ServerId;
import disco.domain.user.UserId;
import disco.service.AuthorizationService;
import disco.service.MessageService;
import io.undertow.server.HttpServerExchange;

import java.util.Deque;
import java.util.UUID;

public final class MessageController {
    private final MessageService messages;
    private final AuthorizationService auth;

    public MessageController(MessageService messages, AuthorizationService auth) {
        this.messages = messages;
        this.auth = auth;
    }

    // post channels/{id}/messages
    public void create(HttpServerExchange exchange) {
        if (!exchange.getRequestMethod().equalToString("POST")) {
            HaytchTeeTeePee.empty(exchange, 405);
            return;
        }

        String channelIdStr = exchange.getQueryParameters().get("id").getFirst();
        UUID channelId = UUID.fromString(channelIdStr);
        UserId caller = requireAuth(exchange);

        // user is member?
        auth.requireMember(new ServerId(resolveServerId(channelId)), caller);
        HaytchTeeTeePee.read(exchange,
                CreateMessageRequest.class,
                req -> {

                    var msg = messages.createMessage(
                            channelId,
                            caller.value(),
                            req.content());

                    HaytchTeeTeePee.write(
                            exchange,
                            201,
                            MessageResponse.from(msg));
                });
    }

    // get channels/{id}/messages
    public void list(HttpServerExchange exchange) {
        if (!exchange.getRequestMethod().equalToString("GET")) {
            HaytchTeeTeePee.empty(exchange, 405);
            return;
        }

        String channelIdStr = exchange.getQueryParameters().get("id").getFirst();
        UUID channelId = UUID.fromString(channelIdStr);
        UserId caller = requireAuth(exchange);

        auth.requireMember(
                new ServerId(resolveServerId(channelId)),
                caller);

        int limit = 50;
        Deque<String> limits = exchange.getQueryParameters().get("limit");

        if (limits != null && !limits.isEmpty()) {
            limit = Integer.parseInt(limits.getFirst());
        }

        var result = messages.recent(channelId, limit);
        HaytchTeeTeePee.write(
                exchange,
                200,
                result.stream()
                        .map(MessageResponse::from)
                        .toList()
        );
    }

    private UserId requireAuth(HttpServerExchange exchange) {
        String auth = exchange.getRequestHeaders().getFirst("Authorization");

        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new RuntimeException("Unauthorized");
        }

        try {
            return AuthTokens.verify(auth.substring(7));
        } catch (AuthException e) {
            e.printStackTrace();
            return null;
        }
    }

    private UUID resolveServerId(UUID channelId) {
        // TODO: get channel to server id from db
        // this shit resolves nada right now
        return channelId;
    }
}