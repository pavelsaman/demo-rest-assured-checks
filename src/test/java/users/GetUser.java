package users;

import config.Config;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetUser {

    private final String existingUserId = "/2";
    private final String nonExistentUserId = "/23";

    @BeforeClass
    public void setup() {
        baseURI = Config.getBaseUrl();
        basePath = "/api/users";
    }

    @Test
    public void userNotFoundAssertStatusCode() {
        given()
        .when()
            .get(nonExistentUserId)
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void getUserValidateResponseBody() {
        given()
        .when()
            .get(existingUserId)
        .then()
            .assertThat()
            .body(matchesJsonSchemaInClasspath("userDetail-schema.json"));
    }

    @Test
    public void userNotFoundAssertEmptyBody() {
        given()
        .when()
            .get(nonExistentUserId)
        .then()
            .assertThat()
            .body("isEmpty()", is(true));
    }

    @Test
    public void userFoundAssertStatusCode() {
        given()
        .when()
            .get(existingUserId)
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void userFoundAssertNonEmptyBody() {
        given()
        .when()
            .get(existingUserId)
        .then()
            .assertThat()
            .body("isEmpty()", is(false));
    }

    @Test
    public void userFoundAssertResponseBodyContainsKeys() {
        given()
        .when()
            .get(existingUserId)
        .then()
            .assertThat()
            .body("", hasKey("data"))
        .and()
            .body("", hasKey("support"))
        .and()
            .body("keySet()", hasSize(2));
    }

    @Test
    public void userFoundAssertDataObjectContainsKeys() {
        given()
        .when()
            .get(existingUserId)
        .then()
            .assertThat()
            .body("data", hasKey("id"))
        .and()
            .body("data", hasKey("email"))
        .and()
            .body("data", hasKey("first_name"))
        .and()
            .body("data", hasKey("last_name"))
        .and()
            .body("data", hasKey("avatar"))
        .and()
            .body("data.keySet()", hasSize(5));
    }

    @Test
    public void getUserAssertResponseTimeLessThanOneSecond() {
        given()
        .when()
            .get(existingUserId)
        .then()
            .assertThat()
            .time(lessThan(1000L));
    }
}
