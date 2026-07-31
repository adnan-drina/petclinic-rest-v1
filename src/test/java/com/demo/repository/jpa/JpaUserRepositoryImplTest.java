package com.demo.repository.jpa;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.demo.model.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class JpaUserRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    private JpaUserRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new JpaUserRepositoryImpl(entityManager);
    }

    @Test
    void testCreation() {
        assertNotNull(repository);
    }

    @Test
    void savePersistsNewUser() {
        User user = new User();
        user.setUsername("jdoe");
        when(entityManager.find(User.class, "jdoe")).thenReturn(null);
        repository.save(user);
        verify(entityManager).persist(user);
        verify(entityManager, never()).merge(any());
    }

    @Test
    void saveMergesExistingUser() {
        User user = new User();
        user.setUsername("jdoe");
        when(entityManager.find(User.class, "jdoe")).thenReturn(user);
        repository.save(user);
        verify(entityManager).merge(user);
        verify(entityManager, never()).persist(any());
        // Assert that user was properly handled by merge
        assertNotNull(user.getUsername());
    }
}
