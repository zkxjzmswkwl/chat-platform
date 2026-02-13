package disco.repo;

import disco.domain.message.Message;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);
    List<Message> findRecent(UUID channelId, int limit);
}