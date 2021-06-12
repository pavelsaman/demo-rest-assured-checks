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

public class PatchUser {

    private final Gson gson = new Gson();

    @BeforeClass
    public void setup() {
        baseURI = Config.getBaseUrl();
        basePath = "/api/users";
    }

    @Test(dataProvider = "random-user", dataProviderClass = UserDataProvider.class)
    public void patchUserAssertStatusCodeAndLine(User randomUser) {
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

    @Test(dataProvider = "partial-user", dataProviderClass = UserDataProvider.class)
    public void patchUserWithPartialDataAssertStatusCodeAndLine(User fullUser, User partialUser) {
        String userId = fullUser.create(gson.toJson(fullUser));

        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(partialUser))
        .when()
            .put(userId)
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
        .and()
            .statusLine(containsString("OK"));
    }

    @Test(dataProvider = "random-user", dataProviderClass = UserDataProvider.class)
    public void patchUserWithOnlyNameAssertResponseBody(User randomUser) {
        String userId = randomUser.create(gson.toJson(randomUser));
        User copyRandomUser = (User) randomUser.clone();
        randomUser.setName("pavel");
        randomUser.setJob(null);

        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(randomUser))
        .when()
            .put(userId)
        .then()
            .assertThat()
            .body("name", equalTo(randomUser.getName()))
        .and()
            .body("job", equalTo(copyRandomUser.getJob()));
    }

    @Test(dataProvider = "random-user", dataProviderClass = UserDataProvider.class)
    public void patchUserWithOnlyJobAssertResponseBody(User randomUser) {
        String userId = randomUser.create(gson.toJson(randomUser));
        User copyRandomUser = (User) randomUser.clone();
        randomUser.setName(null);
        randomUser.setJob("tester");

        given()
            .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
            .body(gson.toJson(randomUser))
        .when()
            .put(userId)
        .then()
            .assertThat()
            .body("name", equalTo(copyRandomUser.getName()))
        .and()
            .body("job", equalTo(randomUser.getJob()));
    }
}
