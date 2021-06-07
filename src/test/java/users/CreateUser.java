package users;

import com.google.gson.Gson;
import config.Config;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CreateUser {

    private final Gson gson = new Gson();
    private final UserBody user = new UserBody();

    @DataProvider(name = "random-user")
    public Object[][] randomUser() {
        String randomName = RandomStringUtils.random(10, true, false);
        String randomJob = RandomStringUtils.random(8, true, false);

        return new Object[][] { { new UserBody(randomName, randomJob) } };
    }

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
    public void createUserAssertResponseBodyContainsKeys() {
        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(user))
        .when()
            .post()
        .then()
            .assertThat()
            .body("", hasKey("name"))
        .and()
            .body("", hasKey("job"))
        .and()
            .body("", hasKey("id"))
        .and()
            .body("", hasKey("createdAt"))
        .and()
            .body("keySet()", hasSize(4));
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

    @Test(dataProvider = "random-user")
    public void createRandomUserAssertResponseBody(UserBody randomUser) {
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
}
