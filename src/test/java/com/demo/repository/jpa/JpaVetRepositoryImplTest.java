package com.demo.repository.jpa;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.demo.model.Vet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Collection;
import java.util.Arrays;

class JpaVetRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    private JpaVetRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new JpaVetRepositoryImpl(entityManager);
    }

    @Test
    void testCreation() {
        assertNotNull(repository);
    }

    @Test
    void testFindByIdWithExistingVet() {
        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("John");
        vet.setLastName("Doe");

        when(entityManager.find(Vet.class, 1)).thenReturn(vet);

        Vet result = repository.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(entityManager).find(Vet.class, 1);
    }

    @Test
    void testFindByIdWithNonExistingVet() {
        when(entityManager.find(Vet.class, 999)).thenReturn(null);

        Vet result = repository.findById(999);

        assertNull(result);
        verify(entityManager).find(Vet.class, 999);
    }

    @Test
    void testFindByIdWithPersistenceException() {
        doThrow(new PersistenceException("Test exception")).when(entityManager).find(Vet.class, 1);

        assertThrows(PersistenceException.class, () -> repository.findById(1));
        verify(entityManager).find(Vet.class, 1);
    }

    @Test
    void testFindAll() {
        List<Vet> vets = List.of(
            createTestVet(1, "John", "Doe"),
            createTestVet(2, "Jane", "Smith")
        );

        jakarta.persistence.Query query = mock(jakarta.persistence.Query.class);
        when(entityManager.createQuery("SELECT vet FROM Vet vet")).thenReturn(query);
        when(query.getResultList()).thenReturn(vets);

        Collection<Vet> result = repository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(entityManager).createQuery("SELECT vet FROM Vet vet");
        verify(query).getResultList();
    }

    @Test
    void testFindAllWithEmptyResult() {
        when(entityManager.createQuery(anyString())).thenReturn(mock(jakarta.persistence.Query.class));
        when(entityManager.createQuery(anyString()).getResultList()).thenReturn(Arrays.asList());

        Collection<Vet> result = repository.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(entityManager).createQuery("SELECT vet FROM Vet vet");
    }

    @Test
    void testFindAllWithPersistenceException() {
        when(entityManager.createQuery(anyString())).thenReturn(mock(jakarta.persistence.Query.class));
        when(entityManager.createQuery(anyString()).getResultList()).thenThrow(new PersistenceException("Test exception"));

        assertThrows(PersistenceException.class, () -> repository.findAll());
        verify(entityManager).createQuery("SELECT vet FROM Vet vet");
    }

    @Test
    void testSaveNewVet() {
        Vet vet = new Vet();
        vet.setFirstName("John");
        vet.setLastName("Doe");
        when(entityManager.find(Vet.class, null)).thenReturn(null);

        repository.save(vet);

        verify(entityManager).persist(vet);
        verify(entityManager, never()).merge(any());
    }

    @Test
    void testSaveExistingVet() {
        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("John");
        vet.setLastName("Doe");

        when(entityManager.find(Vet.class, 1)).thenReturn(vet);

        repository.save(vet);

        verify(entityManager).merge(vet);
        verify(entityManager, never()).persist(any());
    }

    @Test
    void testSaveWithPersistenceException() {
        Vet vet = new Vet();
        vet.setFirstName("John");
        vet.setLastName("Doe");

        when(entityManager.find(Vet.class, null)).thenReturn(null);
        doThrow(new PersistenceException("Test exception")).when(entityManager).persist(any());

        assertThrows(PersistenceException.class, () -> repository.save(vet));
    }

    @Test
    void testDeleteExistingVet() {
        Vet vet = createTestVet(1, "John", "Doe");

        when(entityManager.contains(vet)).thenReturn(true);

        repository.delete(vet);

        verify(entityManager).remove(vet);
        verify(entityManager, never()).merge(any());
    }

    @Test
    void testDeleteNonExistingVet() {
        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("John");
        vet.setLastName("Doe");

        when(entityManager.contains(vet)).thenReturn(false);
        when(entityManager.merge(vet)).thenReturn(vet);

        repository.delete(vet);

        verify(entityManager).merge(vet);
        verify(entityManager).remove(vet);
    }

    @Test
    void testDeleteWithPersistenceException() {
        Vet vet = createTestVet(1, "John", "Doe");

        when(entityManager.contains(vet)).thenReturn(true);
        doThrow(new PersistenceException("Test exception")).when(entityManager).remove(any());

        assertThrows(PersistenceException.class, () -> repository.delete(vet));
    }

    private Vet createTestVet(int id, String firstName, String lastName) {
        Vet vet = new Vet();
        vet.setId(id);
        vet.setFirstName(firstName);
        vet.setLastName(lastName);
        return vet;
    }
}