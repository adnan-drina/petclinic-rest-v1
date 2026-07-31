package com.demo.mapper;

import org.mapstruct.Mapper;
import com.demo.dto.OwnerDto;
import com.demo.model.Owner;

import java.util.Collection;

/**
 * Maps Owner & OwnerDto using Mapstruct
 */
@Mapper(componentModel = "jakarta-cdi", uses = PetMapper.class)
public interface OwnerMapper {

    OwnerDto toOwnerDto(Owner owner);

    Owner toOwner(OwnerDto ownerDto);

    Collection<OwnerDto> toOwnerDtoCollection(Collection<Owner> ownerCollection);

    Collection<Owner> toOwners(Collection<OwnerDto> ownerDtos);
}
