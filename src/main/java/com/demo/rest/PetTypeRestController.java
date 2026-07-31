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

import com.demo.dto.PetTypeDto;
import com.demo.mapper.PetTypeMapper;
import com.demo.model.PetType;
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

@ApplicationScoped
@Path("/api/petTypes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PetTypeRestController {

    private final ClinicService clinicService;
    private final PetTypeMapper petTypeMapper;

    public PetTypeRestController(ClinicService clinicService, PetTypeMapper petTypeMapper) {
        this.clinicService = clinicService;
        this.petTypeMapper = petTypeMapper;
    }

    @GET
    public Response getAllPetTypes() {
        Collection<PetType> petTypes = new ArrayList<>();
        Collection<PetType> foundPetTypes = this.clinicService.findAllPetTypes();
        if (foundPetTypes != null) {
            petTypes.addAll(foundPetTypes);
        }
        if (petTypes.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(petTypeMapper.toPetTypeDtos(petTypes)).build();
    }

    @GET
    @Path("/{petTypeId}")
    public Response getPetType(@PathParam("petTypeId") int petTypeId) {
        PetType petType = this.clinicService.findPetTypeById(petTypeId);
        if (petType == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(petTypeMapper.toPetTypeDto(petType)).build();
    }

    @POST
    public Response addPetType(@Valid PetTypeDto petType, UriInfo uriInfo) {
        if (petType == null) {
            BindingErrorsResponse errors = new BindingErrorsResponse();
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("errors", errors.toJSON())
                    .build();
        }
        
        final PetType type = petTypeMapper.toPetType(petType);
        this.clinicService.savePetType(type);
        
        URI location = uriInfo.getAbsolutePathBuilder()
                .path("/api/petTypes/{id}")
                .resolveTemplate("id", type.getId())
                .build();
        return Response.created(location).entity(petTypeMapper.toPetTypeDto(type)).build();
    }

    @PUT
    @Path("/{petTypeId}")
    public Response updatePetType(@PathParam("petTypeId") int petTypeId, @Valid PetTypeDto petType) {
        if (petType == null) {
            BindingErrorsResponse errors = new BindingErrorsResponse();
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("errors", errors.toJSON())
                    .build();
        }
        
        PetType currentPetType = this.clinicService.findPetTypeById(petTypeId);
        if (currentPetType == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        currentPetType.setName(petType.name());
        this.clinicService.savePetType(currentPetType);
        return Response.ok(petTypeMapper.toPetTypeDto(currentPetType)).build();
    }

    @DELETE
    @Path("/{petTypeId}")
    @Transactional
    public Response deletePetType(@PathParam("petTypeId") int petTypeId) {
        PetType petType = this.clinicService.findPetTypeById(petTypeId);
        if (petType == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.clinicService.deletePetType(petType);
        return Response.noContent().build();
    }
}