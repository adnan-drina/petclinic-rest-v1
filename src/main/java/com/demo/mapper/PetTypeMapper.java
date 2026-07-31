package com.demo.mapper;

import org.mapstruct.Mapper;
import com.demo.dto.PetTypeDto;
import com.demo.model.PetType;

import java.util.Collection;

/**
 * Map PetType & PetTypeDto using mapstruct
 */
@Mapper(componentModel = "jakarta-cdi")
public interface PetTypeMapper {

    PetType toPetType(PetTypeDto petTypeDto);

    PetTypeDto toPetTypeDto(PetType petType);

    Collection<PetTypeDto> toPetTypeDtos(Collection<PetType> petTypes);
}
