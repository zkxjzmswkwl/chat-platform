package disco.service;

import disco.auth.ForbiddenException;
import disco.domain.server.ServerId;
import disco.domain.server.ServerRole;
import disco.domain.user.UserId;
import disco.repo.ServerMemberRepository;

public final class AuthorizationService {
    private final ServerMemberRepository members;

    public AuthorizationService(ServerMemberRepository members) {
        this.members = members;
    }

    public void requireMember(ServerId serverId, UserId userId) {
        if (members.find(serverId, userId).isEmpty()) {
            throw new ForbiddenException("You cannot do that.");
        }
    }

    public void requireOwner(ServerId serverId, UserId userId) {
        var member = members.find(serverId, userId);
        if (member.isEmpty()) {
            throw new ForbiddenException("Lacking authorization");
        }
        
        if (!member.get().roles().contains(ServerRole.SUPER_USER)) {
            throw new ForbiddenException("User is nowhere near super enough.");
        }
    }
}