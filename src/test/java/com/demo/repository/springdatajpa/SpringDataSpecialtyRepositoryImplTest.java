package com.demo.repository.springdatajpa;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.demo.model.Specialty;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SpringDataSpecialtyRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    private SpringDataSpecialtyRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new SpringDataSpecialtyRepositoryImpl(entityManager);
    }

    @Test
    void testCreation() {
        assertNotNull(repository);
    }

    @Test
    void testDelete() {
        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("radiology");

        Query nativeQuery = mock(Query.class);
        Query jpqlQuery = mock(Query.class);
        when(entityManager.contains(specialty)).thenReturn(true);
        when(entityManager.createNativeQuery(anyString())).thenReturn(nativeQuery);
        when(entityManager.createQuery(anyString())).thenReturn(jpqlQuery);
        when(nativeQuery.executeUpdate()).thenReturn(1);
        when(jpqlQuery.executeUpdate()).thenReturn(1);

        repository.delete(specialty);

        verify(entityManager).remove(specialty);
        verify(entityManager).createNativeQuery(anyString());
        verify(entityManager).createQuery(anyString());
    }
}
