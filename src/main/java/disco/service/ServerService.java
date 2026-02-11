package disco.service;

import disco.domain.server.*;
import disco.domain.user.UserId;
import disco.repo.ServerMemberRepository;
import disco.repo.ServerRepository;
import disco.util.Ids;

import java.time.Instant;
import java.util.EnumSet;

public final class ServerService {
    private final ServerRepository servers;
    private final ServerMemberRepository members;

    public ServerService(
            ServerRepository servers,
            ServerMemberRepository members
    ) {
        this.servers = servers;
        this.members = members;
    }

    public Server createServer(UserId suId, String name) {
        var server = new Server(
                new ServerId(Ids.newId()),
                name,
                Instant.now()
        );
        servers.save(server);

        var suMembership = new ServerMember(
                server.id(),
                suId,
                Instant.now(),
                EnumSet.of(ServerRole.SUPER_USER)
        );
        members.save(suMembership);

        return server;
    }
}