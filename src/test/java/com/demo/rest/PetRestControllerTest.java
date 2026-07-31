package com.demo.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.demo.dto.PetDto;
import com.demo.dto.PetTypeDto;
import com.demo.mapper.PetMapper;
import com.demo.model.Pet;
import com.demo.model.PetType;
import com.demo.service.ClinicService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class PetRestControllerTest {

    @Mock
    private ClinicService clinicService;

    @Mock
    private PetMapper petMapper;

    @Mock
    private UriInfo uriInfo;

    private PetRestController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new PetRestController(clinicService, petMapper);
    }

    @Test
    void testGetPetWithExistingPet() {
        Pet pet = createTestPet(1);
        PetDto petDto = createTestPetDto(1);

        when(clinicService.findPetById(1)).thenReturn(pet);
        when(petMapper.toPetDto(pet)).thenReturn(petDto);

        Response response = controller.getPet(1);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findPetById(1);
        verify(petMapper).toPetDto(pet);
    }

    @Test
    void testGetPetWithNonExistingPet() {
        when(clinicService.findPetById(999)).thenReturn(null);

        Response response = controller.getPet(999);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findPetById(999);
    }

    @Test
    void testGetPetsWithExistingPets() {
        List<Pet> pets = Arrays.asList(createTestPet(1), createTestPet(2));
        List<PetDto> petDtos = Arrays.asList(createTestPetDto(1), createTestPetDto(2));

        when(clinicService.findAllPets()).thenReturn(pets);
        when(petMapper.toPetsDto(pets)).thenReturn(petDtos);

        Response response = controller.getPets();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findAllPets();
        verify(petMapper).toPetsDto(pets);
    }

    @Test
    void testGetPetsWithNoPets() {
        when(clinicService.findAllPets()).thenReturn(Collections.emptyList());

        Response response = controller.getPets();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findAllPets();
    }

    @Test
    void testGetPetTypes() {
        List<PetType> petTypes = Arrays.asList(createTestPetType(1), createTestPetType(2));
        List<PetTypeDto> petTypeDtos = Arrays.asList(createTestPetTypeDto(1), createTestPetTypeDto(2));

        when(clinicService.findPetTypes()).thenReturn(petTypes);
        when(petMapper.toPetTypeDtos(petTypes)).thenReturn(petTypeDtos);

        Response response = controller.getPetTypes();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findPetTypes();
        verify(petMapper).toPetTypeDtos(petTypes);
    }

    @Test
    void testAddPetWithNullPetDto() {
        Response response = controller.addPet(null, uriInfo);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void testUpdatePetWithValidPetDto() {
        PetDto petDto = createTestPetDto(1);
        Pet currentPet = createTestPet(1);
        PetDto responseDto = createTestPetDto(1);

        when(clinicService.findPetById(1)).thenReturn(currentPet);
        doNothing().when(clinicService).savePet(currentPet);
        when(petMapper.toPetDto(currentPet)).thenReturn(responseDto);
        when(petMapper.toPetType(petDto.getType())).thenReturn(createTestPetType(1));

        Response response = controller.updatePet(1, petDto);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findPetById(1);
        verify(clinicService).savePet(currentPet);
    }

    @Test
    void testUpdatePetWithNonExistingPet() {
        PetDto petDto = createTestPetDto(1);

        when(clinicService.findPetById(999)).thenReturn(null);

        Response response = controller.updatePet(999, petDto);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findPetById(999);
    }

    @Test
    void testUpdatePetWithNullPetDto() {
        Response response = controller.updatePet(1, null);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void testDeletePetWithExistingPet() {
        Pet pet = createTestPet(1);

        when(clinicService.findPetById(1)).thenReturn(pet);

        Response response = controller.deletePet(1);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(clinicService).findPetById(1);
        verify(clinicService).deletePet(pet);
    }

    @Test
    void testDeletePetWithNonExistingPet() {
        when(clinicService.findPetById(999)).thenReturn(null);

        Response response = controller.deletePet(999);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findPetById(999);
        verify(clinicService, never()).deletePet(any());
    }

    private Pet createTestPet(int id) {
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName("Fluffy");
        return pet;
    }

    private PetDto createTestPetDto(int id) {
        PetDto dto = new PetDto();
        dto.setId(id);
        dto.setName("Fluffy");
        return dto;
    }

    private PetType createTestPetType(int id) {
        PetType petType = new PetType();
        petType.setId(id);
        petType.setName("Dog");
        return petType;
    }

    private PetTypeDto createTestPetTypeDto(int id) {
        return new PetTypeDto(id, "Dog");
    }
}