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

public class DeleteUser {

    private final Gson gson = new Gson();

    @BeforeClass
    public void setup() {
        baseURI = Config.getBaseUrl();
        basePath = "/api/users";
    }

    @Test(dataProvider = "random-user", dataProviderClass = UserDataProvider.class)
    public void deleteUserAssertStatusCode(User randomUser) {
        String id = randomUser.create(gson.toJson(randomUser));

        given()
            .pathParam("id", id)
        .when()
            .delete("/{id}")
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NO_CONTENT)
        .and()
            .headers(HttpHeaders.CONTENT_TYPE, equalTo(null))
        .and()
            .headers(HttpHeaders.CONTENT_LENGTH, equalTo("0"));
    }
}
