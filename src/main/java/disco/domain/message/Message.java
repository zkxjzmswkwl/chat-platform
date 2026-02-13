package disco.domain.message;

import java.time.Instant;
import java.util.UUID;

public final class Message {

    private final UUID id;
    private final UUID channelId;
    private final UUID authorId;
    private final String content;
    private final Instant createdAt;

    public Message(UUID id,
                   UUID channelId,
                   UUID authorId,
                   String content,
                   Instant createdAt) {
        this.id = id;
        this.channelId = channelId;
        this.authorId = authorId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public UUID id()           { return id;        }
    public UUID channelId()    { return channelId; }
    public UUID authorId()     { return authorId;  }
    public String content()    { return content;   }
    public Instant createdAt() { return createdAt; }
}