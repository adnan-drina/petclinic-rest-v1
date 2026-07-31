package com.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * A pet type.
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-31T09:31:57.203304122Z[GMT]")
public record PetTypeDto(
    @JsonProperty("id")
    Integer id,
    
    @JsonProperty("name")
    @NotNull
    @Size(min=1,max=80) 
    String name
) {
    
    /**
     * The ID of the pet type.
     * minimum: 0
     * @return id
     */
    public @Min(0) Integer id() {
        return id;
    }
    
    /**
     * The name of the pet type.
     * @return name
     */
    public String name() {
        return name;
    }
}
