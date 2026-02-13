package disco.repo.impl;

import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;

import disco.domain.server.ServerId;
import disco.domain.server.ServerMember;
import disco.domain.user.UserId;
import disco.repo.ServerMemberRepository;

import static disco.jooq.tables.ServerMembers.SERVER_MEMBERS;

public class ServerMemberRepositoryImpl implements ServerMemberRepository {
    private final DSLContext ctx;

    public ServerMemberRepositoryImpl(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public ServerMember save(ServerMember member) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public Optional<ServerMember> find(ServerId serverId, UserId userId) {
        return ctx.selectFrom(SERVER_MEMBERS)
                .where(SERVER_MEMBERS.SERVER_ID.eq(serverId.value()))
                .and(SERVER_MEMBERS.USER_ID.eq(userId.value()))
                .fetchOptional(this::map);
    }

    @Override
    public List<ServerMember> listMembers(ServerId serverId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listMembers'");
    }

    @Override
    public void remove(ServerId serverId, UserId userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    private ServerMember map(org.jooq.Record r) {
        return new ServerMember(
            new ServerId(r.get(SERVER_MEMBERS.SERVER_ID)),
            new UserId(r.get(SERVER_MEMBERS.USER_ID)),
            r.get(SERVER_MEMBERS.JOINED_AT).toInstant(),
            r.get(SERVER_MEMBERS.ROLES)
        );
    }
    
}
