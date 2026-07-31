package com.demo.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.demo.dto.VisitDto;
import com.demo.mapper.VisitMapper;
import com.demo.model.Visit;
import com.demo.service.ClinicService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;

class VisitRestControllerTest {

    @Mock
    private ClinicService clinicService;

    @Mock
    private VisitMapper visitMapper;

    @Mock
    private UriInfo uriInfo;

    private VisitRestController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new VisitRestController(clinicService, visitMapper);
    }

    @Test
    void testGetAllVisitDtosWithExistingVisits() {
        List<Visit> visits = Arrays.asList(createTestVisit(1), createTestVisit(2));
        List<VisitDto> visitDtos = Arrays.asList(createTestVisitDto(1), createTestVisitDto(2));

        when(clinicService.findAllVisits()).thenReturn(visits);
        when(visitMapper.toVisitsDto(visits)).thenReturn(visitDtos);

        Response response = controller.getAllVisitDtos();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findAllVisits();
        verify(visitMapper).toVisitsDto(visits);
    }

    @Test
    void testGetAllVisitDtosWithNoVisits() {
        when(clinicService.findAllVisits()).thenReturn(Collections.emptyList());

        Response response = controller.getAllVisitDtos();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findAllVisits();
    }

    @Test
    void testGetVisitDtoWithExistingVisit() {
        Visit visit = createTestVisit(1);
        VisitDto visitDto = createTestVisitDto(1);

        when(clinicService.findVisitById(1)).thenReturn(visit);
        when(visitMapper.toVisitDto(visit)).thenReturn(visitDto);

        Response response = controller.getVisitDto(1);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findVisitById(1);
        verify(visitMapper).toVisitDto(visit);
    }

    @Test
    void testGetVisitDtoWithNonExistingVisit() {
        when(clinicService.findVisitById(999)).thenReturn(null);

        Response response = controller.getVisitDto(999);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findVisitById(999);
    }

    @Test
    void testAddVisitWithValidVisitDto() {
        VisitDto visitDto = createTestVisitDto(1);
        Visit visit = createTestVisit(1);
        VisitDto responseDto = createTestVisitDto(1);

        when(visitMapper.toVisit(visitDto)).thenReturn(visit);
        doNothing().when(clinicService).saveVisit(visit);
        when(visitMapper.toVisitDto(visit)).thenReturn(responseDto);
        when(uriInfo.getAbsolutePathBuilder()).thenReturn(mock(jakarta.ws.rs.core.UriBuilder.class));
        when(uriInfo.getAbsolutePathBuilder().path(anyString())).thenReturn(mock(jakarta.ws.rs.core.UriBuilder.class));
        when(uriInfo.getAbsolutePathBuilder().path(anyString()).resolveTemplate(anyString(), any())).thenReturn(mock(jakarta.ws.rs.core.UriBuilder.class));
        when(uriInfo.getAbsolutePathBuilder().path(anyString()).resolveTemplate(anyString(), any()).build()).thenReturn(URI.create("http://localhost/api/visits/1"));

        Response response = controller.addVisit(visitDto, uriInfo);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        verify(clinicService).saveVisit(visit);
        verify(visitMapper).toVisit(visitDto);
        verify(visitMapper).toVisitDto(visit);
    }

    @Test
    void testAddVisitWithNullVisitDto() {
        Response response = controller.addVisit(null, uriInfo);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void testUpdateVisitWithValidVisitDto() {
        VisitDto visitDto = createTestVisitDto(1);
        Visit currentVisit = createTestVisit(1);
        VisitDto responseDto = createTestVisitDto(1);

        when(clinicService.findVisitById(1)).thenReturn(currentVisit);
        doNothing().when(clinicService).saveVisit(currentVisit);
        when(visitMapper.toVisitDto(currentVisit)).thenReturn(responseDto);

        Response response = controller.updateVisit(1, visitDto);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(clinicService).findVisitById(1);
        verify(clinicService).saveVisit(currentVisit);
    }

    @Test
    void testUpdateVisitWithNonExistingVisit() {
        VisitDto visitDto = createTestVisitDto(1);

        when(clinicService.findVisitById(999)).thenReturn(null);

        Response response = controller.updateVisit(999, visitDto);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findVisitById(999);
    }

    @Test
    void testUpdateVisitWithNullVisitDto() {
        Response response = controller.updateVisit(1, null);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void testDeleteVisitWithExistingVisit() {
        Visit visit = createTestVisit(1);

        when(clinicService.findVisitById(1)).thenReturn(visit);

        Response response = controller.deleteVisit(1);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(clinicService).findVisitById(1);
        verify(clinicService).deleteVisit(visit);
    }

    @Test
    void testDeleteVisitWithNonExistingVisit() {
        when(clinicService.findVisitById(999)).thenReturn(null);

        Response response = controller.deleteVisit(999);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clinicService).findVisitById(999);
        verify(clinicService, never()).deleteVisit(any());
    }

    private Visit createTestVisit(int id) {
        Visit visit = new Visit();
        visit.setId(id);
        visit.setDescription("Annual checkup");
        visit.setDate(LocalDate.now());
        return visit;
    }

    private VisitDto createTestVisitDto(int id) {
        VisitDto dto = new VisitDto();
        dto.setId(id);
        dto.setDescription("Annual checkup");
        dto.setDate(LocalDate.now());
        return dto;
    }
}