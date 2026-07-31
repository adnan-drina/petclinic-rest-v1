package com.demo.repository.springdatajpa;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.demo.model.Pet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class SpringDataPetRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    private SpringDataPetRepositoryImpl springDataPetRepositoryImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        springDataPetRepositoryImpl = new SpringDataPetRepositoryImpl(entityManager);
    }

    @Test
    void testCreation() {
        assertNotNull(springDataPetRepositoryImpl);
    }

    @Test
    void testDelete() {
        Pet petToDelete = new Pet();
        petToDelete.setId(1);
        petToDelete.setName("Fluffy");
        
        Query q1 = mock(Query.class);
        Query q2 = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(q1, q2);
        when(q1.executeUpdate()).thenReturn(1);
        when(q2.executeUpdate()).thenReturn(1);
        when(entityManager.contains(petToDelete)).thenReturn(true);

        springDataPetRepositoryImpl.delete(petToDelete);
        
        verify(entityManager, times(1)).remove(petToDelete);
    }
}