package com.demo.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.demo.dto.SpecialtyDto;
import com.demo.mapper.SpecialtyMapper;
import com.demo.model.Specialty;
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
import java.net.URI;

class SpecialtyRestControllerTest {

    @Mock
    private ClinicService clinicService;

    @Mock
    private SpecialtyMapper specialtyMapper;

    @Mock
    private UriInfo uriInfo;

    private SpecialtyRestController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new SpecialtyRestController(clinicService, specialtyMapper);
    }

    @Test
    void testGetAllSpecialtysWithExistingSpecialtys() {
        List<Specialty> specialties = Arrays.asList(createTestSpecialty(1), createTestSpecialty(2));
        List<SpecialtyDto> specialtyDtos = Arrays.asList(createTestSpecialtyDto(1), createTestSpecialtyDto(2));

        when(clinicService.findAllSpecialties()).thenReturn(specialties);
        when(specialtyMapper.toSpecialtyDtos(specialties)).thenReturn(specialtyDtos);

        Response response = controller.getAllSpecialtys();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findAllSpecialties();
        verify(specialtyMapper).toSpecialtyDtos(specialties);
    }

    @Test
    void testGetAllSpecialtysWithNoSpecialtys() {
        when(clinicService.findAllSpecialties()).thenReturn(Collections.emptyList());

        Response response = controller.getAllSpecialtys();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findAllSpecialties();
    }

    @Test
    void testGetSpecialtyWithExistingSpecialty() {
        Specialty specialty = createTestSpecialty(1);
        SpecialtyDto specialtyDto = createTestSpecialtyDto(1);

        when(clinicService.findSpecialtyById(1)).thenReturn(specialty);
        when(specialtyMapper.toSpecialtyDto(specialty)).thenReturn(specialtyDto);

        Response response = controller.getSpecialty(1);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findSpecialtyById(1);
        verify(specialtyMapper).toSpecialtyDto(specialty);
    }

    @Test
    void testGetSpecialtyWithNonExistingSpecialty() {
        when(clinicService.findSpecialtyById(999)).thenReturn(null);

        Response response = controller.getSpecialty(999);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findSpecialtyById(999);
    }

    @Test
    void testAddSpecialtyWithValidSpecialtyDto() {
        SpecialtyDto specialtyDto = createTestSpecialtyDto(1);
        Specialty specialty = createTestSpecialty(1);

        when(specialtyMapper.toSpecialty(specialtyDto)).thenReturn(specialty);
        doNothing().when(clinicService).saveSpecialty(specialty);
        when(specialtyMapper.toSpecialtyDto(specialty)).thenReturn(specialtyDto);
        
        var uriBuilder = mock(jakarta.ws.rs.core.UriBuilder.class);
        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.resolveTemplate(anyString(), any())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(URI.create("http://localhost/api/specialties/1"));

        Response response = controller.addSpecialty(specialtyDto, uriInfo);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        verify(clinicService).saveSpecialty(specialty);
        verify(specialtyMapper).toSpecialty(specialtyDto);
        verify(specialtyMapper).toSpecialtyDto(specialty);
    }

    @Test
    void testAddSpecialtyWithNullSpecialtyDto() {
        Response response = controller.addSpecialty(null, uriInfo);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void testUpdateSpecialtyWithValidSpecialtyDto() {
        SpecialtyDto specialtyDto = createTestSpecialtyDto(1);
        Specialty currentSpecialty = createTestSpecialty(1);

        when(clinicService.findSpecialtyById(1)).thenReturn(currentSpecialty);
        doNothing().when(clinicService).saveSpecialty(currentSpecialty);
        when(specialtyMapper.toSpecialtyDto(currentSpecialty)).thenReturn(specialtyDto);

        Response response = controller.updateSpecialty(1, specialtyDto);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findSpecialtyById(1);
        verify(clinicService).saveSpecialty(currentSpecialty);
        assertEquals("Cardiology", specialtyDto.name());
    }

    @Test
    void testUpdateSpecialtyWithNonExistingSpecialty() {
        SpecialtyDto specialtyDto = createTestSpecialtyDto(1);

        when(clinicService.findSpecialtyById(999)).thenReturn(null);

        Response response = controller.updateSpecialty(999, specialtyDto);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findSpecialtyById(999);
    }

    @Test
    void testUpdateSpecialtyWithNullSpecialtyDto() {
        Response response = controller.updateSpecialty(1, null);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void testDeleteSpecialtyWithExistingSpecialty() {
        Specialty specialty = createTestSpecialty(1);

        when(clinicService.findSpecialtyById(1)).thenReturn(specialty);

        Response response = controller.deleteSpecialty(1);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(clinicService).findSpecialtyById(1);
        verify(clinicService).deleteSpecialty(specialty);
    }

    @Test
    void testDeleteSpecialtyWithNonExistingSpecialty() {
        when(clinicService.findSpecialtyById(999)).thenReturn(null);

        Response response = controller.deleteSpecialty(999);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findSpecialtyById(999);
        verify(clinicService, never()).deleteSpecialty(any());
    }

    private Specialty createTestSpecialty(int id) {
        Specialty specialty = new Specialty();
        specialty.setId(id);
        specialty.setName("Cardiology");
        return specialty;
    }

    private SpecialtyDto createTestSpecialtyDto(int id) {
        return new SpecialtyDto(id, "Cardiology");
    }
}