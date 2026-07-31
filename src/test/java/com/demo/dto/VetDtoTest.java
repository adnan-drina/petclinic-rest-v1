package com.demo.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

class VetDtoTest {

    @Test
    void testVetDtoCreation() {
        VetDto vetDto = new VetDto(1, "John", "Doe", new ArrayList<>());
        assertNotNull(vetDto);
        assertEquals(1, vetDto.id());
        assertEquals("John", vetDto.firstName());
        assertEquals("Doe", vetDto.lastName());
        assertNotNull(vetDto.specialties());
        assertTrue(vetDto.specialties().isEmpty());
    }

    @Test
    void testVetDtoSettersAndGetters() {
        // Records are immutable, so we test the constructor and derived methods
        VetDto vetDto = new VetDto(1, "John", "Doe", new ArrayList<>());
        
        assertEquals(1, vetDto.id());
        assertEquals("John", vetDto.firstName());
        assertEquals("Doe", vetDto.lastName());
        
        SpecialtyDto specialty = new SpecialtyDto(1, "Surgery");
        VetDto vetDtoWithSpecialty = vetDto.addSpecialtiesItem(specialty);
        
        assertEquals(1, vetDtoWithSpecialty.specialties().size());
        assertEquals("Surgery", vetDtoWithSpecialty.specialties().get(0).name());
    }

    @Test
    void testVetDtoEqualsAndHashCode() {
        VetDto vetDto1 = new VetDto(1, "John", "Doe", new ArrayList<>());
        VetDto vetDto2 = new VetDto(1, "John", "Doe", new ArrayList<>());
        VetDto vetDto3 = new VetDto(2, "John", "Doe", new ArrayList<>());
        
        assertEquals(vetDto1, vetDto2);
        assertEquals(vetDto1.hashCode(), vetDto2.hashCode());
        
        assertNotEquals(vetDto1, vetDto3);
        assertNotEquals(vetDto1.hashCode(), vetDto3.hashCode());
    }
}