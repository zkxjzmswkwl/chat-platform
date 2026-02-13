package disco.api.dto;

import java.time.Instant;
import java.util.UUID;

import disco.domain.message.Message;

public record MessageResponse(
    UUID id,
    UUID channelId,
    UUID authorId,
    String content,
    Instant createdAt
) {
    public static MessageResponse from(Message m) {
        return new MessageResponse(
            m.id(),
            m.channelId(),
            m.authorId(),
            m.content(),
            m.createdAt()
        );
    }
}
