package com.demo.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationMessageDtoTest {

    @Test
    void messageRoundTrip() {
        var dto = new ValidationMessageDto();
        assertThat(dto.getMessage()).isNull();
        dto.setMessage("must not be null");
        assertThat(dto.getMessage()).isEqualTo("must not be null");
        assertThat(dto.message("other").getMessage()).isEqualTo("other");
    }

    @Test
    void equalsAndHashCode() {
        ValidationMessageDto a = new ValidationMessageDto().message("x");
        ValidationMessageDto b = new ValidationMessageDto().message("x");
        ValidationMessageDto c = new ValidationMessageDto().message("y");
        assertThat(a).isEqualTo(b).isNotEqualTo(c);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toStringContainsMessage() {
        assertThat(new ValidationMessageDto().message("boom").toString()).contains("boom");
    }
}
