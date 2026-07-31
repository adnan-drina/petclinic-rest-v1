package com.demo.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class VisitDtoTest {

    @Test
    void testVisitDtoCreation() {
        VisitDto dto = new VisitDto();
        assertNotNull(dto);
        assertNull(dto.getDate());
        assertNull(dto.getDescription());
        assertNull(dto.getId());
    }

    @Test
    void testVisitDtoSettersAndGetters() {
        VisitDto dto = new VisitDto();
        LocalDate date = LocalDate.now();
        
        dto.setDate(date);
        assertEquals(date, dto.getDate());
        
        dto.setDescription("Annual checkup");
        assertEquals("Annual checkup", dto.getDescription());
        
        dto.setId(1);
        assertEquals(Integer.valueOf(1), dto.getId());
    }

    @Test
    void testVisitDtoFluentAPI() {
        LocalDate date = LocalDate.now();
        VisitDto dto = new VisitDto()
            .date(date)
            .description("Annual checkup")
            .id(1);
            
        assertEquals(date, dto.getDate());
        assertEquals("Annual checkup", dto.getDescription());
        assertEquals(Integer.valueOf(1), dto.getId());
    }

    @Test
    void testVisitDtoEqualsAndHashCode() {
        LocalDate date = LocalDate.now();
        VisitDto dto1 = new VisitDto();
        dto1.setDate(date);
        dto1.setDescription("Annual checkup");
        dto1.setId(1);
        
        VisitDto dto2 = new VisitDto();
        dto2.setDate(date);
        dto2.setDescription("Annual checkup");
        dto2.setId(1);
        
        VisitDto dto3 = new VisitDto();
        dto3.setDate(date);
        dto3.setDescription("Surgery");
        dto3.setId(1);
        
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testVisitDtoToString() {
        VisitDto dto = new VisitDto();
        dto.setDescription("Annual checkup");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.contains("Annual checkup"));
    }
}