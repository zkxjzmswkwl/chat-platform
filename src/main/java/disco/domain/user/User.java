package disco.domain.user;

import java.time.Instant;

import disco.util.Ids;

public final class User {
    private final UserId id;
    private String username;
    private final String passwordHash;
    private final Instant createdAt;

    public User(UserId id, String username, String passwordHash, Instant createdAt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    public User(String username, String passwordHash) {
        this.id = new UserId(Ids.newId());
        this.username = username;
        this.passwordHash = passwordHash;
        this.createdAt = Instant.now();
    }

    public UserId  id()           { return id;           }
    public String  username()     { return username;     }
    public String  passwordHash() { return passwordHash; }
    public Instant createdAt()    { return createdAt;    }

    public void rename(String newName) {
        this.username = newName;
    }
}
