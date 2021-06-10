package users;

import com.google.gson.Gson;
import config.Config;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import users.support.Login;
import users.support.LoginDataProvider;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class LoginUser {

    private final Gson gson = new Gson();

    @BeforeClass
    public void setup() {
        baseURI = Config.getBaseUrl();
        basePath = "/api/login";
    }

    @Test(dataProvider = "valid-credentials", dataProviderClass = LoginDataProvider.class)
    public void successfulLoginAssertStatusCodeAndLine(Login validCredentials) {
        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(validCredentials))
        .when()
            .post()
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
        .and()
            .statusLine(containsString("OK"));
    }

    @Test
    public void successfulLoginAssertResponseTime() {
        Login validCredentials = new Login(System.getenv("EMAIL"), System.getenv("PASSWORD"));
        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(validCredentials))
        .when()
            .post()
        .then()
            .assertThat()
            .time(lessThan(Config.MAX_RESPONSE_TIME));
    }

    @Test(dataProvider = "valid-credentials", dataProviderClass = LoginDataProvider.class)
    public void successfulLoginValidateResponse(Login validCredentials) {
        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(validCredentials))
        .when()
            .post()
        .then()
            .assertThat()
            .body(matchesJsonSchemaInClasspath("userLogin-schema.json"));
    }

    @Test(dataProvider = "invalid-credentials", dataProviderClass = LoginDataProvider.class)
    public void failedLoginAssertStatusCode(Login invalidCredentials) {
        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(invalidCredentials))
        .when()
            .post()
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
        .and()
            .statusLine(containsString("Bad Request"))
        .and()
            .body("", not(hasKey("token")))
        .and()
            .body("", hasKey("error"));
    }

    @Test
    public void missingEmailAssertErrorMessage() {
        Login invalidCredentials = new Login(null, System.getenv("PASSWORD"));

        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(invalidCredentials))
        .when()
            .post()
        .then()
            .assertThat()
            .body("error", equalTo("Missing email or username"));
    }

    @Test
    public void missingPasswordAssertErrorMessage() {
        Login invalidCredentials = new Login(System.getenv("EMAIL"), null);

        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(invalidCredentials))
        .when()
            .post()
        .then()
            .assertThat()
            .body("error", equalTo("Missing password"));
    }

    @Test
    public void userNotFoundAssertErrorMessage() {
        Login invalidCredentials = new Login("a@a.cz", System.getenv("PASSWORD"));

        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(invalidCredentials))
        .when()
            .post()
        .then()
            .assertThat()
            .body("error", equalTo("user not found"));
    }
}
