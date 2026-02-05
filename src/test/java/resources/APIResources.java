package resources;

public enum APIResources {

    AddUser("/api/users");

    private String resource;

    APIResources(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
