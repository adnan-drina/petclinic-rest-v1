package com.demo.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

/**
 * QuarkusTest + RestAssured characterization of OwnerRestController contracts.
 * Root path is /petclinic (application.properties).
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OwnerRestControllerTest {

    private static final String OWNERS = "/api/owners";
    private static Integer createdId;

    @Test
    @Order(1)
    @DisplayName("GET /api/owners returns 200 collection or 404 when empty")
    void listOwners() {
        given()
            .when().get(OWNERS)
            .then()
            .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    @Order(2)
    @DisplayName("GET /api/owners/{id} returns 404 for missing owner")
    void getMissingOwner() {
        given()
            .when().get(OWNERS + "/999999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(3)
    @DisplayName("POST /api/owners with id set returns 400")
    void postWithIdRejected() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"id":1,"firstName":"George","lastName":"Franklin","address":"110 W. Liberty St.","city":"Madison","telephone":"6085551023"}
                """)
            .when().post(OWNERS)
            .then()
            .statusCode(400);
    }

    @Test
    @Order(4)
    @DisplayName("POST /api/owners validation failure returns 400")
    void postValidationFailure() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"firstName":"","lastName":"Franklin","address":"x","city":"y","telephone":"1"}
                """)
            .when().post(OWNERS)
            .then()
            .statusCode(400);
    }

    @Test
    @Order(5)
    @DisplayName("POST /api/owners returns 201 with Location and body id")
    void postCreatesOwner() {
        createdId = given()
            .contentType(ContentType.JSON)
            .body("""
                {"firstName":"George","lastName":"Franklin","address":"110 W Liberty St","city":"Madison","telephone":"6085551023"}
                """)
            .when().post(OWNERS)
            .then()
            .statusCode(201)
            .header("Location", notNullValue())
            .body("id", notNullValue())
            .body("firstName", equalTo("George"))
            .body("lastName", equalTo("Franklin"))
            .extract().path("id");
    }

    @Test
    @Order(6)
    @DisplayName("GET /api/owners/{id} returns 200 after create")
    void getCreatedOwner() {
        given()
            .when().get(OWNERS + "/" + createdId)
            .then()
            .statusCode(200)
            .body("id", equalTo(createdId))
            .body("lastName", equalTo("Franklin"));
    }

    @Test
    @Order(7)
    @DisplayName("GET /api/owners returns 200 after at least one owner exists")
    void listOwnersAfterCreate() {
        given()
            .when().get(OWNERS)
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(8)
    @DisplayName("GET by lastName returns 200 for Franklin")
    void listByLastName() {
        given()
            .when().get(OWNERS + "/*/lastname/Franklin")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(9)
    @DisplayName("PUT /api/owners/{id} returns 204")
    void updateOwner() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"id":%d,"firstName":"Georgie","lastName":"Franklin","address":"110 W Liberty St","city":"Madison","telephone":"6085551023"}
                """.formatted(createdId))
            .when().put(OWNERS + "/" + createdId)
            .then()
            .statusCode(204);
    }

    @Test
    @Order(10)
    @DisplayName("PUT with mismatched body id returns 400")
    void updateMismatchedId() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"id":%d,"firstName":"Georgie","lastName":"Franklin","address":"110 W Liberty St","city":"Madison","telephone":"6085551023"}
                """.formatted(createdId + 1))
            .when().put(OWNERS + "/" + createdId)
            .then()
            .statusCode(400);
    }

    @Test
    @Order(11)
    @DisplayName("DELETE /api/owners/{id} returns 204 then 404")
    void deleteOwner() {
        given()
            .when().delete(OWNERS + "/" + createdId)
            .then()
            .statusCode(204);

        given()
            .when().delete(OWNERS + "/" + createdId)
            .then()
            .statusCode(404);
    }
}
