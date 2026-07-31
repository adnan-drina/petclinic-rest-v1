package com.demo.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;

class RoleDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testRoleDtoCreation() {
        RoleDto roleDto = new RoleDto();
        assertNotNull(roleDto);
        assertNull(roleDto.getName());
    }

    @Test
    void testRoleDtoSettersAndGetters() {
        RoleDto roleDto = new RoleDto();
        
        roleDto.setName("ADMIN");
        assertEquals("ADMIN", roleDto.getName());
    }

    @Test
    void testRoleDtoEqualsAndHashCode() {
        RoleDto roleDto1 = new RoleDto();
        roleDto1.setName("USER");
        
        RoleDto roleDto2 = new RoleDto();
        roleDto2.setName("USER");
        
        assertEquals(roleDto1, roleDto2);
        assertEquals(roleDto1.hashCode(), roleDto2.hashCode());
        
        roleDto2.setName("ADMIN");
        assertNotEquals(roleDto1, roleDto2);
        assertNotEquals(roleDto1.hashCode(), roleDto2.hashCode());
    }
}