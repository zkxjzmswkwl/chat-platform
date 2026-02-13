package disco.domain.user;

import java.util.UUID;

public record UserId(UUID val) {
    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return val.equals(userId.val);
    }

    public UUID value() {
        return val;
    }
}
