package users;

import com.google.gson.Gson;
import config.Config;
import org.apache.http.HttpHeaders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import users.support.User;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UpdateUser {

    private final Gson gson = new Gson();

    @BeforeClass
    public void setup() {
        baseURI = Config.getBaseUrl();
        basePath = "/api/users";
    }

    private String createRandomUserAndGetId(User randomUser) {
        return given()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(gson.toJson(randomUser))
            .when()
                .post()
            .then()
                .extract()
                .path("id");
    }

    @Test
    public void updateUserNameAssertStatusLine() {
        User randomUser = new User();
        String userId = createRandomUserAndGetId(randomUser);
        randomUser.setName("pavel");

        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(randomUser))
        .when()
            .put(userId)
        .then()
            .assertThat()
            .body("name", equalTo("pavel"));
    }

    @Test
    public void updateUserJobAssertStatusCode() {
        User randomUser = new User();
        String userId = createRandomUserAndGetId(randomUser);
        randomUser.setJob("qa");

        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(randomUser))
        .when()
            .put(userId)
        .then()
            .assertThat()
            .body("job", equalTo("qa"));
    }

    @Test
    public void updateUserValidateResponseBody() {
        User randomUser = new User();
        String userId = createRandomUserAndGetId(randomUser);
        randomUser.setJob("qa");

        given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(gson.toJson(randomUser))
        .when()
            .put(userId)
        .then()
            .assertThat()
            .body(matchesJsonSchemaInClasspath("userUpdate-schema.json"));
    }
}
