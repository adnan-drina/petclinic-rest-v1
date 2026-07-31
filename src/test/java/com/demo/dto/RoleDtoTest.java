package com.demo.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleDtoTest {

    @Test
    void testRoleDtoCreation() {
        RoleDto roleDto = new RoleDto("USER");
        assertNotNull(roleDto);
        assertEquals("USER", roleDto.name());
    }

    @Test
    void testRoleDtoSettersAndGetters() {
        // Records are immutable, so we test the constructor
        RoleDto roleDto = new RoleDto("ADMIN");
        assertEquals("ADMIN", roleDto.name());
    }

    @Test
    void testRoleDtoEqualsAndHashCode() {
        RoleDto roleDto1 = new RoleDto("USER");
        RoleDto roleDto2 = new RoleDto("USER");
        RoleDto roleDto3 = new RoleDto("ADMIN");
        
        assertEquals(roleDto1, roleDto2);
        assertEquals(roleDto1.hashCode(), roleDto2.hashCode());
        
        assertNotEquals(roleDto1, roleDto3);
        assertNotEquals(roleDto1.hashCode(), roleDto3.hashCode());
    }
}