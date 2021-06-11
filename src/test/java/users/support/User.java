package users.support;

import http.HttpContentType;
import org.apache.http.HttpHeaders;

import static io.restassured.RestAssured.given;

public class User {

    private String id;
    private String name;
    private String job;
    private String createdAt;

    public User(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String create(String userJSONString) {
        return given()
                .header(HttpHeaders.CONTENT_TYPE, HttpContentType.JSON)
                .body(userJSONString)
            .when()
                .post()
            .then()
                .extract()
                .path("id");
    }

    public User copy() {
        return new User(name, job);
    }
}
