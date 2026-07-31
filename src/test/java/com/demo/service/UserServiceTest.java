package com.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.demo.model.Role;
import com.demo.model.User;
import com.demo.repository.UserRepository;

/**
 * Characterization of UserServiceImpl role-prefixing and validation.
 * Uses real User/Role beans (mocks hide setName mutations). save() is void.
 */
class UserServiceTest {

    @Mock UserRepository userRepository;

    UserService userService;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
        doNothing().when(userRepository).save(any(User.class));
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("saveUser prefixes ROLE_ when missing")
    void prefixesRoleName() {
        User user = userWithRole("ADMIN");

        userService.saveUser(user);

        assertThat(user.getRoles()).hasSize(1);
        Role role = user.getRoles().iterator().next();
        assertThat(role.getName()).isEqualTo("ROLE_ADMIN");
        assertThat(role.getUser()).isSameAs(user);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("saveUser keeps existing ROLE_ prefix")
    void keepsExistingPrefix() {
        User user = userWithRole("ROLE_USER");

        userService.saveUser(user);

        assertThat(user.getRoles().iterator().next().getName()).isEqualTo("ROLE_USER");
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("saveUser prefixes only roles lacking ROLE_")
    void mixedRolePrefixing() {
        User user = new User();
        user.setUsername("mixed");
        user.setPassword("secret");
        Role bare = role("ADMIN");
        Role prefixed = role("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(bare);
        roles.add(prefixed);
        user.setRoles(roles);

        userService.saveUser(user);

        assertThat(user.getRoles())
            .extracting(Role::getName)
            .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
        for (Role r : user.getRoles()) {
            assertThat(r.getUser()).isSameAs(user);
        }
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("saveUser rejects null roles")
    void rejectsNullRoles() {
        User user = new User();
        user.setUsername("nobody");
        user.setRoles(null);

        assertThatThrownBy(() -> userService.saveUser(user))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("role");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("saveUser rejects empty roles")
    void rejectsEmptyRoles() {
        User user = new User();
        user.setUsername("nobody");
        user.setRoles(new HashSet<>());

        assertThatThrownBy(() -> userService.saveUser(user))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("role");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("saveUser does not overwrite Role.user when already set")
    void preservesExistingRoleUser() {
        User user = new User();
        user.setUsername("owner");
        User other = new User();
        other.setUsername("other");
        Role role = role("ADMIN");
        role.setUser(other);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userService.saveUser(user);

        assertThat(role.getName()).isEqualTo("ROLE_ADMIN");
        assertThat(role.getUser()).isSameAs(other);
        verify(userRepository).save(user);
    }

    private static User userWithRole(String roleName) {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        Set<Role> roles = new HashSet<>();
        roles.add(role(roleName));
        user.setRoles(roles);
        return user;
    }

    private static Role role(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }
}
