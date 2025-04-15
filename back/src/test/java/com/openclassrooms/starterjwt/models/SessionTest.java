package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidation_ShouldFailForBlankName() {
        Session session = new Session();
        session.setName("");
        session.setDate(new Date());
        session.setDescription("Valid description");

        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testValidation_ShouldFailForNullDate() {
        Session session = new Session();
        session.setName("Valid name");
        session.setDate(null);
        session.setDescription("Valid description");

        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertFalse(violations.isEmpty());
    }

   @Test
    void testValidation_ShouldFailForLongDescription() {
        Session session = new Session();
        session.setName("Valid name");
        session.setDate(new Date());

       session.setDescription(String.join("", Collections.nCopies(3000, "A")));
        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertFalse(violations.isEmpty());
    }

}