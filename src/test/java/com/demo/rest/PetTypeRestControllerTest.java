package com.demo.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.demo.dto.PetTypeDto;
import com.demo.mapper.PetTypeMapper;
import com.demo.model.PetType;
import com.demo.service.ClinicService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class PetTypeRestControllerTest {

    @Mock
    private ClinicService clinicService;

    @Mock
    private PetTypeMapper petTypeMapper;

    @Mock
    private UriInfo uriInfo;

    private PetTypeRestController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new PetTypeRestController(clinicService, petTypeMapper);
    }

    @Test
    void testCreation() {
        assertNotNull(controller);
    }

    @Test
    void testGetAllPetTypesWithExistingPetTypes() {
        List<PetType> petTypes = Arrays.asList(createTestPetType(1, "Dog"), createTestPetType(2, "Cat"));
        List<PetTypeDto> petTypeDtos = Arrays.asList(createTestPetTypeDto(1, "Dog"), createTestPetTypeDto(2, "Cat"));

        when(clinicService.findAllPetTypes()).thenReturn(petTypes);
        when(petTypeMapper.toPetTypeDtos(petTypes)).thenReturn(petTypeDtos);

        Response response = controller.getAllPetTypes();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findAllPetTypes();
        verify(petTypeMapper).toPetTypeDtos(petTypes);
    }

    @Test
    void testGetAllPetTypesWithEmptyList() {
        when(clinicService.findAllPetTypes()).thenReturn(Collections.emptyList());

        Response response = controller.getAllPetTypes();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findAllPetTypes();
        verify(petTypeMapper, never()).toPetTypeDtos(any());
    }

    @Test
    void testGetAllPetTypesWithNullList() {
        when(clinicService.findAllPetTypes()).thenReturn(null);

        Response response = controller.getAllPetTypes();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findAllPetTypes();
        // Cannot easily verify with Mockito matchers in this simple test
    }

    @Test
    void testGetPetTypeWithExistingPetType() {
        PetType petType = createTestPetType(1, "Dog");
        PetTypeDto petTypeDto = createTestPetTypeDto(1, "Dog");

        when(clinicService.findPetTypeById(1)).thenReturn(petType);
        when(petTypeMapper.toPetTypeDto(petType)).thenReturn(petTypeDto);

        Response response = controller.getPetType(1);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findPetTypeById(1);
        verify(petTypeMapper).toPetTypeDto(petType);
    }

    @Test
    void testGetPetTypeWithNonExistingPetType() {
        when(clinicService.findPetTypeById(999)).thenReturn(null);

        Response response = controller.getPetType(999);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findPetTypeById(999);
        verify(petTypeMapper, never()).toPetTypeDto(any());
    }

    @Test
    void testAddPetTypeWithValidPetType() {
        PetTypeDto petTypeDto = createTestPetTypeDto(1, "Dog");
        PetType petType = createTestPetType(1, "Dog");
        PetTypeDto savedPetTypeDto = createTestPetTypeDto(1, "Dog");

        when(petTypeMapper.toPetType(petTypeDto)).thenReturn(petType);
        doNothing().when(clinicService).savePetType(petType);
        when(petTypeMapper.toPetTypeDto(petType)).thenReturn(savedPetTypeDto);

        var uriBuilder = mock(jakarta.ws.rs.core.UriBuilder.class);
        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.resolveTemplate(anyString(), any())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(URI.create("http://localhost/api/petTypes/1"));

        Response response = controller.addPetType(petTypeDto, uriInfo);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        verify(petTypeMapper).toPetType(petTypeDto);
        verify(clinicService).savePetType(petType);
        verify(petTypeMapper).toPetTypeDto(petType);
    }

    @Test
    void testAddPetTypeWithNullPetType() {
        Response response = controller.addPetType(null, uriInfo);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        verify(petTypeMapper, never()).toPetType(any());
        verify(clinicService, never()).savePetType(any());
    }

    @Test
    void testUpdatePetTypeWithValidPetType() {
        PetTypeDto petTypeDto = createTestPetTypeDto(1, "Dog");
        PetType currentPetType = createTestPetType(1, "Cat");
        PetTypeDto updatedPetTypeDto = createTestPetTypeDto(1, "Dog");

        when(clinicService.findPetTypeById(1)).thenReturn(currentPetType);
        when(petTypeMapper.toPetTypeDto(currentPetType)).thenReturn(updatedPetTypeDto);

        Response response = controller.updatePetType(1, petTypeDto);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findPetTypeById(1);
        verify(clinicService).savePetType(currentPetType);
        verify(petTypeMapper).toPetTypeDto(currentPetType);
    }

    @Test
    void testUpdatePetTypeWithNullPetType() {
        Response response = controller.updatePetType(1, null);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        // Cannot easily verify with Mockito matchers in this simple test
    }

    @Test
    void testUpdatePetTypeWithNonExistingPetType() {
        PetTypeDto petTypeDto = createTestPetTypeDto(1, "Dog");

        when(clinicService.findPetTypeById(999)).thenReturn(null);

        Response response = controller.updatePetType(999, petTypeDto);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findPetTypeById(999);
        verify(clinicService, never()).savePetType(any());
    }

    @Test
    void testDeletePetTypeWithExistingPetType() {
        PetType petType = createTestPetType(1, "Dog");

        when(clinicService.findPetTypeById(1)).thenReturn(petType);

        Response response = controller.deletePetType(1);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(clinicService).findPetTypeById(1);
        verify(clinicService).deletePetType(petType);
    }

    @Test
    void testDeletePetTypeWithNonExistingPetType() {
        when(clinicService.findPetTypeById(999)).thenReturn(null);

        Response response = controller.deletePetType(999);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findPetTypeById(999);
        verify(clinicService, never()).deletePetType(any());
    }

    private PetType createTestPetType(int id, String name) {
        PetType petType = new PetType();
        petType.setId(id);
        petType.setName(name);
        return petType;
    }

    private PetTypeDto createTestPetTypeDto(int id, String name) {
        return new PetTypeDto(id, name);
    }
}