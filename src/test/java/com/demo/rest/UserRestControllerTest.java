package com.demo.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.demo.dto.*;
import com.demo.mapper.*;
import com.demo.model.*;
import com.demo.service.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.net.URI;

class UserRestControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UriInfo uriInfo;

    private UserRestController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new UserRestController(userService, userMapper);
    }

    @Test
    void testAddOwnerWithValidUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password");

        User user = new User();
        user.setUsername("testuser");
        
        UserDto expectedResponseDto = new UserDto();
        expectedResponseDto.setUsername("testuser");

        when(userMapper.toUser(userDto)).thenReturn(user);
        doNothing().when(userService).saveUser(user);
        when(userMapper.toUserDto(user)).thenReturn(expectedResponseDto);
        
        var uriBuilder = mock(jakarta.ws.rs.core.UriBuilder.class);
        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.resolveTemplate(anyString(), any())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(URI.create("http://localhost/api/users/testuser"));

        Response response = controller.addOwner(userDto, uriInfo);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        verify(userService).saveUser(user);
        verify(userMapper).toUser(userDto);
        verify(userMapper).toUserDto(user);
    }

    @Test
    void testAddOwnerWithNullUserDto() {
        Response response = controller.addOwner(null, uriInfo);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}