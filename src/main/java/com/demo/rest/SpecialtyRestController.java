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

import com.demo.dto.SpecialtyDto;
import com.demo.mapper.SpecialtyMapper;
import com.demo.model.Specialty;
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
@Path("/api/specialties")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SpecialtyRestController {

    private final ClinicService clinicService;
    private final SpecialtyMapper specialtyMapper;

    public SpecialtyRestController(ClinicService clinicService, SpecialtyMapper specialtyMapper) {
        this.clinicService = clinicService;
        this.specialtyMapper = specialtyMapper;
    }

    @GET
    public Response getAllSpecialtys() {
        Collection<SpecialtyDto> specialties = new ArrayList<>();
        specialties.addAll(specialtyMapper.toSpecialtyDtos(this.clinicService.findAllSpecialties()));
        if (specialties.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(specialties).build();
    }

    @GET
    @Path("/{specialtyId}")
    public Response getSpecialty(@PathParam("specialtyId") int specialtyId) {
        Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
        if (specialty == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(specialtyMapper.toSpecialtyDto(specialty)).build();
    }

    @POST
    public Response addSpecialty(@Valid SpecialtyDto specialtyDto, UriInfo uriInfo) {
        if (specialtyDto == null) {
            BindingErrorsResponse errors = new BindingErrorsResponse();
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("errors", errors.toJSON())
                    .build();
        }
        
        Specialty specialty = specialtyMapper.toSpecialty(specialtyDto);
        this.clinicService.saveSpecialty(specialty);
        
        URI location = uriInfo.getAbsolutePathBuilder()
                .path("/api/specialties/{id}")
                .resolveTemplate("id", specialty.getId())
                .build();
        return Response.created(location).entity(specialtyMapper.toSpecialtyDto(specialty)).build();
    }

    @PUT
    @Path("/{specialtyId}")
    public Response updateSpecialty(@PathParam("specialtyId") int specialtyId, @Valid SpecialtyDto specialtyDto) {
        if (specialtyDto == null) {
            BindingErrorsResponse errors = new BindingErrorsResponse();
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("errors", errors.toJSON())
                    .build();
        }
        
        Specialty currentSpecialty = this.clinicService.findSpecialtyById(specialtyId);
        if (currentSpecialty == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        currentSpecialty.setName(specialtyDto.getName());
        this.clinicService.saveSpecialty(currentSpecialty);
        return Response.ok(specialtyMapper.toSpecialtyDto(currentSpecialty)).build();
    }

    @DELETE
    @Path("/{specialtyId}")
    @Transactional
    public Response deleteSpecialty(@PathParam("specialtyId") int specialtyId) {
        Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
        if (specialty == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.clinicService.deleteSpecialty(specialty);
        return Response.noContent().build();
    }
}