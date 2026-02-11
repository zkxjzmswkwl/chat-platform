package disco.repo;

import disco.domain.server.ServerId;
import disco.domain.server.ServerMember;
import disco.domain.user.UserId;

import java.util.List;
import java.util.Optional;

public interface ServerMemberRepository {
    ServerMember save(ServerMember member);
    Optional<ServerMember> find(ServerId serverId, UserId userId);
    List<ServerMember> listMembers(ServerId serverId);
    void remove(ServerId serverId, UserId userId);
}