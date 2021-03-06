package users;

import config.Config;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetUsers {

    @BeforeClass
    public void setup() {
        baseURI = Config.getBaseUrl();
        basePath = "/api/users";
    }

    @Test
    public void getUsersAssertStatusCode() {
        given()
        .when()
            .get()
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void getUsersAssertValidateResponseBody() {
        given()
        .when()
            .get()
        .then()
            .assertThat()
            .body(matchesJsonSchemaInClasspath("users-schema.json"));
    }

    @Test
    public void getUsersAssertPaginationQueryParamWorks() {
        given()
            .queryParam("page", "2")
        .when()
            .get()
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
        .and()
            .body("data.size()", greaterThan(0));
    }

    @Test
    public void getUsersWrongPaginationAssertGetPageOne() {
        given()
            .queryParam("page", "abc")
        .when()
            .get()
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
        .and()
            .body("page", equalTo(1));
    }

    @Test
    public void getUsersBeyondLastPageAssertEmptyDataArray() {
        int lastPage = given()
                .queryParam("page", "abc")
            .when()
                .get()
            .then()
                .extract()
                .path("total");

        given()
            .queryParam("page", lastPage + 1)
        .when()
            .get()
        .then()
            .assertThat()
            .body("data.size()", equalTo(0));
    }

    @Test
    public void getUsersAssertStatusLine() {
        given()
        .when()
            .get()
        .then()
            .assertThat()
            .statusLine(containsString("OK"));
    }

    @Test
    public void getUsersAssertResponseContainsDataKey() {
        given()
        .when()
            .get()
        .then()
            .assertThat()
            .body("", hasKey("data"));
    }

    @Test
    public void getUsersAssertDataObjectContainsKeys() {
        given()
        .when()
            .get()
        .then()
            .assertThat()
            .body("data[0]", hasKey("id"))
        .and()
            .body("data[0]", hasKey("email"))
        .and()
            .body("data[0]", hasKey("first_name"))
        .and()
            .body("data[0]", hasKey("last_name"))
        .and()
            .body("data[0]", hasKey("avatar"))
        .and()
            .body("data[0].keySet()", hasSize(5));
    }

    @Test
    public void getUsersAssertDataArraySizeIsGreaterThanOne() {
        given()
        .when()
            .get()
        .then()
            .assertThat()
            .body("data.size()", greaterThan(1));
    }

    @Test
    public void getUsersAssertResponseTimeLessThanOneSecond() {
        given()
        .when()
            .get()
        .then()
            .assertThat()
            .time(lessThan(Config.MAX_RESPONSE_TIME));
    }
}
