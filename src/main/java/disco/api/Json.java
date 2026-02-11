package disco.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class Json {
    public static final ObjectMapper MAPPER =
        new ObjectMapper()
            .registerModule(new JavaTimeModule())
            // this shit for the human readable timestamps g
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
}
