package com.demo.repository.springdatajpa;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.demo.model.Visit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SpringDataVisitRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    private SpringDataVisitRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new SpringDataVisitRepositoryImpl(entityManager);
    }

    @Test
    void testCreation() {
        assertNotNull(repository);
    }

    @Test
    void testDelete() {
        Visit visit = new Visit();
        visit.setId(1);

        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);
        when(entityManager.contains(visit)).thenReturn(true);

        repository.delete(visit);

        verify(entityManager).createQuery(anyString());
        verify(entityManager).remove(visit);
    }
}
