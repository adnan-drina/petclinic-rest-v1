package com.demo.repository.springdatajpa;

import com.demo.model.Pet;
import com.demo.model.PetType;
import com.demo.model.Owner;
import com.demo.repository.PetRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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