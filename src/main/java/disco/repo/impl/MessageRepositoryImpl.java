package disco.repo.impl;

import disco.domain.message.Message;
import disco.repo.MessageRepository;

import org.jooq.DSLContext;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static disco.jooq.tables.Messages.MESSAGES;

public final class MessageRepositoryImpl implements MessageRepository {
    private final DSLContext ctx;

    public MessageRepositoryImpl(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Message save(Message m) {
        ctx.insertInto(MESSAGES)
           .set(MESSAGES.ID, m.id())
           .set(MESSAGES.CHANNEL_ID, m.channelId())
           .set(MESSAGES.AUTHOR_ID, m.authorId())
           .set(MESSAGES.CONTENT, m.content())
           .set(MESSAGES.CREATED_AT, OffsetDateTime.ofInstant(m.createdAt(), ZoneOffset.UTC))
           .execute();

        return m;
    }

    @Override
    public List<Message> findRecent(UUID channelId, int limit) {
        return ctx.selectFrom(MESSAGES)
                  .where(MESSAGES.CHANNEL_ID.eq(channelId))
                  .orderBy(MESSAGES.CREATED_AT.desc())
                  .limit(limit)
                  .fetch(r -> new Message(
                      r.get(MESSAGES.ID),
                      r.get(MESSAGES.CHANNEL_ID),
                      r.get(MESSAGES.AUTHOR_ID),
                      r.get(MESSAGES.CONTENT),
                      r.get(MESSAGES.CREATED_AT).toInstant()
                  ));
    }
}