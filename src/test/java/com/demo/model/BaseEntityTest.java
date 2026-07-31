package com.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BaseEntityTest {

    static class TestEntity extends BaseEntity {
    }

    private TestEntity entity;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        entity = new TestEntity();
        mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Nested
    @DisplayName("getId and setId")
    class IdAccessors {

        @Test
        @DisplayName("id is null by default")
        void idIsNullByDefault() {
            assertThat(entity.getId()).isNull();
        }

        @Test
        @DisplayName("setId sets the id")
        void setIdSetsId() {
            entity.setId(42);
            assertThat(entity.getId()).isEqualTo(42);
        }

        @Test
        @DisplayName("setId overwrites previous id")
        void setIdOverwritesPreviousId() {
            entity.setId(1);
            entity.setId(2);
            assertThat(entity.getId()).isEqualTo(2);
        }

        @Test
        @DisplayName("setId to null resets id")
        void setIdToNull() {
            entity.setId(10);
            entity.setId(null);
            assertThat(entity.getId()).isNull();
        }
    }

    @Nested
    @DisplayName("isNew")
    class IsNewBehavior {

        @Test
        @DisplayName("returns true when id is null")
        void isNewWhenIdIsNull() {
            assertThat(entity.isNew()).isTrue();
        }

        @Test
        @DisplayName("returns false when id is not null")
        void isNotNewWhenIdIsSet() {
            entity.setId(1);
            assertThat(entity.isNew()).isFalse();
        }

        @Test
        @DisplayName("returns false when id is zero")
        void isNotNewWhenIdIsZero() {
            entity.setId(0);
            assertThat(entity.isNew()).isFalse();
        }

        @Test
        @DisplayName("returns true after id is reset to null")
        void isNewAfterIdReset() {
            entity.setId(5);
            assertThat(entity.isNew()).isFalse();
            entity.setId(null);
            assertThat(entity.isNew()).isTrue();
        }
    }

    @Nested
    @DisplayName("JsonIgnore on isNew")
    class JsonIgnoreOnIsNew {

        @Test
        @DisplayName("isNew method is annotated with @JsonIgnore")
        void isNewHasJsonIgnoreAnnotation() throws NoSuchMethodException {
            var method = BaseEntity.class.getMethod("isNew");
            assertThat(method.getAnnotation(JsonIgnore.class)).isNotNull();
        }

        @Test
        @DisplayName("serialized JSON does not contain isNew field")
        void serializesWithoutIsNew() throws Exception {
            entity.setId(7);
            var json = mapper.writeValueAsString(entity);
            assertThat(json).doesNotContain("isNew");
        }

        @Test
        @DisplayName("serialized JSON contains id field")
        void serializesWithId() throws Exception {
            entity.setId(7);
            var json = mapper.writeValueAsString(entity);
            assertThat(json).contains("\"id\"");
            assertThat(json).contains("7");
        }

        @Test
        @DisplayName("deserialized entity has correct id")
        void deserializesId() throws Exception {
            var json = "{\"id\":99}";
            var deserialized = mapper.readValue(json, TestEntity.class);
            assertThat(deserialized.getId()).isEqualTo(99);
            assertThat(deserialized.isNew()).isFalse();
        }

        @Test
        @DisplayName("deserialized entity with null id is new")
        void deserializesNullIdAsNew() throws Exception {
            var json = "{}";
            var deserialized = mapper.readValue(json, TestEntity.class);
            assertThat(deserialized.getId()).isNull();
            assertThat(deserialized.isNew()).isTrue();
        }
    }
}
