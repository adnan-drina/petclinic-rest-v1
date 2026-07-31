package com.demo.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.net.URI;
import java.util.Set;

class RestErrorDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testRestErrorDtoCreation() {
        RestErrorDto restErrorDto = new RestErrorDto();
        assertNotNull(restErrorDto);
        assertNull(restErrorDto.getStatus());
        assertNull(restErrorDto.getError());
        assertNull(restErrorDto.getPath());
        assertNull(restErrorDto.getTimestamp());
        assertNull(restErrorDto.getMessage());
    }

    @Test
    void testRestErrorDtoSettersAndGetters() {
        RestErrorDto restErrorDto = new RestErrorDto();
        
        restErrorDto.setStatus(500);
        assertEquals(Integer.valueOf(500), restErrorDto.getStatus());
        
        restErrorDto.setError("Internal Server Error");
        assertEquals("Internal Server Error", restErrorDto.getError());
        
        URI path = URI.create("/api/vets");
        restErrorDto.setPath(path);
        assertEquals(path, restErrorDto.getPath());
        
        OffsetDateTime timestamp = OffsetDateTime.now();
        restErrorDto.setTimestamp(timestamp);
        assertEquals(timestamp, restErrorDto.getTimestamp());
        
        restErrorDto.setMessage("An error occurred");
        assertEquals("An error occurred", restErrorDto.getMessage());
    }

    @Test
    void testRestErrorDtoEqualsAndHashCode() {
        RestErrorDto error1 = new RestErrorDto();
        error1.setStatus(404);
        error1.setError("Not Found");
        
        RestErrorDto error2 = new RestErrorDto();
        error2.setStatus(404);
        error2.setError("Not Found");
        
        assertEquals(error1, error2);
        assertEquals(error1.hashCode(), error2.hashCode());
        
        error2.setStatus(500);
        assertNotEquals(error1, error2);
        assertNotEquals(error1.hashCode(), error2.hashCode());
    }
}