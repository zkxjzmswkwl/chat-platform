package disco;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import org.jooq.impl.DSL;

import com.zaxxer.hikari.HikariDataSource;

import com.sun.net.httpserver.HttpServer;

import disco.api.Json;
import disco.api.dto.CreateUserRequest;
import disco.api.dto.UserResponse;
import disco.repo.impl.UserRepositoryImpl;
import disco.service.UserService;

public class App  {
    public static void main( String[] args ) throws Exception {
        var ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://localhost:5432/disco-chat");
        ds.setUsername("postgres");
        ds.setPassword("example");

        var ctx = DSL.using(ds, org.jooq.SQLDialect.POSTGRES);
        var userService = new UserService(new UserRepositoryImpl(ctx));

        HttpServer server = HttpServer.create(new InetSocketAddress(9092), 0);
        server.createContext("/api/users", exchange -> {
            try {
                if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                    exchange.sendResponseHeaders(405, -1);
                    exchange.close();
                    return;
                }

                byte[] bodyBytes = exchange.getRequestBody().readAllBytes();
                if (bodyBytes.length == 0) {
                    exchange.sendResponseHeaders(400, -1);
                    exchange.close();
                    return;
                }

                CreateUserRequest req =
                    Json.MAPPER.readValue(bodyBytes, CreateUserRequest.class);

                if (req.username() == null || req.username().isBlank()) {
                    exchange.sendResponseHeaders(400, -1);
                    exchange.close();
                    return;
                }

                var user = userService.createUser(req.username());
                var respObj = new UserResponse(
                    user.id().value(),
                    user.username(),
                    user.createdAt()
                );
                byte[] resp = Json.MAPPER.writeValueAsBytes(respObj);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(201, resp.length);
                exchange.getResponseBody().write(resp);
                exchange.close();

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    exchange.sendResponseHeaders(500, -1);
                } catch (Exception ignored) {}
                exchange.close();
            }
        });
        server.createContext("/health", exchange -> {
            byte[] resp = "ok".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, resp.length);
            exchange.getResponseBody().write(resp);
            exchange.close();
        });
        server.start();
    }
}
