package users.support;

import http.HttpContentType;
import org.apache.http.HttpHeaders;

import java.util.Objects;

import static io.restassured.RestAssured.given;

public class User implements Cloneable {

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

    @Override
    public Object clone() {
        User copyOfUser = null;
        try {
            copyOfUser = (User) super.clone();
            copyOfUser.id = this.id;
            copyOfUser.name = this.name;
            copyOfUser.job = this.job;
            copyOfUser.createdAt = this.createdAt;

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return copyOfUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(job, user.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, job);
    }
}
