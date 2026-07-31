package com.demo;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.demo.model.Owner;
import com.demo.model.Pet;
import com.demo.model.PetType;
import com.demo.model.Visit;
import com.demo.service.ClinicService;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;

/**
 * Circular-group E2E: Owner ↔ Pet ↔ Visit through CDI services + JAX-RS reads.
 * VisitDto/PetDto OpenAPI harvest omit FK ids — relationships asserted via ClinicService
 * and REST GETs after service-layer wiring (AS-IS API).
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CircularGroupIntegrationTest {

    @Inject
    ClinicService clinicService;

    private static Integer ownerId;
    private static Integer petId;
    private static Integer visitId;
    private static Integer petTypeId;

    @Test
    @Order(1)
    @DisplayName("REST: create Owner (entry to circular group)")
    void createOwnerViaRest() {
        ownerId = given()
            .contentType(ContentType.JSON)
            .body("""
                {"firstName":"George","lastName":"Franklin","address":"110 W Liberty St","city":"Madison","telephone":"6085551023"}
                """)
            .when().post("/api/owners")
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .extract().path("id");
    }

    @Test
    @Order(2)
    @DisplayName("Service: PetType + Pet linked to Owner + Visit (bidirectional)")
    void wirePetAndVisitViaService() {
        Owner owner = clinicService.findOwnerById(ownerId);
        assertThat(owner).isNotNull();

        PetType type = new PetType();
        type.setName("cat");
        clinicService.savePetType(type);
        petTypeId = type.getId();
        assertThat(petTypeId).isNotNull();

        Pet pet = new Pet();
        pet.setName("Leo");
        pet.setBirthDate(LocalDate.of(2020, 9, 7));
        pet.setType(type);
        owner.addPet(pet);
        clinicService.savePet(pet);
        petId = pet.getId();
        assertThat(petId).isNotNull();
        assertThat(pet.getOwner()).isSameAs(owner);
        assertThat(owner.getPets()).extracting(Pet::getName).contains("Leo");

        Visit visit = new Visit();
        visit.setDate(LocalDate.of(2024, 1, 15));
        visit.setDescription("rabies shot");
        pet.addVisit(visit);
        clinicService.saveVisit(visit);
        visitId = visit.getId();
        assertThat(visitId).isNotNull();
        assertThat(visit.getPet()).isSameAs(pet);
        assertThat(pet.getVisits()).isNotEmpty();
    }

    @Test
    @Order(3)
    @DisplayName("REST: Owner/Pet/Visit GETs see persisted graph + MapStruct DTOs")
    void restReadsReflectGraph() {
        given().when().get("/api/owners/" + ownerId)
            .then().statusCode(200)
            .body("id", equalTo(ownerId))
            .body("lastName", equalTo("Franklin"));

        given().when().get("/api/pets/" + petId)
            .then().statusCode(200)
            .body("id", equalTo(petId))
            .body("name", equalTo("Leo"))
            .body("type.name", equalTo("cat"));

        given().when().get("/api/visits/" + visitId)
            .then().statusCode(200)
            .body("id", equalTo(visitId))
            .body("description", equalTo("rabies shot"));

        given().when().get("/api/petTypes/" + petTypeId)
            .then().statusCode(200)
            .body("name", equalTo("cat"));

        given().when().get("/api/owners")
            .then().statusCode(200)
            .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(4)
    @DisplayName("REST validation failure on Owner create")
    void ownerValidationFailure() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"firstName":"","lastName":"X","address":"a","city":"b","telephone":"1"}
                """)
            .when().post("/api/owners")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(5)
    @DisplayName("REST missing entity → 404")
    void missingEntities() {
        given().when().get("/api/owners/999999").then().statusCode(404);
        given().when().get("/api/pets/999999").then().statusCode(404);
        given().when().get("/api/visits/999999").then().statusCode(404);
    }

    @Test
    @Order(6)
    @DisplayName("Concurrent Owner create attempts succeed independently")
    void concurrentOwnerCreates() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            List<Callable<Integer>> jobs = new ArrayList<>();
            String[] names = {"Alpha", "Bravo", "Charlie", "Delta"};
            for (int i = 0; i < names.length; i++) {
                final String first = names[i];
                final String phone = "555100" + i;
                jobs.add(() -> given()
                    .contentType(ContentType.JSON)
                    .body("""
                        {"firstName":"%s","lastName":"Concurrent","address":"1 Main","city":"Town","telephone":"%s"}
                        """.formatted(first, phone))
                    .when().post("/api/owners")
                    .then()
                    .statusCode(201)
                    .extract().path("id"));
            }
            List<Future<Integer>> futures = pool.invokeAll(jobs);
            List<Integer> ids = new ArrayList<>();
            for (Future<Integer> f : futures) {
                ids.add(f.get());
            }
            assertThat(ids).doesNotHaveDuplicates().hasSize(4);
        } finally {
            pool.shutdownNow();
        }
    }
}
