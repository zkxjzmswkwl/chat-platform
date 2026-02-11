package disco.util;

import java.util.UUID;
import java.util.function.Supplier;

public final class Ids {
    private Ids() {}
    private static Supplier<UUID> gen = UUID::randomUUID;

    public static UUID newId() {
        return gen.get();
    }

    /**
     * Sticky. Uses given generator program-wide.
     * @param gen
     * @return previously set generator.
     */
    static Supplier<UUID> useGen(Supplier<UUID> gen) {
        // yoink it
        var oldGen = Ids.gen;
        Ids.gen = gen;
        // return old
        return oldGen;
    }
}