package com.demo.rest;

import com.demo.rest.BindingErrorsResponse.BindingError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BindingErrorsResponseTest {

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("Constructors")
    class ConstructorTests {

        @Test
        @DisplayName("default constructor creates empty response")
        void defaultConstructorCreatesEmptyResponse() {
            var response = new BindingErrorsResponse();
            var json = response.toJSON();
            var node = parseJson(json);
            assertThat(node.isArray()).isTrue();
            assertThat(node.size()).isZero();
        }

        @Test
        @DisplayName("single ID constructor delegates to two-arg and adds bodyId error")
        void singleIdConstructorAddsBodyIdError() {
            var response = new BindingErrorsResponse(5);
            var json = response.toJSON();
            var node = parseJson(json);
            assertThat(node.isArray()).isTrue();
            assertThat(node.size()).isEqualTo(1);
            assertThat(node.get(0).get("objectName").asText()).isEqualTo("body");
            assertThat(node.get(0).get("fieldName").asText()).isEqualTo("id");
            assertThat(node.get(0).get("fieldValue").asText()).isEqualTo("5");
            assertThat(node.get(0).get("errorMessage").asText()).isEqualTo("must not be specified");
        }

        @Test
        @DisplayName("two-arg constructor with only bodyId adds error")
        void twoArgConstructorWithOnlyBodyId() {
            var response = new BindingErrorsResponse(null, 5);
            var json = response.toJSON();
            var node = parseJson(json);
            assertThat(node.size()).isEqualTo(1);
            assertThat(node.get(0).get("objectName").asText()).isEqualTo("body");
            assertThat(node.get(0).get("fieldName").asText()).isEqualTo("id");
            assertThat(node.get(0).get("fieldValue").asText()).isEqualTo("5");
            assertThat(node.get(0).get("errorMessage").asText()).isEqualTo("must not be specified");
        }

        @Test
        @DisplayName("two-arg constructor with mismatched IDs adds error")
        void twoArgConstructorWithMismatchedIds() {
            var response = new BindingErrorsResponse(3, 5);
            var json = response.toJSON();
            var node = parseJson(json);
            assertThat(node.size()).isEqualTo(1);
            assertThat(node.get(0).get("objectName").asText()).isEqualTo("body");
            assertThat(node.get(0).get("fieldName").asText()).isEqualTo("id");
            assertThat(node.get(0).get("fieldValue").asText()).isEqualTo("5");
            assertThat(node.get(0).get("errorMessage").asText()).isEqualTo("does not match pathId: 3");
        }

        @Test
        @DisplayName("two-arg constructor with matching IDs adds no error")
        void twoArgConstructorWithMatchingIds() {
            var response = new BindingErrorsResponse(5, 5);
            var json = response.toJSON();
            var node = parseJson(json);
            assertThat(node.size()).isZero();
        }

        @Test
        @DisplayName("two-arg constructor with both null adds no error")
        void twoArgConstructorWithBothNull() {
            var response = new BindingErrorsResponse(null, null);
            var json = response.toJSON();
            var node = parseJson(json);
            assertThat(node.size()).isZero();
        }
    }

    @Nested
    @DisplayName("addError")
    class AddErrorTests {

        @Test
        @DisplayName("adds a single binding error")
        void addSingleError() {
            var response = new BindingErrorsResponse();
            var error = createBindingError("Owner", "name", "value", "must not be blank");
            response.addError(error);
            var json = response.toJSON();
            var node = parseJson(json);
            assertThat(node.size()).isEqualTo(1);
            assertThat(node.get(0).get("objectName").asText()).isEqualTo("Owner");
            assertThat(node.get(0).get("fieldName").asText()).isEqualTo("name");
            assertThat(node.get(0).get("fieldValue").asText()).isEqualTo("value");
            assertThat(node.get(0).get("errorMessage").asText()).isEqualTo("must not be blank");
        }

        @Test
        @DisplayName("adds multiple errors")
        void addMultipleErrors() {
            var response = new BindingErrorsResponse();
            response.addError(createBindingError("Owner", "name", "a", "too short"));
            response.addError(createBindingError("Owner", "email", "bad", "invalid"));
            var json = response.toJSON();
            var node = parseJson(json);
            assertThat(node.size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("addAllErrors with ConstraintViolations")
    class AddAllErrorsTests {

        @Test
        @DisplayName("adds errors from ConstraintViolation list")
        void addsErrorsFromViolations() {
            var response = new BindingErrorsResponse();

            var path = mock(Path.class);
            when(path.toString()).thenReturn("name");

            var violation = mock(ConstraintViolation.class);
            var rootBean = mock(Object.class);
            var rootClass = Owner.class;
            when(violation.getRootBean()).thenReturn(rootBean);
            when(violation.getRootBeanClass()).thenReturn(rootClass);
            when(violation.getPropertyPath()).thenReturn(path);
            when(violation.getInvalidValue()).thenReturn("bad value");
            when(violation.getMessage()).thenReturn("must not be blank");

            response.addAllErrors(java.util.List.of(violation));
            var json = response.toJSON();
            var node = parseJson(json);
            assertThat(node.size()).isEqualTo(1);
            assertThat(node.get(0).get("objectName").asText()).isEqualTo("Owner");
            assertThat(node.get(0).get("fieldName").asText()).isEqualTo("name");
            assertThat(node.get(0).get("fieldValue").asText()).isEqualTo("bad value");
            assertThat(node.get(0).get("errorMessage").asText()).isEqualTo("must not be blank");
        }

        @Test
        @DisplayName("adds errors from multiple violations")
        void addsMultipleViolations() {
            var response = new BindingErrorsResponse();

            var path1 = mock(Path.class);
            when(path1.toString()).thenReturn("name");
            var v1 = mock(ConstraintViolation.class);
            when(v1.getRootBeanClass()).thenReturn(Owner.class);
            when(v1.getPropertyPath()).thenReturn(path1);
            when(v1.getInvalidValue()).thenReturn("a");
            when(v1.getMessage()).thenReturn("too short");

            var path2 = mock(Path.class);
            when(path2.toString()).thenReturn("email");
            var v2 = mock(ConstraintViolation.class);
            when(v2.getRootBeanClass()).thenReturn(Owner.class);
            when(v2.getPropertyPath()).thenReturn(path2);
            when(v2.getInvalidValue()).thenReturn("bad");
            when(v2.getMessage()).thenReturn("invalid format");

            response.addAllErrors(java.util.List.of(v1, v2));
            var json = response.toJSON();
            var node = parseJson(json);
            assertThat(node.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("empty violation list adds no errors")
        void emptyViolationList() {
            var response = new BindingErrorsResponse();
            response.addAllErrors(java.util.List.of());
            var json = response.toJSON();
            var node = parseJson(json);
            assertThat(node.size()).isZero();
        }
    }

    @Nested
    @DisplayName("toJSON")
    class ToJsonTests {

        @Test
        @DisplayName("returns valid JSON array")
        void returnsValidJsonArray() throws JsonProcessingException {
            var response = new BindingErrorsResponse();
            response.addError(createBindingError("Pet", "type", "dog", "required"));
            var json = response.toJSON();
            var node = mapper.readTree(json);
            assertThat(node.isArray()).isTrue();
        }

        @Test
        @DisplayName("empty response returns empty JSON array")
        void emptyResponseReturnsEmptyArray() throws JsonProcessingException {
            var response = new BindingErrorsResponse();
            var json = response.toJSON();
            var node = mapper.readTree(json);
            assertThat(node.isArray()).isTrue();
            assertThat(node.size()).isZero();
        }

        @Test
        @DisplayName("JSON contains expected fields")
        void jsonContainsExpectedFields() throws JsonProcessingException {
            var response = new BindingErrorsResponse();
            response.addError(createBindingError("Visit", "date", "", "required"));
            var json = response.toJSON();
            var node = mapper.readTree(json).get(0);
            assertThat(node.has("objectName")).isTrue();
            assertThat(node.has("fieldName")).isTrue();
            assertThat(node.has("fieldValue")).isTrue();
            assertThat(node.has("errorMessage")).isTrue();
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringTests {

        @Test
        @DisplayName("toString contains bindingErrors")
        void toStringContainsBindingErrors() {
            var response = new BindingErrorsResponse();
            assertThat(response.toString()).contains("bindingErrors");
        }

        @Test
        @DisplayName("toString reflects added errors")
        void toStringReflectsErrors() {
            var response = new BindingErrorsResponse();
            response.addError(createBindingError("Owner", "name", "a", "short"));
            assertThat(response.toString()).contains("Owner");
            assertThat(response.toString()).contains("name");
        }
    }

    private JsonNode parseJson(String json) {
        try {
            return mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private BindingError createBindingError(String objectName, String fieldName, String fieldValue, String errorMessage) {
        // Access via reflection since BindingError is protected static
        try {
            var clazz = Class.forName("com.demo.rest.BindingErrorsResponse$BindingError");
            var constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            var instance = constructor.newInstance();
            clazz.getDeclaredMethod("setObjectName", String.class).invoke(instance, objectName);
            clazz.getDeclaredMethod("setFieldName", String.class).invoke(instance, fieldName);
            clazz.getDeclaredMethod("setFieldValue", String.class).invoke(instance, fieldValue);
            clazz.getDeclaredMethod("setErrorMessage", String.class).invoke(instance, errorMessage);
            return (BindingError) instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class Owner {
    }
}
