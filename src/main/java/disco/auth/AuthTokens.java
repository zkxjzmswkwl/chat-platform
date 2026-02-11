package disco.auth;

import disco.domain.user.UserId;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

/**
 * no storage of tokens in a database at this time.
 * meaning anyone can log in as anyone.
 */
public final class AuthTokens {
    private static final long TTL_SECONDS = 60 * 60 * 24 * 7;
    private static final byte[] SECRET = System.getenv("DISCO_AUTH_SECRET").getBytes(StandardCharsets.UTF_8);

    public static String issue(UserId userId) {
        long issuedAt = Instant.now().getEpochSecond();
        String payload = userId.value() + "." + issuedAt;
        String sig = sign(payload);
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString((payload + "." + sig).getBytes(StandardCharsets.UTF_8));
    }

    public static UserId verify(String token) throws AuthException {
        String decoded = new String(
                Base64.getUrlDecoder().decode(token),
                StandardCharsets.UTF_8);

        String[] parts = decoded.split("\\.");
        if (parts.length != 3)
            throw new AuthException();

        String userId = parts[0];
        long issuedAt = Long.parseLong(parts[1]);
        String sig = parts[2];

        if (!sign(userId + "." + issuedAt).equals(sig))
            throw new AuthException();

        if (Instant.now().getEpochSecond() > issuedAt + TTL_SECONDS)
            throw new AuthException();

        return new UserId(java.util.UUID.fromString(userId));
    }

    private static String sign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET, "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}