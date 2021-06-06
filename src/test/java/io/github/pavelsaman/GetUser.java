package io.github.pavelsaman;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GetUser {

    private final String existingUserId = "/2";
    private final String nonExistentUserId = "/23";

    @BeforeClass
    public void setup() {
        baseURI = Config.getBaseUrl();
        basePath = "/api/users";
    }

    @Test
    public void userNotFoundAssertStatusCode() {
        get(nonExistentUserId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void userNotFoundAssertEmptyBody() {
        get(nonExistentUserId)
                .then()
                .assertThat()
                .body("isEmpty()", is(true));
    }

    @Test
    public void userFoundAssertStatusCode() {
        get(existingUserId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void userFoundAssertNonEmptyBody() {
        get(existingUserId)
                .then()
                .assertThat()
                .body("isEmpty()", is(false));
    }

    @Test
    public void userFoundAssertResponseBodyContainsKeys() {
        get(existingUserId)
                .then()
                .assertThat()
                .body("", hasKey("data"))
                .and()
                .body("", hasKey("support"));
    }

    @Test
    public void userFoundAssertDataObjectContainsKeys() {
        get(existingUserId)
                .then()
                .assertThat()
                .body("data", hasKey("id"))
                .and()
                .body("data", hasKey("email"))
                .and()
                .body("data", hasKey("first_name"))
                .and()
                .body("data", hasKey("last_name"))
                .and()
                .body("data", hasKey("avatar"));
    }
}
