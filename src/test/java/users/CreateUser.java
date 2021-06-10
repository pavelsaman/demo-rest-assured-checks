package users;

import com.google.gson.Gson;
import config.Config;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import users.support.User;
import users.support.UserDataProvider;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CreateUser {

    private final Gson gson = new Gson();
    private final User user = new User("Pavel Saman", "Tester");

    @BeforeClass
    public void setup() {
        baseURI = Config.getBaseUrl();
        basePath = "/api/users";
    }

    @Test
    public void createUserAssertStatusCode() {
        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(user))
        .when()
            .post()
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void createUserValidateResponseBody() {
        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(user))
        .when()
            .post()
        .then()
            .assertThat()
            .body(matchesJsonSchemaInClasspath("userCreate-schema.json"));
    }

    @Test
    public void createUserAssertStatusLine() {
        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(user))
        .when()
            .post()
        .then()
            .assertThat()
            .statusLine(containsString("Created"));
    }

    @Test
    public void createUserWithNoHeaderAssertResponseBody() {
        given()
            .body(gson.toJson(user))
        .when()
            .post()
        .then()
            .assertThat()
            .body("", not(hasKey("name")))
        .and()
            .body("", not(hasKey("job")))
        .and()
            .body("", hasKey("id"))
        .and()
            .body("", hasKey("createdAt"))
        .and()
            .body("keySet()", hasSize(2));
    }

    @Test
    public void createUserWithNoRequestBodyAssertStatusCode() {
        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
        .when()
            .post()
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void createUserWithEmptyRequestBodyAssertStatusCode() {
        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body("{}")
        .when()
            .post()
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void createUserWithEmptyRequestBodyAssertResponseBody() {
        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body("{}")
        .when()
            .post()
        .then()
            .assertThat()
            .body("", hasKey("createdAt"))
        .and()
            .body("", hasKey("id"))
        .and()
            .body("keySet()", hasSize(2));
    }

    @Test(dataProvider = "random-user", dataProviderClass = UserDataProvider.class)
    public void createRandomUserAssertResponseBody(User randomUser) {
        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(randomUser))
        .when()
            .post()
        .then()
            .assertThat()
            .body("name", equalTo(randomUser.getName()))
        .and()
            .body("job", equalTo(randomUser.getJob()));
    }

    @Test(dataProvider = "random-user", dataProviderClass = UserDataProvider.class)
    public void createUserWithMalformedRequestBodyAssertResponse(User randomUser) {
        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(randomUser) + ",")
        .when()
            .post()
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
        .and()
            .statusLine(containsString("Bad Request"))
        .and()
            .contentType(containsString("application/json"));
    }

    @Test(dataProvider = "random-user", dataProviderClass = UserDataProvider.class)
    public void createUserWithIdAssertIdIsNotAccepted(User randomUser) {
        randomUser.setId("15");

        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(randomUser))
        .when()
            .post()
        .then()
            .assertThat()
            .body("id", not(equalTo(randomUser.getId())));
    }

    @Test(dataProvider = "random-user", dataProviderClass = UserDataProvider.class)
    public void createUserWithCreatedAtAssertCreatedAtIsNotAccepted(User randomUser) {
        randomUser.setCreatedAt("2021-06-09T18:32:05.161Z");

        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(randomUser))
        .when()
            .post()
        .then()
            .assertThat()
            .body("createdAt", not(equalTo(randomUser.getCreatedAt())));
    }
}
