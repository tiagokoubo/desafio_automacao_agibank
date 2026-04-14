package com.agibank.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.agibank.utils.ReportListener;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Tag("api")
@ExtendWith(ReportListener.class)
public class DogApiTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://dog.ceo/api";
    }

    @Test
    @DisplayName("Validar listagem de todas as raças")
    public void shouldListAllBreeds() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/breeds/list/all")
        .then()
            .statusCode(200)
            .body("status", equalTo("success"))
            .body("message", notNullValue())
            .body(matchesJsonSchemaInClasspath("schemas/all-breeds-schema.json"));
    }

    @Test
    @DisplayName("Validar retorno de imagem aleatória")
    public void shouldReturnRandomBreedsImage() {
        when()
            .get("/breeds/image/random")
        .then()
            .statusCode(200)
            .body("status", equalTo("success"))
            .body("message", matchesPattern(".*(jpg|jpeg|png)"));
    }

    @Test
    @DisplayName("Validar retorno de multiplas imagens aleatórias de raças")
    public void shouldReturnMultipleRandomImagesOfBreeds() {
        int expectedAmount = 3;

        given()
            .pathParam("amount", expectedAmount)
        .when()
            .get("/breeds/image/random/{amount}")
        .then()
            .statusCode(200)
            .body("status", equalTo("success"))
            .body("message", instanceOf(List.class))
            .body("message", hasSize(expectedAmount))
            .body("message", everyItem(matchesPattern(".*(jpg|jpeg|png)")));
    }

    @Test
    @DisplayName("Validar busca de imagens por raça específica")
    public void shouldListImagesOfSpecificBreed() {
        String breedName = "husky";

        given()
            .pathParam("breed", breedName)
        .when()
            .get("/breed/{breed}/images")
        .then()
            .statusCode(200)
            .body("status", equalTo("success"))
            .body("message", hasItem(containsString(breedName)));
    }

    @Test
    @DisplayName("Validar retorno de imagem aleatória de raça especifica")
    public void shouldReturnRandomImageFromSpecificBreed() {
        String breedName = "husky";

        given()
            .pathParam("breed", breedName)
        .when()
            .get("/breed/{breed}/images/random")
        .then()
            .statusCode(200)
            .body("status", equalTo("success"))
            .body("message", containsString(breedName))
            .body("message", matchesPattern(".*(jpg|jpeg|png)"));
    }

    @Test
    @DisplayName("Validar retorno de multiplas imagens aleatórias de raça especifica")
    public void shouldReturnMultipleRandomImagesFromSpecificBreed() {
        String breedName = "husky";
        int expectedAmount = 3;

        given()
            .pathParam("breed", breedName)
            .pathParam("amount", expectedAmount)
        .when()
            .get("/breed/{breed}/images/random/{amount}")
        .then()
            .statusCode(200)
            .body("status", equalTo("success"))
            .body("message", hasSize(expectedAmount))
            .body("message", instanceOf(List.class))
            .body("message", everyItem(allOf(
                containsString(breedName),
                matchesPattern(".*(jpg|jpeg|png)")
            )));
    }

    @Test
    @DisplayName("Validar busca de sub-raças de raça especifica")
    public void shouldReturnSubBreedListForHound() {
        String breedName = "bulldog";

        given()
            .pathParam("breed", breedName)
        .when()
            .get("/breed/{breed}/list")
        .then()
            .statusCode(200)
            .body("status", equalTo("success"))
            .body("message", hasItems("boston", "english", "french"))
            .body("message", everyItem(matchesPattern("[a-z]+")));
    }

    @Test
    @DisplayName("Validar retorno de todas as imagens de uma especifica sub-raça")
    public void shouldReturnAllImagesFromSpecificSubBreed() {
        String breed = "bulldog";
        String subBreed = "french";

        given()
            .pathParam("breed", breed)
            .pathParam("subBreed", subBreed)
        .when()
            .get("/breed/{breed}/{subBreed}/images")
        .then()
            .statusCode(200)
            .body("status", equalTo("success"))
            .body("message", instanceOf(List.class))
            .body("message.size()", greaterThan(0))
            .body("message", everyItem(allOf(
            containsString(breed + "-" + subBreed),
            matchesPattern(".*(jpg|jpeg|png)")
        )));
    }

    @Test
    @DisplayName("Validate retorno de imagem aleatória de uma sub-raça especifica")
    public void shouldReturnRandomImageFromSpecificSubBreed() {
        String breed = "bulldog";
        String subBreed = "french";

        given()
            .pathParam("breed", breed)
            .pathParam("subBreed", subBreed)
        .when()
            .get("/breed/{breed}/{subBreed}/images/random")
        .then()
            .statusCode(200)
            .body("status", equalTo("success"))
            .body("message", containsString(breed + "-" + subBreed))
            .body("message", matchesPattern(".*(jpg|jpeg|png)"));
    }

    @Test
    @DisplayName("Validar retorno de multiplas imagens aleatórias de uma sub-raça especifica")
    public void shouldReturnMultipleRandomImagesFromSpecificSubBreed() {
        String breed = "bulldog";
        String subBreed = "french";
        int expectedAmount = 3;

        given()
            .pathParam("breed", breed)
            .pathParam("subBreed", subBreed)
            .pathParam("amount", expectedAmount)
        .when()
            .get("/breed/{breed}/{subBreed}/images/random/{amount}")
        .then()
            .statusCode(200)
            .body("status", equalTo("success"))
            .body("message", instanceOf(List.class))
            .body("message", hasSize(expectedAmount))
            .body("message", everyItem(allOf(
            containsString(breed + "-" + subBreed),
            matchesPattern(".*(jpg|jpeg|png)")
        )));
    }
}