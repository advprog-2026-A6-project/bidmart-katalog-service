package id.ac.ui.cs.advprog.bidmartcatalog.util;

import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public final class ListingDateTimeParser {

    private static final DateTimeFormatter HTML_DATETIME_LOCAL =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private ListingDateTimeParser() {
    }

    public static LocalDateTime parseEndBefore(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String trimmed = value.trim();

        try {
            return LocalDateTime.parse(trimmed, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException ignored) {
            // Fall through to HTML datetime-local format.
        }

        try {
            return LocalDateTime.parse(trimmed, HTML_DATETIME_LOCAL);
        } catch (DateTimeParseException exception) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Invalid endBefore datetime. Use ISO format such as 2026-05-20T15:30 or 2026-05-20T15:30:00",
                    exception
            );
        }
    }
}
