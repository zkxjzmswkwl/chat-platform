package disco.repo.impl;

import disco.domain.user.User;
import disco.domain.user.UserId;
import disco.repo.UserRepository;
import org.jooq.DSLContext;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static disco.jooq.tables.Users.USERS;

public final class UserRepositoryImpl implements UserRepository {
    private final DSLContext ctx;

    public UserRepositoryImpl(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public User save(User user) {
        ctx.insertInto(USERS)
           .set(USERS.ID, user.id().value())
           .set(USERS.USERNAME, user.username())
           .set(USERS.CREATED_AT, OffsetDateTime.ofInstant(user.createdAt(), ZoneOffset.UTC))
           .onConflict(USERS.ID)
           .doNothing()
           .execute();

        return user;
    }

    @Override
    public Optional<User> findById(UserId id) {
        return ctx.selectFrom(USERS)
                  .where(USERS.ID.eq(id.value()))
                  .fetchOptional(this::map);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return ctx.selectFrom(USERS)
                  .where(USERS.USERNAME.eq(username))
                  .fetchOptional(this::map);
    }

    private User map(org.jooq.Record r) {
        return new User(
            new UserId(r.get(USERS.ID)),
            r.get(USERS.USERNAME),
            r.get(USERS.CREATED_AT).toInstant()
        );
    }
}