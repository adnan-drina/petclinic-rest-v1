package com.demo.dto;

/**
 * Base DTO class with common functionality for all DTOs.
 */
public abstract class BaseDto {
    
    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    protected String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}