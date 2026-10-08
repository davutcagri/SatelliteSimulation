package com.davutcagri.satsim.session;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.regex.Pattern;

public final class SessionIds {

    public static final String QUERY_PARAMETER = "session";
    public static final String HEADER = "X-Session-Id";

    private static final Pattern VALID_ID = Pattern.compile("[A-Za-z0-9-]{8,64}");

    private SessionIds() {
    }

    public static Optional<String> parse(String raw) {
        return raw != null && VALID_ID.matcher(raw).matches() ? Optional.of(raw) : Optional.empty();
    }

    public static Optional<String> fromUri(URI uri) {
        if (uri == null) {
            return Optional.empty();
        }
        return parse(UriComponentsBuilder.fromUri(uri).build().getQueryParams().getFirst(QUERY_PARAMETER));
    }
}
