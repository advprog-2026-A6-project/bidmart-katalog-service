package id.ac.ui.cs.advprog.bidmartcatalog.util;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ListingDateTimeParserTest {

    @Test
    void parseEndBefore_returnsNullForBlankValue() {
        assertThat(ListingDateTimeParser.parseEndBefore(null)).isNull();
        assertThat(ListingDateTimeParser.parseEndBefore("   ")).isNull();
    }

    @Test
    void parseEndBefore_parsesIsoLocalDateTimeWithSeconds() {
        LocalDateTime parsed = ListingDateTimeParser.parseEndBefore("2026-05-20T15:30:00");

        assertThat(parsed).isEqualTo(LocalDateTime.of(2026, 5, 20, 15, 30, 0));
    }

    @Test
    void parseEndBefore_parsesHtmlDatetimeLocalFormat() {
        LocalDateTime parsed = ListingDateTimeParser.parseEndBefore("2026-05-20T15:30");

        assertThat(parsed).isEqualTo(LocalDateTime.of(2026, 5, 20, 15, 30, 0));
    }

    @Test
    void parseEndBefore_rejectsInvalidValue() {
        assertThatThrownBy(() -> ListingDateTimeParser.parseEndBefore("not-a-date"))
                .isInstanceOf(ResponseStatusException.class);
    }
}
