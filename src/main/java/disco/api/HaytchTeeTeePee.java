package disco.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public final class HaytchTeeTeePee {
    private static final ObjectMapper MAPPER = Json.MAPPER;

    public static void empty(HttpServerExchange ex, int status) {
        ex.setStatusCode(status);
        ex.endExchange();
    }

    public static <T> void read(HttpServerExchange exchange, Class<T> type, Consumer<T> handler) {
        exchange.getRequestReceiver().receiveFullBytes((ex, data) -> {
            try {
                T obj = MAPPER.readValue(data, type);
                handler.accept(obj);
            } catch (Exception e) {
                ex.setStatusCode(400);
                ex.endExchange();
            }
        });
    }

    public static void write(HttpServerExchange exchange, int status, Object body) {
        try {
            byte[] out = MAPPER.writeValueAsBytes(body);
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exchange.setStatusCode(status);
            exchange.getResponseSender().send(new String(out, StandardCharsets.UTF_8));
        } catch (Exception e) {
            exchange.setStatusCode(500);
            exchange.endExchange();
        }
    }
}