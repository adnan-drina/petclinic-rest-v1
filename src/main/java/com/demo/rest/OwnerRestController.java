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

import com.demo.dto.OwnerDto;
import com.demo.mapper.OwnerMapper;
import com.demo.model.Owner;
import com.demo.service.ClinicService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.Collection;

/**
 * @author Vitaliy Fedoriv
 */

@ApplicationScoped
@Path("/api/owners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OwnerRestController {

    private final ClinicService clinicService;
    private final OwnerMapper ownerMapper;

    public OwnerRestController(ClinicService clinicService, OwnerMapper ownerMapper) {
        this.clinicService = clinicService;
        this.ownerMapper = ownerMapper;
    }

    @GET
    @Path("/*/lastname/{lastName}")
    public Response getOwnersList(@PathParam("lastName") String ownerLastName) {
        if (ownerLastName == null) {
            ownerLastName = "";
        }
        Collection<Owner> owners = this.clinicService.findOwnerByLastName(ownerLastName);
        if (owners.isEmpty()) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(ownerMapper.toOwnerDtoCollection(owners)).build();
    }

    @GET
    public Response getOwners() {
        Collection<Owner> owners = this.clinicService.findAllOwners();
        if (owners.isEmpty()) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(ownerMapper.toOwnerDtoCollection(owners)).build();
    }

    @GET
    @Path("/{ownerId}")
    public Response getOwner(@PathParam("ownerId") int ownerId) {
        Owner owner = null;
        owner = this.clinicService.findOwnerById(ownerId);
        if (owner == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(ownerMapper.toOwnerDto(owner)).build();
    }

    @POST
    public Response addOwner(@Valid OwnerDto ownerDto, UriInfo uriInfo) {
        if (ownerDto.getId() != null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        Owner owner = ownerMapper.toOwner(ownerDto);
        this.clinicService.saveOwner(owner);
        ownerDto.setId(owner.getId());
        URI location = uriInfo.getAbsolutePathBuilder()
                .path("/api/owners/{id}")
                .resolveTemplate("id", owner.getId())
                .build();
        return Response.created(location).entity(ownerDto).build();
    }

    @PUT
    @Path("/{ownerId}")
    public Response updateOwner(@PathParam("ownerId") int ownerId, @Valid OwnerDto ownerDto) {
        boolean bodyIdMatchesPathId = ownerDto.getId() == null || ownerId == ownerDto.getId();
        if (!bodyIdMatchesPathId) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        Owner currentOwner = this.clinicService.findOwnerById(ownerId);
        if (currentOwner == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        currentOwner.setAddress(ownerDto.getAddress());
        currentOwner.setCity(ownerDto.getCity());
        currentOwner.setFirstName(ownerDto.getFirstName());
        currentOwner.setLastName(ownerDto.getLastName());
        currentOwner.setTelephone(ownerDto.getTelephone());
        this.clinicService.saveOwner(currentOwner);
        return Response.status(Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{ownerId}")
    public Response deleteOwner(@PathParam("ownerId") int ownerId) {
        Owner owner = this.clinicService.findOwnerById(ownerId);
        if (owner == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        this.clinicService.deleteOwner(owner);
        return Response.status(Status.NO_CONTENT).build();
    }

}
