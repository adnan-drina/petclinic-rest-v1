package com.demo.mapper;

import org.mapstruct.Mapper;
import com.demo.dto.PetDto;
import com.demo.dto.PetTypeDto;
import com.demo.model.Pet;
import com.demo.model.PetType;

import java.util.Collection;

/**
 * Map Pet & PetDto using mapstruct
 */
@Mapper(componentModel = "jakarta-cdi")
public interface PetMapper {
    PetDto toPetDto(Pet pet);

    Collection<PetDto> toPetsDto(Collection<Pet> pets);

    Collection<Pet> toPets(Collection<PetDto> pets);

    Pet toPet(PetDto petDto);

    PetTypeDto toPetTypeDto(PetType petType);

    PetType toPetType(PetTypeDto petTypeDto);

    Collection<PetTypeDto> toPetTypeDtos(Collection<PetType> petTypes);
}
