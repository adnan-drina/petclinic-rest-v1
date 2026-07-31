package com.demo.repository.springdatajpa;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.demo.model.PetType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class SpringDataPetTypeRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    private SpringDataPetTypeRepositoryImpl springDataPetTypeRepositoryImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        springDataPetTypeRepositoryImpl = new SpringDataPetTypeRepositoryImpl(entityManager);
    }

    @Test
    void testCreation() {
        assertNotNull(springDataPetTypeRepositoryImpl);
    }
}