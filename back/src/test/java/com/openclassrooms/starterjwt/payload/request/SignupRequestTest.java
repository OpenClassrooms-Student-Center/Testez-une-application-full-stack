package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void shouldPassValidationWithValidData() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("valid@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("securePass123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationWhenEmailIsInvalid() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("invalid-email");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("securePass123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void shouldFailValidationWhenFirstNameIsTooShort() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("valid@example.com");
        signupRequest.setFirstName("Jo");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("securePass123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    void shouldFailValidationWhenPasswordIsTooShort() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("valid@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void shouldTestEqualsAndHashCode() {

        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("securePass123");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("securePass123");

        SignupRequest request3 = new SignupRequest();
        request3.setEmail("other@example.com");
        request3.setFirstName("Jane");
        request3.setLastName("Smith");
        request3.setPassword("otherPass");

        assertThat(request1).isEqualTo(request2);
        assertThat(request1).hasSameHashCodeAs(request2);
        assertThat(request1).isNotEqualTo(request3);
    }

    @Test
    void shouldTestToString() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("securePass123");

        String requestToString = request.toString();

        assertThat(requestToString).contains("test@example.com", "John", "Doe", "securePass123");
    }

    @Test
    void shouldTestCanEqual() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");

        assertThat(request1.canEqual(request2)).isTrue();
        assertThat(request1.canEqual(new Object())).isFalse();
    }
}
