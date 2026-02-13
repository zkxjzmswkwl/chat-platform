package disco.api.rest;

import disco.api.rest.controllers.messages.MessageController;
import disco.api.rest.controllers.users.UserController;
import disco.service.AuthorizationService;
import disco.service.MessageService;
import disco.service.UserService;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Methods;

public final class RestRouter {

    private final RoutingHandler routes = new RoutingHandler();

    public RestRouter(
        UserService userService,
        AuthorizationService auth,
        MessageService messageService
    ) {

        // user shit
        UserController users = new UserController(userService);
        routes.add(Methods.POST, "/api/users", users::register);
        routes.add(Methods.GET, "/api/users", users::get);

        // message shit
        MessageController messages = new MessageController(messageService, auth);
        routes.add(Methods.POST, "/channels/{id}/messages", messages::create);
        routes.add(Methods.GET, "/channels/{id}/messages", messages::list);
    }

    public HttpHandler handler() {
        return routes;
    }
}