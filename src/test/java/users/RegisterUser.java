package users;

import com.google.gson.Gson;
import config.Config;
import http.HttpContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import users.support.UserRegister;
import users.support.UserRegisterDataProvider;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RegisterUser {

    private final Gson gson = new Gson();

    @BeforeClass
    public void setup() {
        baseURI = Config.getBaseUrl();
        basePath = "/api/register";
    }

    @Test(dataProvider = "valid-registration-credentials", dataProviderClass = UserRegisterDataProvider.class)
    public void successfulUserRegistrationAssertStatusCodeAndLine(UserRegister validRegistrationCredentials) {
        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(validRegistrationCredentials))
        .when()
            .post()
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_CREATED)
        .and()
            .statusLine(containsString("Created"));
    }

    @Test(dataProvider = "invalid-registration-credentials", dataProviderClass = UserRegisterDataProvider.class)
    public void failedUserRegistrationAssertStatusCodeAndLine(UserRegister invalidRegistrationCredentials) {
        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(invalidRegistrationCredentials))
        .when()
            .post()
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
        .and()
            .statusLine(containsString("Bad Request"))
        .and()
            .body("", not(hasKey("id")))
        .and()
            .body("", not(hasKey("token")))
        .and()
            .body("", hasKey("error"));
    }

    @Test
    public void successfulUserRegistrationValidateResponseBody() {
        UserRegister validRegistrationCredentials = new UserRegister("eve.holt@reqres.in", "pistol");

        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(validRegistrationCredentials))
        .when()
            .post()
        .then()
            .assertThat()
            .body(matchesJsonSchemaInClasspath("userRegister-schema.json"));
    }

    @Test
    public void addIdToRequestAssertResponseIdIsDifferent() {
        UserRegister registrationUserBody = new UserRegister(
            87, "eve.holt@reqres.in", "pistol"
        );

        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(registrationUserBody))
        .when()
            .post()
        .then()
            .assertThat()
            .body("id", not(equalTo(registrationUserBody.getId())));
    }

    @Test
    public void addTokenToRequestAssertResponseTokenIsDifferent() {
        UserRegister registrationUserBody = new UserRegister(
            "eve.holt@reqres.in",
            "pistol",
            RandomStringUtils.random(20, true, true)
        );

        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(registrationUserBody))
        .when()
            .post()
        .then()
            .assertThat()
            .body("token", not(equalTo(registrationUserBody.getToken())));
    }

    @Test
    public void malformedRequestBodyAssertResponse() {
        UserRegister registrationUserBody = new UserRegister(
            "eve.holt@reqres.in",
            "pistol"
        );

        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(String.format(
                "{\"email\":\"%s\",\"password\":\"%s\"",
                registrationUserBody.getEmail(),
                registrationUserBody.getPassword()
            ))
        .when()
            .post()
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
        .and()
            .statusLine(containsString("Bad Request"))
        .and()
            .contentType(containsString(HttpContentType.JSON));
    }

    @Test
    public void missingEmailAssertErrorMessage() {
        UserRegister registrationUserBody = new UserRegister(
            "eve.holt@reqres.in",
            "pistol"
        );
        registrationUserBody.setEmail(null);

        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(registrationUserBody))
        .when()
            .post()
        .then()
            .assertThat()
            .body("error", equalTo("Missing email or username"));
    }

    @Test
    public void missingPasswordAssertErrorMessage() {
        UserRegister registrationUserBody = new UserRegister(
            "eve.holt@reqres.in",
            "pistol"
        );
        registrationUserBody.setPassword(null);

        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(registrationUserBody))
        .when()
            .post()
        .then()
            .assertThat()
            .body("error", equalTo("Missing password"));
    }

    @Test
    public void missingContentTypeAssertStatusCode() {
        UserRegister registrationUserBody = new UserRegister(
            "eve.holt@reqres.in",
            "pistol"
        );

        given()
            .body(gson.toJson(registrationUserBody))
        .when()
            .post()
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void registerUserAssertResponseTimeLessThanSecond() {
        UserRegister registrationUserBody = new UserRegister(
            "eve.holt@reqres.in",
            "pistol"
        );

        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(registrationUserBody))
        .when()
            .post()
        .then()
            .assertThat()
            .time(lessThan(Config.MAX_RESPONSE_TIME));
    }
}
