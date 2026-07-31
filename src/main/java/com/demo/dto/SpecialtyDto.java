package com.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Fields of specialty of vets.
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-31T09:31:57.203304122Z[GMT]")
public record SpecialtyDto(
    @JsonProperty("id")
    @Min(0)
    Integer id,
    
    @JsonProperty("name")
    @NotNull
    @Size(min=1,max=80) 
    String name
) {
}