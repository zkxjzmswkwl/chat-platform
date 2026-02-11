package disco.domain.user;

import java.time.Instant;

import disco.util.Ids;

public final class User {
    private final UserId id;
    private String username;
    private final Instant createdAt;

    public User(UserId id, String username, Instant createdAt) {
        this.id = id;
        this.username = username;
        this.createdAt = createdAt;
    }

    public User(String username) {
        this.id = new UserId(Ids.newId());
        this.username = username;
        this.createdAt = Instant.now();
    }

    public UserId  id()        { return id;        }
    public String  username()  { return username;  }
    public Instant createdAt() { return createdAt; }

    public void rename(String newName) {
        this.username = newName;
    }
}
