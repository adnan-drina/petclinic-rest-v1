package com.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.demo.model.Owner;
import com.demo.model.Pet;
import com.demo.model.PetType;
import com.demo.model.Specialty;
import com.demo.model.Vet;
import com.demo.model.Visit;
import com.demo.repository.OwnerRepository;
import com.demo.repository.PetRepository;
import com.demo.repository.PetTypeRepository;
import com.demo.repository.SpecialtyRepository;
import com.demo.repository.VetRepository;
import com.demo.repository.VisitRepository;

/**
 * Characterization of ClinicServiceImpl CDI wiring and repository delegation.
 * Repository save/delete are void — stub with doNothing, never given(save).willReturn.
 */
class ClinicServiceTest {

    @Mock PetRepository petRepository;
    @Mock VetRepository vetRepository;
    @Mock OwnerRepository ownerRepository;
    @Mock VisitRepository visitRepository;
    @Mock SpecialtyRepository specialtyRepository;
    @Mock PetTypeRepository petTypeRepository;

    ClinicService clinicService;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        clinicService = new ClinicServiceImpl(
            petRepository, vetRepository, ownerRepository,
            visitRepository, specialtyRepository, petTypeRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("findOwnerById delegates to OwnerRepository")
    void findOwnerByIdDelegates() {
        Owner owner = owner(1, "George", "Franklin");
        given(ownerRepository.findById(1)).willReturn(owner);

        assertThat(clinicService.findOwnerById(1)).isSameAs(owner);
        verify(ownerRepository).findById(1);
    }

    @Test
    @DisplayName("findOwnerByLastName delegates to OwnerRepository")
    void findOwnerByLastNameDelegates() {
        Collection<Owner> owners = List.of(owner(1, "George", "Franklin"));
        given(ownerRepository.findByLastName("Franklin")).willReturn(owners);

        assertThat(clinicService.findOwnerByLastName("Franklin")).containsExactlyElementsOf(owners);
        verify(ownerRepository).findByLastName("Franklin");
    }

    @Test
    @DisplayName("saveOwner / deleteOwner delegate void ops to OwnerRepository")
    void ownerMutationsDelegate() {
        Owner owner = owner(1, "George", "Franklin");
        doNothing().when(ownerRepository).save(any(Owner.class));
        doNothing().when(ownerRepository).delete(any(Owner.class));

        clinicService.saveOwner(owner);
        clinicService.deleteOwner(owner);

        verify(ownerRepository).save(owner);
        verify(ownerRepository).delete(owner);
    }

    @Test
    @DisplayName("pet find/save/delete delegate to PetRepository")
    void petCrudDelegates() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Leo");
        given(petRepository.findById(1)).willReturn(pet);
        doNothing().when(petRepository).save(any(Pet.class));
        doNothing().when(petRepository).delete(any(Pet.class));

        assertThat(clinicService.findPetById(1)).isSameAs(pet);
        clinicService.savePet(pet);
        clinicService.deletePet(pet);

        verify(petRepository).findById(1);
        verify(petRepository).save(pet);
        verify(petRepository).delete(pet);
    }

    @Test
    @DisplayName("findVets / saveVet / deleteVet delegate to VetRepository")
    void vetCrudDelegates() {
        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("James");
        vet.setLastName("Carter");
        given(vetRepository.findAll()).willReturn(List.of(vet));
        doNothing().when(vetRepository).save(any(Vet.class));
        doNothing().when(vetRepository).delete(any(Vet.class));

        assertThat(clinicService.findVets()).containsExactly(vet);
        clinicService.saveVet(vet);
        clinicService.deleteVet(vet);

        verify(vetRepository).findAll();
        verify(vetRepository).save(vet);
        verify(vetRepository).delete(vet);
    }

    @Test
    @DisplayName("findPetTypes uses PetRepository.findPetTypes (legacy facade quirk)")
    void findPetTypesViaPetRepository() {
        PetType type = new PetType();
        type.setId(1);
        type.setName("cat");
        given(petRepository.findPetTypes()).willReturn(List.of(type));

        assertThat(clinicService.findPetTypes()).containsExactly(type);
        verify(petRepository).findPetTypes();
    }

    @Test
    @DisplayName("visit and specialty aggregates delegate to matching repos")
    void visitAndSpecialtyDelegate() {
        Visit visit = new Visit();
        visit.setId(1);
        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("radiology");

        given(visitRepository.findById(1)).willReturn(visit);
        given(specialtyRepository.findById(1)).willReturn(specialty);
        given(specialtyRepository.findAll()).willReturn(List.of(specialty));
        doNothing().when(visitRepository).save(any(Visit.class));
        doNothing().when(visitRepository).delete(any(Visit.class));
        doNothing().when(specialtyRepository).save(any(Specialty.class));
        doNothing().when(specialtyRepository).delete(any(Specialty.class));

        assertThat(clinicService.findVisitById(1)).isSameAs(visit);
        assertThat(clinicService.findSpecialtyById(1)).isSameAs(specialty);
        assertThat(clinicService.findAllSpecialties()).containsExactly(specialty);

        clinicService.saveVisit(visit);
        clinicService.deleteVisit(visit);
        clinicService.saveSpecialty(specialty);
        clinicService.deleteSpecialty(specialty);

        verify(visitRepository).save(visit);
        verify(visitRepository).delete(visit);
        verify(specialtyRepository).save(specialty);
        verify(specialtyRepository).delete(specialty);
    }

    @Test
    @DisplayName("findOwnerById returns null when repository misses")
    void findOwnerByIdMiss() {
        given(ownerRepository.findById(anyInt())).willReturn(null);
        assertThat(clinicService.findOwnerById(99)).isNull();
    }

    private static Owner owner(int id, String first, String last) {
        Owner o = new Owner();
        o.setId(id);
        o.setFirstName(first);
        o.setLastName(last);
        return o;
    }
}
