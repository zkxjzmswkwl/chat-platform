package disco;

import com.zaxxer.hikari.HikariDataSource;
import disco.api.rest.RestRouter;
import disco.gateway.ConnectionRegistry;
import disco.gateway.GatewayHandler;
import disco.repo.impl.MessageRepositoryImpl;
import disco.repo.impl.ServerMemberRepositoryImpl;
import disco.repo.impl.UserRepositoryImpl;
import disco.service.AuthorizationService;
import disco.service.MessageService;
import disco.service.UserService;
import io.undertow.Handlers;
import io.undertow.Undertow;
import org.jooq.impl.DSL;

public final class App {

    public static void main(String[] args) {
        var ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://localhost:5432/disco-chat");
        ds.setUsername("postgres");
        ds.setPassword("example");
        var ctx = DSL.using(ds, org.jooq.SQLDialect.POSTGRES);

        // repos
        var userRepo = new UserRepositoryImpl(ctx);
        var serverMemberRepo = new ServerMemberRepositoryImpl(ctx);
        var messageRepo = new MessageRepositoryImpl(ctx);

        // services
        var userService = new UserService(userRepo);
        var authorizationService = new AuthorizationService(serverMemberRepo);
        var messageService = new MessageService(messageRepo);

        var registry = new ConnectionRegistry();
        var gateway = new GatewayHandler(
            authorizationService,
            messageService,
            registry
        );

        var rest = new RestRouter(userService, authorizationService, messageService);
        var server = Undertow.builder()
            .addHttpListener(9092, "0.0.0.0")
            .setHandler(
                Handlers.path()
                    .addPrefixPath("/api", rest.handler())
                    .addPrefixPath("/gateway", gateway.websocket())
            )
            .build();

        server.start();

        System.out.print(":9092\n");
    }
}