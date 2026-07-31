package com.demo.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecialtyDtoTest {

    @Test
    void testSpecialtyDtoCreation() {
        SpecialtyDto dto = new SpecialtyDto(1, "Surgery");
        assertNotNull(dto);
        assertEquals(Integer.valueOf(1), dto.id());
        assertEquals("Surgery", dto.name());
    }

    @Test
    void testSpecialtyDtoEqualsAndHashCode() {
        SpecialtyDto dto1 = new SpecialtyDto(1, "Surgery");
        SpecialtyDto dto2 = new SpecialtyDto(1, "Surgery");
        SpecialtyDto dto3 = new SpecialtyDto(2, "Radiology");
        
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testSpecialtyDtoToString() {
        SpecialtyDto dto = new SpecialtyDto(1, "Surgery");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.contains("SpecialtyDto"));
    }
}