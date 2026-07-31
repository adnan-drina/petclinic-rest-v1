package com.demo.mapper;

import org.mapstruct.Mapper;
import com.demo.dto.VetDto;
import com.demo.model.Vet;

import java.util.Collection;

/**
 * Map Vet & VetoDto using mapstruct
 */
@Mapper(componentModel = "jakarta-cdi", uses = SpecialtyMapper.class)
public interface VetMapper {
    Vet toVet(VetDto vetDto);

    VetDto toVetDto(Vet vet);

    Collection<VetDto> toVetDtos(Collection<Vet> vets);
}
