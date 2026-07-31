package com.demo.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    @Test
    void testUserDtoCreation() {
        UserDto userDto = new UserDto();
        assertNotNull(userDto);
        assertNull(userDto.getUsername());
        assertNull(userDto.getPassword());
        assertNull(userDto.getEnabled());
        assertNull(userDto.getRoles());
    }

    @Test
    void testUserDtoSettersAndGetters() {
        UserDto userDto = new UserDto();
        
        userDto.setUsername("johndoe");
        assertEquals("johndoe", userDto.getUsername());
        
        userDto.setPassword("secret");
        assertEquals("secret", userDto.getPassword());
        
        userDto.setEnabled(true);
        assertEquals(true, userDto.getEnabled());
        
        RoleDto role = new RoleDto("USER");
        userDto.setRoles(java.util.Collections.singletonList(role));
        
        assertEquals(1, userDto.getRoles().size());
        assertEquals("USER", userDto.getRoles().get(0).name());
    }

    @Test
    void testUserDtoEqualsAndHashCode() {
        UserDto user1 = new UserDto();
        user1.setUsername("johndoe");
        user1.setPassword("secret");
        
        UserDto user2 = new UserDto();
        user2.setUsername("johndoe");
        user2.setPassword("secret");
        
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        
        user2.setUsername("janedoe");
        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }
}