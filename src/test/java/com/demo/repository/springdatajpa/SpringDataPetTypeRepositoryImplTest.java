package com.demo.repository.springdatajpa;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.demo.model.Pet;
import com.demo.model.PetType;
import com.demo.model.Visit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

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

    @Test
    void testDeleteWithPetTypeContainingPets() {
        PetType petType = createTestPetType(1);
        Pet pet1 = createTestPet(1);
        Pet pet2 = createTestPet(2);
        Visit visit1 = createTestVisit(1);
        Visit visit2 = createTestVisit(2);
        
        List<Pet> pets = Arrays.asList(pet1, pet2);
        pet1.setVisits(Arrays.asList(visit1));
        pet2.setVisits(Arrays.asList(visit2));

        Query petQuery = mock(Query.class);
        Query visit1DeleteQuery = mock(Query.class);
        Query visit2DeleteQuery = mock(Query.class);
        Query pet1DeleteQuery = mock(Query.class);
        Query pet2DeleteQuery = mock(Query.class);
        Query petTypeDeleteQuery = mock(Query.class);

        when(entityManager.contains(petType)).thenReturn(true);
        when(entityManager.createQuery("SELECT pet FROM Pet pet WHERE type_id=" + petType.getId())).thenReturn(petQuery);
        when(petQuery.getResultList()).thenReturn(pets);
        when(entityManager.createQuery("DELETE FROM Visit visit WHERE id=" + visit1.getId())).thenReturn(visit1DeleteQuery);
        when(entityManager.createQuery("DELETE FROM Visit visit WHERE id=" + visit2.getId())).thenReturn(visit2DeleteQuery);
        when(entityManager.createQuery("DELETE FROM Pet pet WHERE id=" + pet1.getId())).thenReturn(pet1DeleteQuery);
        when(entityManager.createQuery("DELETE FROM Pet pet WHERE id=" + pet2.getId())).thenReturn(pet2DeleteQuery);
        when(entityManager.createQuery("DELETE FROM PetType pettype WHERE id=" + petType.getId())).thenReturn(petTypeDeleteQuery);

        springDataPetTypeRepositoryImpl.delete(petType);

        verify(entityManager).remove(petType);
        verify(visit1DeleteQuery).executeUpdate();
        verify(visit2DeleteQuery).executeUpdate();
        verify(pet1DeleteQuery).executeUpdate();
        verify(pet2DeleteQuery).executeUpdate();
        verify(petTypeDeleteQuery).executeUpdate();
    }

    @Test
    void testDeleteWithPetTypeNotInPersistenceContext() {
        PetType petType = createTestPetType(1);
        List<Pet> emptyPetList = Arrays.asList();
        Query petQuery = mock(Query.class);
        Query petTypeDeleteQuery = mock(Query.class);
        
        when(entityManager.contains(petType)).thenReturn(false);
        
        PetType mergedPetType = createTestPetType(1);
        when(entityManager.merge(petType)).thenReturn(mergedPetType);
        when(entityManager.createQuery("SELECT pet FROM Pet pet WHERE type_id=" + petType.getId())).thenReturn(petQuery);
        when(petQuery.getResultList()).thenReturn(emptyPetList);
        when(entityManager.createQuery("DELETE FROM PetType pettype WHERE id=" + petType.getId())).thenReturn(petTypeDeleteQuery);

        springDataPetTypeRepositoryImpl.delete(petType);

        verify(entityManager).merge(petType);
        verify(entityManager).remove(mergedPetType);
        // Verify that Pet and Visit delete queries were NOT called since there are no pets
        verify(entityManager, never()).createQuery(contains("DELETE FROM Visit WHERE"));
        verify(entityManager, never()).createQuery(contains("DELETE FROM Pet WHERE"));
        verify(petTypeDeleteQuery).executeUpdate();
    }

    private PetType createTestPetType(int id) {
        PetType petType = new PetType();
        petType.setId(id);
        petType.setName("Dog");
        return petType;
    }

    private Pet createTestPet(int id) {
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName("Fluffy");
        return pet;
    }

    private Visit createTestVisit(int id) {
        Visit visit = new Visit();
        visit.setId(id);
        visit.setDescription("Checkup");
        return visit;
    }
}