package disco.repo.impl;

import org.jooq.DSLContext;

import disco.domain.server.Server;
import disco.domain.server.ServerId;
import disco.domain.user.UserId;
import disco.repo.ServerRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static disco.jooq.tables.Servers.SERVERS;
import static disco.jooq.tables.ServerMembers.SERVER_MEMBERS;

public final class ServerRepositoryImpl implements ServerRepository {
    private final DSLContext ctx;

    public ServerRepositoryImpl(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Server save(Server server) {
        ctx.insertInto(SERVERS)
           .set(SERVERS.ID, server.id().value())
           .set(SERVERS.NAME, server.name())
           .set(SERVERS.CREATED_AT, OffsetDateTime.ofInstant(server.createdAt(), ZoneOffset.UTC))
           .onConflict(SERVERS.ID)
           .doUpdate()
           .set(SERVERS.NAME, server.name())
           .execute();
        return server;
    }

    @Override
    public Optional<Server> findById(ServerId id) {
        return ctx.selectFrom(SERVERS)
                  .where(SERVERS.ID.eq(id.value()))
                  .fetchOptional(this::map);
    }

    @Override
    public List<Server> listByUserId(UserId userId) {
        return ctx.select(SERVERS.fields())
                  .from(SERVERS)
                  .join(SERVER_MEMBERS)
                  .on(SERVERS.ID.eq(SERVER_MEMBERS.SERVER_ID))
                  .where(SERVER_MEMBERS.USER_ID.eq(userId.value()))
                  .fetch(this::map);
    }

    private Server map(org.jooq.Record r) {
        return new Server(
            new ServerId(r.get(SERVERS.ID)),
            r.get(SERVERS.NAME),
            r.get(SERVERS.CREATED_AT).toInstant()
        );
    }
    
}
