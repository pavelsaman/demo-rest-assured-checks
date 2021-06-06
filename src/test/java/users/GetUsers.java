package users;

import config.Config;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GetUsers {

    @BeforeClass
    public void setup() {
        baseURI = Config.getBaseUrl();
        basePath = "/api/users";
    }

    @Test
    public void getUsersAssertStatusCode() {
        get()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void getUsersAssertStatusLine() {
        get()
                .then()
                .assertThat()
                .statusLine(containsString("OK"));
    }

    @Test
    public void getUsersAssertResponseContainsDataKey() {
        get()
                .then()
                .assertThat()
                .body("", hasKey("data"));
    }

    @Test
    public void getUsersAssertDataObjectContainsKeys() {
        get()
                .then()
                .assertThat()
                .body("data[0]", hasKey("id"))
                .and()
                .body("data[0]", hasKey("email"))
                .and()
                .body("data[0]", hasKey("first_name"))
                .and()
                .body("data[0]", hasKey("last_name"))
                .and()
                .body("data[0]", hasKey("avatar"))
                .and()
                .body("data[0].keySet()", hasSize(5));
    }

    @Test
    public void getUsersAssertDataArraySizeIsGreaterThanOne() {
        get()
                .then()
                .assertThat()
                .body("data.size()", greaterThan(1));
    }
}
