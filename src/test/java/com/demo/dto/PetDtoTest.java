package com.demo.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PetDtoTest {

    @Test
    void testPetDtoCreation() {
        PetDto dto = new PetDto();
        assertNotNull(dto);
        assertNull(dto.getName());
        assertNull(dto.getBirthDate());
        assertNull(dto.getTypeId());
        assertNull(dto.getId());
        assertNotNull(dto.getVisits());
        assertTrue(dto.getVisits().isEmpty());
    }

    @Test
    void testPetDtoSettersAndGetters() {
        PetDto dto = new PetDto();
        LocalDate birthDate = LocalDate.of(2020, 1, 1);
        
        dto.setName("Fluffy");
        assertEquals("Fluffy", dto.getName());
        
        dto.setBirthDate(birthDate);
        assertEquals(birthDate, dto.getBirthDate());
        
        dto.setTypeId(1);
        assertEquals(Integer.valueOf(1), dto.getTypeId());
        
        dto.setId(1);
        assertEquals(Integer.valueOf(1), dto.getId());
    }

    @Test
    void testPetDtoFluentAPI() {
        LocalDate birthDate = LocalDate.of(2020, 1, 1);
        VisitDto visit = new VisitDto();
        visit.setDescription("Checkup");
        
        PetDto dto = new PetDto()
            .name("Fluffy")
            .birthDate(birthDate)
            .typeId(1)
            .id(1)
            .addVisitsItem(visit);
            
        assertEquals("Fluffy", dto.getName());
        assertEquals(birthDate, dto.getBirthDate());
        assertEquals(Integer.valueOf(1), dto.getTypeId());
        assertEquals(Integer.valueOf(1), dto.getId());
        assertEquals(1, dto.getVisits().size());
    }

    @Test
    void testPetDtoVisitsManagement() {
        PetDto dto = new PetDto();
        VisitDto visit1 = new VisitDto();
        VisitDto visit2 = new VisitDto();
        
        dto.setVisits(new ArrayList<>());
        dto.addVisitsItem(visit1);
        dto.addVisitsItem(visit2);
        
        assertEquals(2, dto.getVisits().size());
    }

    @Test
    void testPetDtoEqualsAndHashCode() {
        PetDto dto1 = new PetDto();
        dto1.setName("Fluffy");
        dto1.setId(1);
        
        PetDto dto2 = new PetDto();
        dto2.setName("Fluffy");
        dto2.setId(1);
        
        PetDto dto3 = new PetDto();
        dto3.setName("Rex");
        dto3.setId(1);
        
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testPetDtoToString() {
        PetDto dto = new PetDto();
        dto.setName("Fluffy");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.contains("Fluffy"));
    }
}