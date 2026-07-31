package com.demo.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * Messages describing a validation error.
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-31T09:31:57.203304122Z[GMT]")
public class ValidationMessageDto extends HashMap<String, Object>  {
  @JsonProperty("message")
  private String message;

  public ValidationMessageDto message(String message) {
    this.message = message;
    return this;
  }

  /**
   * The valiation message.
   * @return message
   */
  @NotNull

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidationMessageDto validationMessage = (ValidationMessageDto) o;
    return Objects.equals(this.message, validationMessage.message) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidationMessageDto {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

