package id.ac.ui.cs.advprog.bidmartcatalog;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListingServiceTestThree {
    @Mock
    private Validator validator;

    @InjectMocks
    private UpdateListingRequest request;

    @Mock
    private ConstraintViolation<UpdateListingRequest> violation;

    // =========================
    // HAPPY PATH TESTS
    // =========================

    @Test
    void shouldPassValidationWhenDescriptionAndImageUrlAreValid() {
        request.setDescription("Updated description");
        request.setImageUrl("https://example.com/image.jpg");

        when(validator.validate(request))
                .thenReturn(Collections.emptySet());

        Set<ConstraintViolation<UpdateListingRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassValidationWhenOnlyDescriptionIsProvided() {
        request.setDescription("Updated description");
        request.setImageUrl(null);

        when(validator.validate(request))
                .thenReturn(Collections.emptySet());

        Set<ConstraintViolation<UpdateListingRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassValidationWhenOnlyImageUrlIsProvided() {
        request.setDescription(null);
        request.setImageUrl("https://example.com/image.png");

        when(validator.validate(request))
                .thenReturn(Collections.emptySet());

        Set<ConstraintViolation<UpdateListingRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    // =========================
    // UNHAPPY PATH TESTS
    // =========================

    @Test
    void shouldFailValidationWhenDescriptionIsBlank() {
        request.setDescription("   ");
        request.setImageUrl("https://example.com/image.jpg");

        when(validator.validate(request))
                .thenReturn(Set.of(violation));

        Set<ConstraintViolation<UpdateListingRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenImageUrlIsInvalid() {
        request.setDescription("Updated description");
        request.setImageUrl("not-a-valid-url");

        when(validator.validate(request))
                .thenReturn(Set.of(violation));

        Set<ConstraintViolation<UpdateListingRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenBothDescriptionAndImageUrlAreMissing() {
        request.setDescription(null);
        request.setImageUrl(null);

        when(validator.validate(request))
                .thenReturn(Set.of(violation));

        Set<ConstraintViolation<UpdateListingRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }
}
