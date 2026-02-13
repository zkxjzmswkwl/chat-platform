package disco.gateway;

import java.util.List;

import disco.auth.AuthTokens;
import disco.domain.user.UserId;
import disco.service.AuthorizationService;
import disco.service.MessageService;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.spi.WebSocketHttpExchange;

public final class GatewayHandler {

    private final AuthorizationService authService;
    private final MessageService messageService;
    private final ConnectionRegistry registry;

    public GatewayHandler(
        AuthorizationService authz,
        MessageService messages,
        ConnectionRegistry registry
    ) {
        this.authService = authz;
        this.messageService = messages;
        this.registry = registry;
    }

    public HttpHandler websocket() {
        return Handlers.websocket(this::onConnect);
    }

    private void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
        try {
            List<String> tokens = exchange.getRequestParameters().get("token");

            String token = (tokens == null || tokens.isEmpty())
                ? null
                : tokens.get(0);

            if (token == null || tokens.isEmpty()) {
                channel.sendClose();
                return;
            }

            UserId userId;
            try {
                userId = AuthTokens.verify(token);
            } catch (Exception e) {
                channel.sendClose();
                return;
            }

            registry.add(userId, channel);

            channel.getCloseSetter().set(c -> registry.remove(userId));

            channel.getReceiveSetter().set(new AbstractReceiveListener() {
                @Override
                protected void onFullTextMessage(
                    WebSocketChannel ch,
                    BufferedTextMessage msg
                ) {
                    handleMessage(userId, ch, msg.getData());
                }
            });

            channel.resumeReceives();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(
        UserId userId,
        WebSocketChannel ch,
        String raw
    ) {
        // parse
        {

        }

        // check auth 
        {

        }

        // send out
        {

        }
    }
}
