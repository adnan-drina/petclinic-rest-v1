package com.demo.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * Smoke contracts for remaining JAX-RS controllers (open access, empty→404 or 200).
 */
@QuarkusTest
class RestControllersContractTest {

    @Test
    @DisplayName("GET collection endpoints return 200 or 404 when empty")
    void collectionEndpoints() {
        for (String path : new String[] {
            "/api/pets",
            "/api/vets",
            "/api/petTypes",
            "/api/specialties",
            "/api/visits"
        }) {
            given().when().get(path).then().statusCode(anyOf(is(200), is(404)));
        }
    }

    @Test
    @DisplayName("GET missing ids return 404")
    void missingIds() {
        for (String path : new String[] {
            "/api/pets/999999",
            "/api/vets/999999",
            "/api/petTypes/999999",
            "/api/specialties/999999",
            "/api/visits/999999"
        }) {
            given().when().get(path).then().statusCode(404);
        }
    }
}
