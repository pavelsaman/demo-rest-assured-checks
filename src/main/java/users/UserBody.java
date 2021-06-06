package users;

class UserBody {

    private String name;
    private String job;

    public UserBody() {
        this.name = "morpheus";
        this.job = "leader";
    }

    public UserBody(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
