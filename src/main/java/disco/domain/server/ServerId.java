package disco.domain.server;

import java.util.UUID;

public record ServerId(UUID val) {
    @Override
    public String toString() {
        return val.toString();
    }

    public UUID value() {
        return val;
    }
}
