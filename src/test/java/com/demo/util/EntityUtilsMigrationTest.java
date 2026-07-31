package com.demo.util;

import com.demo.model.BaseEntity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EntityUtilsMigrationTest {

    static class SampleEntity extends BaseEntity {
        private String name;

        SampleEntity() {
        }

        SampleEntity(int id, String name) {
            setId(id);
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class AnotherEntity extends BaseEntity {
        private String value;

        AnotherEntity() {
        }

        AnotherEntity(int id, String value) {
            setId(id);
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private static <T extends BaseEntity> T getByIdReplacement(List<T> entities, Class<T> entityClass, int entityId) {
        return entities.stream()
                .filter(e -> e.getId() != null && e.getId() == entityId && entityClass.isInstance(e))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Entity of type " + entityClass.getName() + " with id " + entityId + " not found"));
    }

    @Nested
    @DisplayName("Stream API replacement finds entity when present")
    class FindEntityWhenPresent {

        @Test
        @DisplayName("finds entity by ID in a single-element list")
        void findsSingleEntity() {
            List<SampleEntity> list = new ArrayList<>();
            list.add(new SampleEntity(1, "alpha"));

            SampleEntity found = getByIdReplacement(list, SampleEntity.class, 1);

            assertThat(found).isNotNull();
            assertThat(found.getId()).isEqualTo(1);
            assertThat(found.getName()).isEqualTo("alpha");
        }

        @Test
        @DisplayName("finds entity by ID in a multi-element list")
        void findsEntityInMultiElementList() {
            List<SampleEntity> list = List.of(
                    new SampleEntity(1, "alpha"),
                    new SampleEntity(2, "beta"),
                    new SampleEntity(3, "gamma")
            );

            SampleEntity found = getByIdReplacement(list, SampleEntity.class, 2);

            assertThat(found).isNotNull();
            assertThat(found.getId()).isEqualTo(2);
            assertThat(found.getName()).isEqualTo("beta");
        }

        @Test
        @DisplayName("finds first matching entity when multiple have same ID")
        void findsFirstMatch() {
            List<SampleEntity> list = new ArrayList<>();
            list.add(new SampleEntity(1, "first"));
            list.add(new SampleEntity(1, "second"));

            SampleEntity found = getByIdReplacement(list, SampleEntity.class, 1);

            assertThat(found.getName()).isEqualTo("first");
        }

        @Test
        @DisplayName("finds entity at end of list")
        void findsEntityAtEnd() {
            List<SampleEntity> list = List.of(
                    new SampleEntity(1, "alpha"),
                    new SampleEntity(2, "beta"),
                    new SampleEntity(3, "gamma")
            );

            SampleEntity found = getByIdReplacement(list, SampleEntity.class, 3);

            assertThat(found.getId()).isEqualTo(3);
            assertThat(found.getName()).isEqualTo("gamma");
        }

        @Test
        @DisplayName("finds entity at beginning of list")
        void findsEntityAtBeginning() {
            List<SampleEntity> list = List.of(
                    new SampleEntity(1, "alpha"),
                    new SampleEntity(2, "beta"),
                    new SampleEntity(3, "gamma")
            );

            SampleEntity found = getByIdReplacement(list, SampleEntity.class, 1);

            assertThat(found.getId()).isEqualTo(1);
            assertThat(found.getName()).isEqualTo("alpha");
        }
    }

    @Nested
    @DisplayName("Stream API replacement throws when entity not found")
    class EntityNotFound {

        @Test
        @DisplayName("throws NoSuchElementException for non-existent ID")
        void throwsForNonExistentId() {
            List<SampleEntity> list = List.of(
                    new SampleEntity(1, "alpha"),
                    new SampleEntity(2, "beta")
            );

            assertThatThrownBy(() -> getByIdReplacement(list, SampleEntity.class, 99))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("throws NoSuchElementException for empty collection")
        void throwsForEmptyCollection() {
            List<SampleEntity> list = Collections.emptyList();

            assertThatThrownBy(() -> getByIdReplacement(list, SampleEntity.class, 1))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("throws when no entity matches the requested class")
        void throwsForNoMatch() {
            List<SampleEntity> list = new ArrayList<>();
            list.add(new SampleEntity(1, "alpha"));

            assertThatThrownBy(() -> getByIdReplacement(list, SampleEntity.class, 999))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("exception message identifies entity type and ID")
        void exceptionMessageIsInformative() {
            List<SampleEntity> list = List.of(new SampleEntity(1, "alpha"));

            assertThatThrownBy(() -> getByIdReplacement(list, SampleEntity.class, 5))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("SampleEntity")
                    .hasMessageContaining("5");
        }
    }

    @Nested
    @DisplayName("Stream API replacement with different entity types")
    class DifferentEntityTypes {

        @Test
        @DisplayName("works with SampleEntity type")
        void worksWithSampleEntity() {
            List<SampleEntity> list = List.of(new SampleEntity(42, "test"));
            SampleEntity found = getByIdReplacement(list, SampleEntity.class, 42);
            assertThat(found.getName()).isEqualTo("test");
        }

        @Test
        @DisplayName("works with AnotherEntity type")
        void worksWithAnotherEntity() {
            List<AnotherEntity> list = List.of(new AnotherEntity(7, "val"));
            AnotherEntity found = getByIdReplacement(list, AnotherEntity.class, 7);
            assertThat(found.getValue()).isEqualTo("val");
        }

        @Test
        @DisplayName("class guard prevents cross-type matches in mixed collection")
        void classGuardPreventsCrossTypeMatch() {
            List<SampleEntity> list = new ArrayList<>();
            list.add(new SampleEntity(1, "alpha"));
            list.add(new SampleEntity(1, "alpha-copy"));

            SampleEntity found = getByIdReplacement(list, SampleEntity.class, 1);
            assertThat(found.getName()).isEqualTo("alpha");
        }
    }

    @Nested
    @DisplayName("Null handling and edge cases")
    class NullHandling {

        @Test
        @DisplayName("skips entities with null ID")
        void skipsNullIdEntities() {
            List<SampleEntity> list = new ArrayList<>();
            list.add(new SampleEntity());
            list.add(new SampleEntity(2, "beta"));

            SampleEntity found = getByIdReplacement(list, SampleEntity.class, 2);
            assertThat(found.getName()).isEqualTo("beta");
        }

        @Test
        @DisplayName("throws when searching for null ID")
        void throwsForNullIdSearch() {
            List<SampleEntity> list = List.of(new SampleEntity(1, "alpha"));

            assertThatThrownBy(() -> getByIdReplacement(list, SampleEntity.class, 0))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("handles zero ID")
        void handlesZeroId() {
            List<SampleEntity> list = List.of(new SampleEntity(0, "zero"));
            SampleEntity found = getByIdReplacement(list, SampleEntity.class, 0);
            assertThat(found.getName()).isEqualTo("zero");
        }

        @Test
        @DisplayName("handles negative ID")
        void handlesNegativeId() {
            List<SampleEntity> list = List.of(new SampleEntity(-1, "neg"));
            SampleEntity found = getByIdReplacement(list, SampleEntity.class, -1);
            assertThat(found.getName()).isEqualTo("neg");
        }

        @Test
        @DisplayName("behavior matches legacy EntityUtils.getById iteration order")
        void preservesIterationOrder() {
            List<SampleEntity> list = new ArrayList<>();
            list.add(new SampleEntity(1, "first"));
            list.add(new SampleEntity(1, "second"));
            list.add(new SampleEntity(1, "third"));

            SampleEntity found = getByIdReplacement(list, SampleEntity.class, 1);

            assertThat(found.getName()).isEqualTo("first");
        }
    }

    @Nested
    @DisplayName("Replacement pattern verification")
    class ReplacementPattern {

        @Test
        @DisplayName("Stream filter+findFirst pattern produces same results as legacy loop")
        void streamPatternMatchesLegacyLoop() {
            List<SampleEntity> list = List.of(
                    new SampleEntity(1, "alpha"),
                    new SampleEntity(2, "beta"),
                    new SampleEntity(3, "gamma")
            );

            for (SampleEntity entity : list) {
                int targetId = entity.getId();

                SampleEntity streamResult = getByIdReplacement(list, SampleEntity.class, targetId);

                assertThat(streamResult).isNotNull();
                assertThat(streamResult.getId()).isEqualTo(targetId);
                assertThat(streamResult.getName()).isEqualTo(entity.getName());
            }
        }

        @Test
        @DisplayName("collected stream results match expected subset")
        void streamFilterProducesCorrectSubset() {
            List<SampleEntity> list = List.of(
                    new SampleEntity(1, "alpha"),
                    new SampleEntity(2, "beta"),
                    new SampleEntity(3, "gamma")
            );

            List<SampleEntity> filtered = list.stream()
                    .filter(e -> e.getId() != null && e.getId() > 1)
                    .toList();

            assertThat(filtered).hasSize(2);
            assertThat(filtered.get(0).getId()).isEqualTo(2);
            assertThat(filtered.get(1).getId()).isEqualTo(3);
        }
    }
}
