package com.demo.dto;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RestErrorDtoTest {

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
        
        String path = "/api/vets";
        restErrorDto.setPath(java.net.URI.create(path));
        assertEquals(path, restErrorDto.getPath().toString());
        
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