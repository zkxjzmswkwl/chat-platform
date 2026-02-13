package disco.auth;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public final class Passwords {

    private static final Argon2 argon2 =
        Argon2Factory.create(
            Argon2Factory.Argon2Types.ARGON2id
        );

    public static String hash(char[] password) {
        return argon2.hash(3, 64 * 1024, 1, password);
    }

    public static boolean verify(String hash, char[] password) {
        return argon2.verify(hash, password);
    }
} 
