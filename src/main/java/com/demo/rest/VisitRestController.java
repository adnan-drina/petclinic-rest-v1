/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demo.rest;

import com.demo.dto.VisitDto;
import com.demo.mapper.VisitMapper;
import com.demo.model.Visit;
import com.demo.service.ClinicService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.transaction.Transactional;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Vitaliy Fedoriv
 */

@ApplicationScoped
@Path("/api/visits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VisitRestController {

    private final ClinicService clinicService;
    private final VisitMapper visitMapper;

    public VisitRestController(ClinicService clinicService, VisitMapper visitMapper) {
        this.clinicService = clinicService;
        this.visitMapper = visitMapper;
    }

    @GET
    public Response getAllVisitDtos() {
        Collection<Visit> visits = new ArrayList<>();
        visits.addAll(this.clinicService.findAllVisits());
        if (visits.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(visitMapper.toVisitsDto(visits)).build();
    }

    @GET
    @Path("/{visitId}")
    public Response getVisitDto(@PathParam("visitId") int visitId) {
        Visit visit = this.clinicService.findVisitById(visitId);
        if (visit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(visitMapper.toVisitDto(visit)).build();
    }

    @POST
    public Response addVisit(@Valid VisitDto visitDto, UriInfo uriInfo) {
        if (visitDto == null) {
            BindingErrorsResponse errors = new BindingErrorsResponse();
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("errors", errors.toJSON())
                    .build();
        }
        
        Visit visit = visitMapper.toVisit(visitDto);
        this.clinicService.saveVisit(visit);
        visitDto = visitMapper.toVisitDto(visit);
        
        URI location = uriInfo.getAbsolutePathBuilder()
                .path("/api/visits/{id}")
                .resolveTemplate("id", visit.getId())
                .build();
        return Response.created(location).entity(visitDto).build();
    }

    @PUT
    @Path("/{visitId}")
    public Response updateVisit(@PathParam("visitId") int visitId, @Valid VisitDto visitDto) {
        if (visitDto == null) {
            BindingErrorsResponse errors = new BindingErrorsResponse();
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("errors", errors.toJSON())
                    .build();
        }
        
        Visit currentVisit = this.clinicService.findVisitById(visitId);
        if (currentVisit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        currentVisit.setDate(visitDto.getDate());
        currentVisit.setDescription(visitDto.getDescription());
        this.clinicService.saveVisit(currentVisit);
        return Response.ok(visitMapper.toVisitDto(currentVisit)).build();
    }

    @DELETE
    @Path("/{visitId}")
    @Transactional
    public Response deleteVisit(@PathParam("visitId") int visitId) {
        Visit visit = this.clinicService.findVisitById(visitId);
        if (visit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.clinicService.deleteVisit(visit);
        return Response.noContent().build();
    }
}