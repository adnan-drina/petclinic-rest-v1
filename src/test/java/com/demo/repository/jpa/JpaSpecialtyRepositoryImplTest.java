package com.demo.repository.jpa;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.demo.model.Specialty;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Collection;

class JpaSpecialtyRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    private JpaSpecialtyRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new JpaSpecialtyRepositoryImpl(entityManager);
    }

    @Test
    void testCreation() {
        assertNotNull(repository);
    }

    @Test
    void testFindByIdWithExistingSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("Cardiology");

        when(entityManager.find(Specialty.class, 1)).thenReturn(specialty);

        Specialty result = repository.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Cardiology", result.getName());
        verify(entityManager).find(Specialty.class, 1);
    }

    @Test
    void testFindByIdWithNonExistingSpecialty() {
        when(entityManager.find(Specialty.class, 999)).thenReturn(null);

        Specialty result = repository.findById(999);

        assertNull(result);
        verify(entityManager).find(Specialty.class, 999);
    }

    @Test
    void testFindAll() {
        List<Specialty> specialties = List.of(
            createTestSpecialty(1, "Cardiology"),
            createTestSpecialty(2, "Radiology")
        );

        jakarta.persistence.Query query = mock(jakarta.persistence.Query.class);
        when(entityManager.createQuery("SELECT s FROM Specialty s")).thenReturn(query);
        when(query.getResultList()).thenReturn(specialties);

        Collection<Specialty> result = repository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(entityManager).createQuery("SELECT s FROM Specialty s");
        verify(query).getResultList();
    }

    @Test
    void testSaveNewSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setName("Surgery");
        when(entityManager.find(Specialty.class, null)).thenReturn(null);

        repository.save(specialty);

        verify(entityManager).persist(specialty);
        verify(entityManager, never()).merge(any());
    }

    @Test
    void testSaveExistingSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("Surgery");

        when(entityManager.find(Specialty.class, 1)).thenReturn(specialty);

        repository.save(specialty);

        verify(entityManager).merge(specialty);
        verify(entityManager, never()).persist(any());
    }

    @Test
    void testSaveWithPersistenceException() {
        Specialty specialty = new Specialty();
        specialty.setName("Surgery");

        when(entityManager.find(Specialty.class, null)).thenReturn(null);
        doThrow(new PersistenceException("Test exception")).when(entityManager).persist(any());

        assertThrows(PersistenceException.class, () -> repository.save(specialty));
    }

    @Test
    void testDeleteExistingSpecialty() {
        Specialty specialty = createTestSpecialty(1, "Cardiology");
        jakarta.persistence.Query nativeQuery = mock(jakarta.persistence.Query.class);
        jakarta.persistence.Query query = mock(jakarta.persistence.Query.class);

        when(entityManager.contains(specialty)).thenReturn(true);
        when(entityManager.createNativeQuery(anyString())).thenReturn(nativeQuery);
        when(entityManager.createQuery(anyString())).thenReturn(query);

        repository.delete(specialty);

        verify(entityManager).remove(specialty);
        verify(nativeQuery).executeUpdate();
        verify(query).executeUpdate();
    }

    @Test
    void testDeleteWithPersistenceException() {
        Specialty specialty = createTestSpecialty(1, "Cardiology");

        when(entityManager.contains(specialty)).thenReturn(true);
        doThrow(new PersistenceException("Test exception")).when(entityManager).remove(any());

        assertThrows(PersistenceException.class, () -> repository.delete(specialty));
    }

    private Specialty createTestSpecialty(int id, String name) {
        Specialty specialty = new Specialty();
        specialty.setId(id);
        specialty.setName(name);
        return specialty;
    }
}