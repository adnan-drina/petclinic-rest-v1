package com.demo.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.demo.dto.VetDto;
import com.demo.mapper.SpecialtyMapper;
import com.demo.mapper.VetMapper;
import com.demo.model.Specialty;
import com.demo.model.Vet;
import com.demo.service.ClinicService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.net.URI;

class VetRestControllerTest {

    @Mock
    private ClinicService clinicService;

    @Mock
    private VetMapper vetMapper;

    @Mock
    private SpecialtyMapper specialtyMapper;

    @Mock
    private UriInfo uriInfo;

    private VetRestController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new VetRestController(clinicService, vetMapper, specialtyMapper);
    }

    @Test
    void testGetAllVetsWithExistingVets() {
        List<Vet> vets = Arrays.asList(createTestVet(1), createTestVet(2));
        List<VetDto> vetDtos = Arrays.asList(createTestVetDto(1), createTestVetDto(2));

        when(clinicService.findAllVets()).thenReturn(vets);
        when(vetMapper.toVetDtos(vets)).thenReturn(vetDtos);

        Response response = controller.getAllVets();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findAllVets();
        verify(vetMapper).toVetDtos(vets);
    }

    @Test
    void testGetAllVetsWithNoVets() {
        when(clinicService.findAllVets()).thenReturn(Collections.emptyList());

        Response response = controller.getAllVets();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findAllVets();
    }

    @Test
    void testGetVetWithExistingVet() {
        Vet vet = createTestVet(1);
        VetDto vetDto = createTestVetDto(1);

        when(clinicService.findVetById(1)).thenReturn(vet);
        when(vetMapper.toVetDto(vet)).thenReturn(vetDto);

        Response response = controller.getVet(1);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findVetById(1);
        verify(vetMapper).toVetDto(vet);
    }

    @Test
    void testGetVetWithNonExistingVet() {
        when(clinicService.findVetById(999)).thenReturn(null);

        Response response = controller.getVet(999);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findVetById(999);
    }

    @Test
    void testAddVetWithValidVetDto() {
        VetDto vetDto = createTestVetDto(1);
        Vet vet = createTestVet(1);

        when(vetMapper.toVet(vetDto)).thenReturn(vet);
        doNothing().when(clinicService).saveVet(vet);
        when(vetMapper.toVetDto(vet)).thenReturn(vetDto);
        
        var uriBuilder = mock(jakarta.ws.rs.core.UriBuilder.class);
        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.resolveTemplate(anyString(), any())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(URI.create("http://localhost/api/vets/1"));

        Response response = controller.addVet(vetDto, uriInfo);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        verify(clinicService).saveVet(vet);
        verify(vetMapper).toVet(vetDto);
        verify(vetMapper).toVetDto(vet);
    }

    @Test
    void testAddVetWithNullVetDto() {
        Response response = controller.addVet(null, uriInfo);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void testUpdateVetWithValidVetDto() {
        VetDto vetDto = createTestVetDto(1);
        Vet currentVet = createTestVet(1);

        when(clinicService.findVetById(1)).thenReturn(currentVet);
        doNothing().when(clinicService).saveVet(currentVet);
        when(vetMapper.toVetDto(currentVet)).thenReturn(vetDto);
        when(specialtyMapper.toSpecialtys(vetDto.specialties())).thenReturn(Collections.emptyList());

        Response response = controller.updateVet(1, vetDto);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findVetById(1);
        verify(clinicService).saveVet(currentVet);
    }

    @Test
    void testUpdateVetWithNonExistingVet() {
        VetDto vetDto = createTestVetDto(1);

        when(clinicService.findVetById(999)).thenReturn(null);

        Response response = controller.updateVet(999, vetDto);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findVetById(999);
    }

    @Test
    void testUpdateVetWithNullVetDto() {
        Response response = controller.updateVet(1, null);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void testDeleteVetWithExistingVet() {
        Vet vet = createTestVet(1);

        when(clinicService.findVetById(1)).thenReturn(vet);

        Response response = controller.deleteVet(1);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(clinicService).findVetById(1);
        verify(clinicService).deleteVet(vet);
    }

    @Test
    void testDeleteVetWithNonExistingVet() {
        when(clinicService.findVetById(999)).thenReturn(null);

        Response response = controller.deleteVet(999);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findVetById(999);
        verify(clinicService, never()).deleteVet(any());
    }

    private Vet createTestVet(int id) {
        Vet vet = new Vet();
        vet.setId(id);
        vet.setFirstName("John");
        vet.setLastName("Doe");
        return vet;
    }

    private VetDto createTestVetDto(int id) {
        return new VetDto(id, "John", "Doe", new ArrayList<>());
    }
}