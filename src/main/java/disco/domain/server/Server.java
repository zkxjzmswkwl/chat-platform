package disco.domain.server;

import java.time.Instant;

public final class Server {
    private final ServerId id;
    private String name;
    private final Instant createdAt;

    public Server(ServerId id, String name, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }
    public ServerId id()          { return id;          }
    public String   name()        { return name;        }
    public Instant  createdAt()   { return createdAt;   }

    public void rename(String newName) {
        this.name = newName;
    }
}