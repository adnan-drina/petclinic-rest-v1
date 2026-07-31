package com.demo.repository.jpa;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.demo.model.Pet;
import com.demo.model.PetType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Collection;

class JpaPetRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    private JpaPetRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new JpaPetRepositoryImpl(entityManager);
    }

    @Test
    void testCreation() {
        assertNotNull(repository);
    }

    @Test
    void testFindPetTypes() {
        List<PetType> petTypes = List.of(
            createTestPetType(1, "Dog"),
            createTestPetType(2, "Cat")
        );

        jakarta.persistence.Query query = mock(jakarta.persistence.Query.class);
        when(entityManager.createQuery("SELECT ptype FROM PetType ptype ORDER BY ptype.name")).thenReturn(query);
        when(query.getResultList()).thenReturn(petTypes);

        List<PetType> result = repository.findPetTypes();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(entityManager).createQuery("SELECT ptype FROM PetType ptype ORDER BY ptype.name");
        verify(query).getResultList();
    }

    @Test
    void testFindPetTypesWithEmptyResult() {
        when(entityManager.createQuery(anyString())).thenReturn(mock(jakarta.persistence.Query.class));
        when(entityManager.createQuery(anyString()).getResultList()).thenReturn(Collections.emptyList());

        List<PetType> result = repository.findPetTypes();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(entityManager).createQuery("SELECT ptype FROM PetType ptype ORDER BY ptype.name");
    }

    @Test
    void testFindByIdWithExistingPet() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Fluffy");

        when(entityManager.find(Pet.class, 1)).thenReturn(pet);

        Pet result = repository.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Fluffy", result.getName());
        verify(entityManager).find(Pet.class, 1);
    }

    @Test
    void testFindByIdWithNonExistingPet() {
        when(entityManager.find(Pet.class, 999)).thenReturn(null);

        Pet result = repository.findById(999);

        assertNull(result);
        verify(entityManager).find(Pet.class, 999);
    }

    @Test
    void testSaveNewPet() {
        Pet pet = new Pet();
        pet.setName("Buddy");

        repository.save(pet);

        verify(entityManager).persist(pet);
    }

    @Test
    void testSaveExistingPet() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Buddy");

        repository.save(pet);

        verify(entityManager).merge(pet);
    }

    @Test
    void testSaveWithPersistenceException() {
        Pet pet = new Pet();
        pet.setName("Buddy");

        doThrow(new RuntimeException("Test exception")).when(entityManager).persist(any());

        assertThrows(RuntimeException.class, () -> repository.save(pet));
    }

    @Test
    void testFindAll() {
        List<Pet> pets = List.of(
            createTestPet(1, "Fluffy"),
            createTestPet(2, "Buddy")
        );

        jakarta.persistence.Query query = mock(jakarta.persistence.Query.class);
        when(entityManager.createQuery("SELECT pet FROM Pet pet")).thenReturn(query);
        when(query.getResultList()).thenReturn(pets);

        Collection<Pet> result = repository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(entityManager).createQuery("SELECT pet FROM Pet pet");
        verify(query).getResultList();
    }

    @Test
    void testFindAllWithEmptyResult() {
        when(entityManager.createQuery(anyString())).thenReturn(mock(jakarta.persistence.Query.class));
        when(entityManager.createQuery(anyString()).getResultList()).thenReturn(Collections.emptyList());

        Collection<Pet> result = repository.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(entityManager).createQuery("SELECT pet FROM Pet pet");
    }

    @Test
    void testFindAllWithPersistenceException() {
        when(entityManager.createQuery(anyString())).thenReturn(mock(jakarta.persistence.Query.class));
        when(entityManager.createQuery(anyString()).getResultList()).thenThrow(new PersistenceException("Test exception"));

        assertThrows(PersistenceException.class, () -> repository.findAll());
        verify(entityManager).createQuery("SELECT pet FROM Pet pet");
    }

    @Test
    void testDeletePetWithVisits() {
        Pet pet = createTestPet(1, "Fluffy");
        jakarta.persistence.Query visitDeleteQuery = mock(jakarta.persistence.Query.class);
        jakarta.persistence.Query petDeleteQuery = mock(jakarta.persistence.Query.class);

        when(entityManager.createQuery("DELETE FROM Visit visit WHERE pet_id=" + pet.getId())).thenReturn(visitDeleteQuery);
        when(entityManager.createQuery("DELETE FROM Pet pet WHERE id=" + pet.getId())).thenReturn(petDeleteQuery);

        repository.delete(pet);

        verify(visitDeleteQuery).executeUpdate();
        verify(petDeleteQuery).executeUpdate();
    }

    @Test
    void testDeletePetInPersistenceContext() {
        Pet pet = createTestPet(1, "Fluffy");

        when(entityManager.contains(pet)).thenReturn(true);
        when(entityManager.createQuery(anyString())).thenReturn(mock(jakarta.persistence.Query.class));

        repository.delete(pet);

        verify(entityManager).contains(pet);
        verify(entityManager).remove(pet);
    }

    @Test
    void testDeletePetNotInPersistenceContext() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Fluffy");

        when(entityManager.contains(pet)).thenReturn(false);
        when(entityManager.createQuery(anyString())).thenReturn(mock(jakarta.persistence.Query.class));

        repository.delete(pet);

        verify(entityManager).contains(pet);
        verify(entityManager, never()).remove(any());
    }

    @Test
    void testDeleteWithPersistenceException() {
        Pet pet = createTestPet(1, "Fluffy");

        when(entityManager.contains(pet)).thenReturn(true);
        when(entityManager.createQuery(anyString())).thenThrow(new PersistenceException("Test exception"));

        assertThrows(PersistenceException.class, () -> repository.delete(pet));
    }

    private Pet createTestPet(int id, String name) {
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName(name);
        return pet;
    }

    private PetType createTestPetType(int id, String name) {
        PetType petType = new PetType();
        petType.setId(id);
        petType.setName(name);
        return petType;
    }
}