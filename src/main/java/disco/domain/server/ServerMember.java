package disco.domain.server;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

import disco.domain.user.UserId;

public final class ServerMember {
    private final ServerId serverId;
    private final UserId userId;
    private final Instant joinedAt;
    private final EnumSet<ServerRole> roles;

    public ServerMember(
            ServerId serverId,
            UserId userId,
            Instant joinedAt,
            Set<ServerRole> roles
    ) {
        this.serverId = serverId;
        this.userId = userId;
        this.joinedAt = joinedAt;
        this.roles = EnumSet.copyOf(roles);
    }

    public ServerId        serverId() { return serverId;          }
    public UserId          userId()   { return userId;            }
    public Instant         joinedAt() { return joinedAt;          }
    public Set<ServerRole> roles()    { return Set.copyOf(roles); }

    public boolean hasRole(ServerRole role) {
        return roles.contains(role);
    }

    public void addRole(ServerRole role) {
        roles.add(role);
    }

    public void removeRole(ServerRole role) {
        roles.remove(role);
    } 
}
