package disco.service;

import disco.domain.message.Message;
import disco.repo.MessageRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class MessageService {
    private final MessageRepository repo;

    public MessageService(MessageRepository repo) {
        this.repo = repo;
    }

    public Message createMessage(UUID channelId, UUID authorId, String content) {
        Message m = new Message(
            UUID.randomUUID(),
            channelId,
            authorId,
            content,
            Instant.now()
        );
        return repo.save(m);
    }

    public List<Message> recent(UUID channelId, int limit) {
        return repo.findRecent(channelId, limit);
    }
}