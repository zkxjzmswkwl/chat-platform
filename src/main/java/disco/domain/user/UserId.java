package disco.domain.user;

import java.util.UUID;

public record UserId(UUID val) {
    @Override
    public String toString() {
        return val.toString();
    }

    public UUID value() {
        return val;
    }
}
