package users;

import com.google.gson.Gson;
import config.Config;
import http.HttpContentType;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import users.support.User;
import users.support.UserDataProvider;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UpdateUser {

    private final Gson gson = new Gson();

    @BeforeClass
    public void setup() {
        baseURI = Config.getBaseUrl();
        basePath = "/api/users";
    }

    @Test(dataProvider = "random-user", dataProviderClass = UserDataProvider.class)
    public void updateUserAssertStatusCodeAndLine(User randomUser) {
        String userId = randomUser.create(gson.toJson(randomUser));

        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(randomUser))
        .when()
            .put(userId)
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
        .and()
            .statusLine(containsString("OK"));
    }

    @Test(dataProvider = "random-user", dataProviderClass = UserDataProvider.class)
    public void updateUserNameAssertName(User randomUser) {
        String userId = randomUser.create(gson.toJson(randomUser));
        randomUser.setName("pavel");

        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(randomUser))
        .when()
            .put(userId)
        .then()
            .assertThat()
            .body("name", equalTo(randomUser.getName()));
    }

    @Test(dataProvider = "random-user", dataProviderClass = UserDataProvider.class)
    public void updateUserJobAssertJob(User randomUser) {
        String userId = randomUser.create(gson.toJson(randomUser));
        randomUser.setJob("qa");

        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(randomUser))
        .when()
            .put(userId)
        .then()
            .assertThat()
            .body("job", equalTo(randomUser.getJob()));
    }

    @Test(dataProvider = "random-user", dataProviderClass = UserDataProvider.class)
    public void updateUserValidateResponseBody(User randomUser) {
        String userId = randomUser.create(gson.toJson(randomUser));
        randomUser.setJob("qa");

        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(randomUser))
        .when()
            .put(userId)
        .then()
            .assertThat()
            .body(matchesJsonSchemaInClasspath("userUpdate-schema.json"));
    }

    @Test(dataProvider = "partial-user", dataProviderClass = UserDataProvider.class)
    public void partialUpdateJobNullAssertBadRequest(User fullUser, User partialUser) {
        String userId = fullUser.create(gson.toJson(fullUser));

        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(partialUser))
        .when()
            .put(userId)
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
