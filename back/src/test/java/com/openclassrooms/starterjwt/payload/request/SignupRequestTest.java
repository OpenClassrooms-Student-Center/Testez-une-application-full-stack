package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();



    @Test
    void testValidSignupRequest() {
        SignupRequest req = new SignupRequest();
        req.setEmail("user@mail.com");
        req.setFirstName("Jean");
        req.setLastName("Dupont");
        req.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testBlankFields() {
        SignupRequest req = new SignupRequest();
        req.setEmail("");
        req.setFirstName("");
        req.setLastName("");
        req.setPassword("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(req);
        assertEquals(7, violations.size());
    }

    @Test
    void testInvalidEmail() {
        SignupRequest req = new SignupRequest();
        req.setEmail("not-an-email");
        req.setFirstName("Jean");
        req.setLastName("Dupont");
        req.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testTooShortOrLongFields() {
        SignupRequest req = new SignupRequest();
        req.setEmail("a@b.c");
        req.setFirstName("Jo");
        req.setLastName("Du");
        req.setPassword("123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(req);
        assertEquals(3, violations.size());
    }

    @Test
    void testTooLongEmail() {
        SignupRequest req = new SignupRequest();
        req.setEmail("a".repeat(51) + "@mail.com");
        req.setFirstName("Jean");
        req.setLastName("Dupont");
        req.setPassword("password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(req);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testEqualsAndHashCode() {
        SignupRequest req1 = new SignupRequest();
        req1.setEmail("user@mail.com");
        req1.setFirstName("Jean");
        req1.setLastName("Dupont");
        req1.setPassword("password123");

        SignupRequest req2 = new SignupRequest();
        req2.setEmail("user@mail.com");
        req2.setFirstName("Jean");
        req2.setLastName("Dupont");
        req2.setPassword("password123");

        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());

        req2.setEmail("autre@mail.com");
        assertNotEquals(req1, req2);
        assertNotEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void testToString() {
        SignupRequest req = new SignupRequest();
        req.setEmail("user@mail.com");
        req.setFirstName("Jean");
        req.setLastName("Dupont");
        req.setPassword("password123");

        String str = req.toString();
        assertTrue(str.contains("user@mail.com"));
        assertTrue(str.contains("Jean"));
        assertTrue(str.contains("Dupont"));
        assertTrue(str.contains("password123"));
    }

    @Test
    void testEqualsWithNull() {
        SignupRequest req = new SignupRequest();
        assertNotEquals(req, null);
    }

    @Test
    void testEqualsWithDifferentType() {
        SignupRequest req = new SignupRequest();
        assertNotEquals(req, "un string");
    }

    @Test
    void testEqualsWithSelf() {
        SignupRequest req = new SignupRequest();
        assertEquals(req, req);
    }

    @Test
    void testEqualsWithNullFields() {
        SignupRequest req1 = new SignupRequest();
        SignupRequest req2 = new SignupRequest();
        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void testToStringWithNullFields() {
        SignupRequest req = new SignupRequest();
        String str = req.toString();
        assertTrue(str.contains("null"));
    }

    // Java
    @Test
    void testEqualsWithOneNullField() {
        SignupRequest req1 = new SignupRequest();
        req1.setEmail("user@mail.com");
        req1.setFirstName(null);
        req1.setLastName("Dupont");
        req1.setPassword("password123");

        SignupRequest req2 = new SignupRequest();
        req2.setEmail("user@mail.com");
        req2.setFirstName("Jean");
        req2.setLastName("Dupont");
        req2.setPassword("password123");

        assertNotEquals(req1, req2);
    }

    @Test
    void testEqualsWithAllNullExceptOne() {
        SignupRequest req1 = new SignupRequest();
        req1.setEmail("user@mail.com");

        SignupRequest req2 = new SignupRequest();
        req2.setEmail("user@mail.com");

        assertEquals(req1, req2);
    }

    @Test
    void testHashCodeWithNullFields() {
        SignupRequest req1 = new SignupRequest();
        SignupRequest req2 = new SignupRequest();
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void testEqualsWithDifferentPassword() {
        SignupRequest req1 = new SignupRequest();
        req1.setEmail("user@mail.com");
        req1.setFirstName("Jean");
        req1.setLastName("Dupont");
        req1.setPassword("password123");

        SignupRequest req2 = new SignupRequest();
        req2.setEmail("user@mail.com");
        req2.setFirstName("Jean");
        req2.setLastName("Dupont");
        req2.setPassword("autre");

        assertNotEquals(req1, req2);
    }
}