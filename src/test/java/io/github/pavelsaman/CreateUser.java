package io.github.pavelsaman;

import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CreateUser {

    private final Gson gson = new Gson();
    private final UserBody user = new UserBody();

    private static class UserBody {

        private final String name;
        private final String job;

        public UserBody() {
            this.name = "morpheus";
            this.job = "leader";
        }

        public UserBody(String name, String job) {
            this.name = name;
            this.job = job;
        }
    }

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
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void createUserAssertStatusLine() {
        given()
                .header("Content-Type", "application/json")
                .body(gson.toJson(user))
                .post()
                .then()
                .assertThat()
                .statusLine(containsString("Created"));
    }

    @Test
    public void createUserAssertResponseBodyContainsKeys() {
        given()
                .header("Content-Type", "application/json")
                .body(gson.toJson(user))
                .post()
                .then()
                .assertThat()
                .body("", hasKey("name"))
                .and()
                .body("", hasKey("job"))
                .and()
                .body("", hasKey("id"))
                .and()
                .body("", hasKey("createdAt"));
    }

    @Test
    public void createUserWithNoHeaderAssertResponseBody() {
        given()
                .body(gson.toJson(user))
                .post()
                .then()
                .assertThat()
                .body("", not(hasKey("name")))
                .and()
                .body("", not(hasKey("job")))
                .and()
                .body("", hasKey("id"))
                .and()
                .body("", hasKey("createdAt"));
    }

    @Test
    public void createUserWithNoRequestBodyAssertStatusCode() {
        given()
                .header("Content-Type", "application/json")
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void createUserWithEmptyRequestBodyAssertStatusCode() {
        given()
                .header("Content-Type", "application/json")
                .body("{}")
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(dataProvider = "random-user")
    public void createRandomUserAssertResponseBody(UserBody randomUser) {
        given()
                .header("Content-Type", "application/json")
                .body(gson.toJson(randomUser))
                .post()
                .then()
                .assertThat()
                .body("name", equalTo(randomUser.name))
                .and()
                .body("job", equalTo(randomUser.job));
    }
}
