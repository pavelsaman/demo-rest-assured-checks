package users;

import com.google.gson.Gson;
import config.Config;
import org.apache.http.HttpHeaders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class UpdateUser {

    private final Gson gson = new Gson();

    @BeforeClass
    public void setup() {
        baseURI = Config.getBaseUrl();
        basePath = "/api/users";
    }

    private String createRandomUserAndGetId(UserBody randomUser) {
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
        UserBody randomUser = new UserBody();
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
        UserBody randomUser = new UserBody();
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
}
