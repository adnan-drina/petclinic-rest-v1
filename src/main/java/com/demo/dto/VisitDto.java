package com.demo.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * A booking for a vet visit.
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-31T09:31:57.203304122Z[GMT]")
public class VisitDto   {
  @JsonProperty("date")
    private LocalDate date;

  @JsonProperty("description")
  private String description;

  @JsonProperty("id")
  private Integer id;

  public VisitDto date(LocalDate date) {
    this.date = date;
    return this;
  }

  /**
   * The date of the visit.
   * @return date
  */
  
  @Valid

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public VisitDto description(String description) {
    this.description = description;
    return this;
  }

  /**
   * The description for the visit.
   * @return description
  */
    @NotNull

@Size(min=1,max=255) 
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public VisitDto id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * The ID of the visit.
   * minimum: 0
   * @return id
  */
    @NotNull

@Min(0)
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VisitDto visit = (VisitDto) o;
    return Objects.equals(this.date, visit.date) &&
        Objects.equals(this.description, visit.description) &&
        Objects.equals(this.id, visit.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, description, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VisitDto {\n");
    
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

