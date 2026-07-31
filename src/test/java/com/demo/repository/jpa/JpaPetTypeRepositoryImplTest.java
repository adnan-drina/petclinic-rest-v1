package com.demo.repository.jpa;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.demo.model.Pet;
import com.demo.model.PetType;
import com.demo.model.Visit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Collection;

class JpaPetTypeRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    private JpaPetTypeRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new JpaPetTypeRepositoryImpl(entityManager);
    }

    @Test
    void testCreation() {
        assertNotNull(repository);
    }

    @Test
    void testFindByIdWithExistingPetType() {
        PetType petType = new PetType();
        petType.setId(1);
        petType.setName("Dog");

        when(entityManager.find(PetType.class, 1)).thenReturn(petType);

        PetType result = repository.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Dog", result.getName());
        verify(entityManager).find(PetType.class, 1);
    }

    @Test
    void testFindByIdWithNonExistingPetType() {
        when(entityManager.find(PetType.class, 999)).thenReturn(null);

        PetType result = repository.findById(999);

        assertNull(result);
        verify(entityManager).find(PetType.class, 999);
    }

    @Test
    void testFindAll() {
        List<PetType> petTypes = List.of(
            createTestPetType(1, "Dog"),
            createTestPetType(2, "Cat")
        );

        jakarta.persistence.Query query = mock(jakarta.persistence.Query.class);
        when(entityManager.createQuery("SELECT ptype FROM PetType ptype")).thenReturn(query);
        when(query.getResultList()).thenReturn(petTypes);

        Collection<PetType> result = repository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(entityManager).createQuery("SELECT ptype FROM PetType ptype");
        verify(query).getResultList();
    }

    @Test
    void testSaveNewPetType() {
        PetType petType = new PetType();
        petType.setName("Rabbit");
        when(entityManager.find(PetType.class, null)).thenReturn(null);

        repository.save(petType);

        verify(entityManager).persist(petType);
        verify(entityManager, never()).merge(any());
    }

    @Test
    void testSaveExistingPetType() {
        PetType petType = new PetType();
        petType.setId(1);
        petType.setName("Rabbit");

        when(entityManager.find(PetType.class, 1)).thenReturn(petType);

        repository.save(petType);

        verify(entityManager).merge(petType);
        verify(entityManager, never()).persist(any());
    }

    @Test
    void testSaveWithPersistenceException() {
        PetType petType = new PetType();
        petType.setName("Rabbit");

        when(entityManager.find(PetType.class, null)).thenReturn(null);
        doThrow(new PersistenceException("Test exception")).when(entityManager).persist(any());

        assertThrows(PersistenceException.class, () -> repository.save(petType));
    }

    @Test
    void testDeletePetTypeWithNoPets() {
        PetType petType = createTestPetType(1, "Dog");
        jakarta.persistence.Query query = mock(jakarta.persistence.Query.class);

        when(entityManager.contains(petType)).thenReturn(true);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        repository.delete(petType);

        verify(entityManager).remove(petType);
        verify(query, times(1)).executeUpdate(); // Only the PetType delete query
    }

    @Test
    void testDeletePetTypeWithPets() {
        PetType petType = createTestPetType(1, "Dog");
        Visit visit1 = createTestVisit(1);
        Visit visit2 = createTestVisit(2);
        Pet pet1 = createTestPet(1);
        Pet pet2 = createTestPet(2);
        pet1.setVisits(Arrays.asList(visit1));
        pet2.setVisits(Arrays.asList(visit2));

        jakarta.persistence.Query petQuery = mock(jakarta.persistence.Query.class);
        jakarta.persistence.Query visit1DeleteQuery = mock(jakarta.persistence.Query.class);
        jakarta.persistence.Query visit2DeleteQuery = mock(jakarta.persistence.Query.class);
        jakarta.persistence.Query pet1DeleteQuery = mock(jakarta.persistence.Query.class);
        jakarta.persistence.Query pet2DeleteQuery = mock(jakarta.persistence.Query.class);
        jakarta.persistence.Query petTypeDeleteQuery = mock(jakarta.persistence.Query.class);

        when(entityManager.contains(petType)).thenReturn(true);
        when(entityManager.createQuery("SELECT pet FROM Pet pet WHERE type_id=" + petType.getId())).thenReturn(petQuery);
        when(petQuery.getResultList()).thenReturn(Arrays.asList(pet1, pet2));
        when(entityManager.createQuery("DELETE FROM Visit visit WHERE id=" + visit1.getId())).thenReturn(visit1DeleteQuery);
        when(entityManager.createQuery("DELETE FROM Visit visit WHERE id=" + visit2.getId())).thenReturn(visit2DeleteQuery);
        when(entityManager.createQuery("DELETE FROM Pet pet WHERE id=" + pet1.getId())).thenReturn(pet1DeleteQuery);
        when(entityManager.createQuery("DELETE FROM Pet pet WHERE id=" + pet2.getId())).thenReturn(pet2DeleteQuery);
        when(entityManager.createQuery("DELETE FROM PetType pettype WHERE id=" + petType.getId())).thenReturn(petTypeDeleteQuery);

        repository.delete(petType);

        verify(entityManager).remove(petType);
        verify(visit1DeleteQuery).executeUpdate();
        verify(visit2DeleteQuery).executeUpdate();
        verify(pet1DeleteQuery).executeUpdate();
        verify(pet2DeleteQuery).executeUpdate();
        verify(petTypeDeleteQuery).executeUpdate();
    }

    @Test
    void testDeletePetTypeNotInPersistenceContext() {
        PetType petType = createTestPetType(1, "Dog");
        PetType mergedPetType = createTestPetType(1, "Dog");

        when(entityManager.contains(petType)).thenReturn(false);
        when(entityManager.merge(petType)).thenReturn(mergedPetType);
        when(entityManager.createQuery(anyString())).thenReturn(mock(jakarta.persistence.Query.class));
        when(entityManager.createQuery(anyString()).getResultList()).thenReturn(Collections.emptyList());

        repository.delete(petType);

        verify(entityManager).merge(petType);
        verify(entityManager).remove(mergedPetType);
    }

    @Test
    void testDeleteWithPersistenceException() {
        PetType petType = createTestPetType(1, "Dog");

        when(entityManager.contains(petType)).thenReturn(true);
        doThrow(new PersistenceException("Test exception")).when(entityManager).remove(any());

        assertThrows(PersistenceException.class, () -> repository.delete(petType));
    }

    private PetType createTestPetType(int id, String name) {
        PetType petType = new PetType();
        petType.setId(id);
        petType.setName(name);
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