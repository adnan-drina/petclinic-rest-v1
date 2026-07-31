package com.demo.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PetTypeDtoTest {

    @Test
    void testPetTypeDtoCreation() {
        PetTypeDto dto = new PetTypeDto(1, "Dog");
        assertNotNull(dto);
        assertEquals(Integer.valueOf(1), dto.id());
        assertEquals("Dog", dto.name());
    }

    @Test
    void testPetTypeDtoValidation() {
        // Test with null name should work (validation is at runtime)
        PetTypeDto dto = new PetTypeDto(null, null);
        assertNull(dto.id());
        assertNull(dto.name());
    }

    @Test
    void testPetTypeDtoEqualsAndHashCode() {
        PetTypeDto dto1 = new PetTypeDto(1, "Dog");
        PetTypeDto dto2 = new PetTypeDto(1, "Dog");
        PetTypeDto dto3 = new PetTypeDto(2, "Cat");
        
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testPetTypeDtoToString() {
        PetTypeDto dto = new PetTypeDto(1, "Dog");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.contains("PetTypeDto"));
    }
}