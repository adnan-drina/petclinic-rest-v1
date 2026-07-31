package com.demo.repository.jpa;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.demo.model.Visit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Collection;

class JpaVisitRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    private JpaVisitRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new JpaVisitRepositoryImpl(entityManager);
    }

    @Test
    void testCreation() {
        assertNotNull(repository);
    }

    @Test
    void testSaveNewVisit() {
        Visit visit = new Visit();
        visit.setDescription("Routine checkup");

        repository.save(visit);

        verify(entityManager).persist(visit);
    }

    @Test
    void testSaveExistingVisit() {
        Visit visit = new Visit();
        visit.setId(1);
        visit.setDescription("Routine checkup");

        repository.save(visit);

        verify(entityManager).merge(visit);
    }

    @Test
    void testSaveWithNullId() {
        Visit visit = new Visit();
        visit.setDescription("Routine checkup");
        visit.setId(null);

        repository.save(visit);

        verify(entityManager).persist(visit);
    }

    @Test
    void testSaveWithPersistenceException() {
        Visit visit = new Visit();
        visit.setDescription("Routine checkup");
        visit.setId(null);

        doThrow(new RuntimeException("Test exception")).when(entityManager).persist(any());

        assertThrows(RuntimeException.class, () -> repository.save(visit));
    }

    @Test
    void testFindByPetId() {
        Integer petId = 1;
        List<Visit> visits = List.of(
            createTestVisit(1, "Routine checkup"),
            createTestVisit(2, "Vaccination")
        );

        Query query = mock(Query.class);
        when(entityManager.createQuery("SELECT v FROM Visit v where v.pet.id= :id")).thenReturn(query);
        when(query.setParameter("id", petId)).thenReturn(query);
        when(query.getResultList()).thenReturn(visits);

        List<Visit> result = repository.findByPetId(petId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(entityManager).createQuery("SELECT v FROM Visit v where v.pet.id= :id");
        verify(query).setParameter("id", petId);
        verify(query).getResultList();
    }

    @Test
    void testFindByPetIdWithEmptyResult() {
        Integer petId = 999;

        Query query = mock(Query.class);
        when(entityManager.createQuery("SELECT v FROM Visit v where v.pet.id= :id")).thenReturn(query);
        when(query.setParameter("id", petId)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        List<Visit> result = repository.findByPetId(petId);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(entityManager).createQuery("SELECT v FROM Visit v where v.pet.id= :id");
        verify(query).setParameter("id", petId);
        verify(query).getResultList();
    }

    @Test
    void testFindByPetIdWithNullPetId() {
        Integer petId = null;

        Query query = mock(Query.class);
        when(entityManager.createQuery("SELECT v FROM Visit v where v.pet.id= :id")).thenReturn(query);
        when(query.setParameter("id", petId)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        List<Visit> result = repository.findByPetId(petId);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(entityManager).createQuery("SELECT v FROM Visit v where v.pet.id= :id");
        verify(query).setParameter("id", petId);
    }

    @Test
    void testFindByIdWithExistingVisit() {
        Visit visit = createTestVisit(1, "Routine checkup");

        when(entityManager.find(Visit.class, 1)).thenReturn(visit);

        Visit result = repository.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Routine checkup", result.getDescription());
        verify(entityManager).find(Visit.class, 1);
    }

    @Test
    void testFindByIdWithNonExistingVisit() {
        when(entityManager.find(Visit.class, 999)).thenReturn(null);

        Visit result = repository.findById(999);

        assertNull(result);
        verify(entityManager).find(Visit.class, 999);
    }

    @Test
    void testFindByIdWithPersistenceException() {
        doThrow(new PersistenceException("Test exception")).when(entityManager).find(Visit.class, 1);

        assertThrows(PersistenceException.class, () -> repository.findById(1));
        verify(entityManager).find(Visit.class, 1);
    }

    @Test
    void testFindAll() {
        List<Visit> visits = List.of(
            createTestVisit(1, "Routine checkup"),
            createTestVisit(2, "Vaccination")
        );

        Query query = mock(Query.class);
        when(entityManager.createQuery("SELECT v FROM Visit v")).thenReturn(query);
        when(query.getResultList()).thenReturn(visits);

        Collection<Visit> result = repository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(entityManager).createQuery("SELECT v FROM Visit v");
        verify(query).getResultList();
    }

    @Test
    void testFindAllWithEmptyResult() {
        when(entityManager.createQuery(anyString())).thenReturn(mock(Query.class));
        when(entityManager.createQuery(anyString()).getResultList()).thenReturn(Collections.emptyList());

        Collection<Visit> result = repository.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(entityManager).createQuery("SELECT v FROM Visit v");
    }

    @Test
    void testFindAllWithPersistenceException() {
        when(entityManager.createQuery(anyString())).thenReturn(mock(Query.class));
        when(entityManager.createQuery(anyString()).getResultList()).thenThrow(new PersistenceException("Test exception"));

        assertThrows(PersistenceException.class, () -> repository.findAll());
        verify(entityManager).createQuery("SELECT v FROM Visit v");
    }

    @Test
    void testDeleteExistingVisit() {
        Visit visit = createTestVisit(1, "Routine checkup");

        when(entityManager.contains(visit)).thenReturn(true);

        repository.delete(visit);

        verify(entityManager).remove(visit);
        verify(entityManager, never()).merge(any());
    }

    @Test
    void testDeleteNonExistingVisit() {
        Visit visit = new Visit();
        visit.setId(1);
        visit.setDescription("Routine checkup");

        when(entityManager.contains(visit)).thenReturn(false);
        when(entityManager.merge(visit)).thenReturn(visit);

        repository.delete(visit);

        verify(entityManager).merge(visit);
        verify(entityManager).remove(visit);
    }

    @Test
    void testDeleteWithPersistenceException() {
        Visit visit = createTestVisit(1, "Routine checkup");

        when(entityManager.contains(visit)).thenReturn(true);
        doThrow(new PersistenceException("Test exception")).when(entityManager).remove(any());

        assertThrows(PersistenceException.class, () -> repository.delete(visit));
    }

    private Visit createTestVisit(int id, String description) {
        Visit visit = new Visit();
        visit.setId(id);
        visit.setDescription(description);
        return visit;
    }
}