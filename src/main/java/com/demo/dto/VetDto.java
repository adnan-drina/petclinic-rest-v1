package com.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * A veterinarian.
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-31T09:31:57.203304122Z[GMT]")
public record VetDto(
    @JsonProperty("id")
    @Min(0)
    Integer id,
    
    @JsonProperty("firstName")
    @NotNull
    @Pattern(regexp="^[a-zA-Z]*$") @Size(min=1,max=30) 
    String firstName,
    
    @JsonProperty("lastName")
    @NotNull
    @Pattern(regexp="^[a-zA-Z]*$") @Size(min=1,max=30) 
    String lastName,
    
    @JsonProperty("specialties")
    @NotNull
    @Valid
    List<SpecialtyDto> specialties
) {
    
    /**
     * The specialties of the vet.
     * @return specialties
     */
    public List<SpecialtyDto> specialties() {
        return specialties != null ? specialties : new ArrayList<>();
    }
    
    public VetDto addSpecialtiesItem(SpecialtyDto specialtiesItem) {
        List<SpecialtyDto> currentSpecialties = this.specialties != null ? this.specialties : new ArrayList<>();
        List<SpecialtyDto> newSpecialties = new ArrayList<>(currentSpecialties);
        newSpecialties.add(specialtiesItem);
        return new VetDto(id, firstName, lastName, newSpecialties);
    }
}