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

import com.demo.dto.PetDto;
import com.demo.mapper.PetMapper;
import com.demo.model.Pet;
import com.demo.service.ClinicService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.transaction.Transactional;

import java.net.URI;
import java.util.Collection;

/**
 * @author Vitaliy Fedoriv
 */

@ApplicationScoped
@Path("/api/pets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PetRestController {

    private final ClinicService clinicService;
    private final PetMapper petMapper;

    public PetRestController(ClinicService clinicService, PetMapper petMapper) {
        this.clinicService = clinicService;
        this.petMapper = petMapper;
    }

    @GET
    @Path("/{petId}")
    public Response getPet(@PathParam("petId") int petId) {
        PetDto pet = petMapper.toPetDto(this.clinicService.findPetById(petId));
        if (pet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(pet).build();
    }

    @GET
    public Response getPets() {
        Collection<PetDto> pets = petMapper.toPetsDto(this.clinicService.findAllPets());
        if (pets.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(pets).build();
    }

    @GET
    @Path("/pettypes")
    public Response getPetTypes() {
        return Response.ok(petMapper.toPetTypeDtos(this.clinicService.findPetTypes())).build();
    }

    @POST
    public Response addPet(@Valid PetDto petDto, UriInfo uriInfo) {
        if (petDto == null) {
            BindingErrorsResponse errors = new BindingErrorsResponse();
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("errors", errors.toJSON())
                    .build();
        }
        
        Pet pet = petMapper.toPet(petDto);
        this.clinicService.savePet(pet);
        petDto.setId(pet.getId());
        
        URI location = uriInfo.getAbsolutePathBuilder()
                .path("/api/pets/{id}")
                .resolveTemplate("id", pet.getId())
                .build();
        return Response.created(location).entity(petDto).build();
    }

    @PUT
    @Path("/{petId}")
    public Response updatePet(@PathParam("petId") int petId, @Valid PetDto pet) {
        if (pet == null) {
            BindingErrorsResponse errors = new BindingErrorsResponse();
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("errors", errors.toJSON())
                    .build();
        }
        
        Pet currentPet = this.clinicService.findPetById(petId);
        if (currentPet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        currentPet.setBirthDate(pet.getBirthDate());
        currentPet.setName(pet.getName());
        currentPet.setType(petMapper.toPetType(pet.getType()));
        this.clinicService.savePet(currentPet);
        return Response.ok(petMapper.toPetDto(currentPet)).build();
    }

    @DELETE
    @Path("/{petId}")
    @Transactional
    public Response deletePet(@PathParam("petId") int petId) {
        Pet pet = this.clinicService.findPetById(petId);
        if (pet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.clinicService.deletePet(pet);
        return Response.noContent().build();
    }
}