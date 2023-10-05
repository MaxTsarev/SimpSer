public class Request {

    private String requestMethod;
    private String path;
    private String[] headers;
    private String body;

    public Request(String requestMethod, String path, String[] headers, String body) {
        this.requestMethod = requestMethod;
        this.path = path;
        this.headers = headers;
        this.body = body;
    }

    public Request(String requestMethod, String path, String[] headers) {
        this.requestMethod = requestMethod;
        this.headers = headers;
        this.path = path;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getPath() {
        return path;
    }

    public String[] getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
