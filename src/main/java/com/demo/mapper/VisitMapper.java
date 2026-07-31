package com.demo.mapper;

import org.mapstruct.Mapper;
import com.demo.dto.VisitDto;
import com.demo.model.Visit;

import java.util.Collection;

/**
 * Map Visit & VisitDto using mapstruct
 */
@Mapper(componentModel = "jakarta-cdi", uses = PetMapper.class)
public interface VisitMapper {
    Visit toVisit(VisitDto visitDto);

    VisitDto toVisitDto(Visit visit);

    Collection<VisitDto> toVisitsDto(Collection<Visit> visits);

}
