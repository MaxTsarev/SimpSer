import java.util.Map;

public class Request {

    String requestMethod;
    Map <String, String> headers;
    String path;
    String body;

    public Request(String requestMethod, Map<String,String> headers, String path, String body) {
        this.requestMethod = requestMethod;
        this.headers = headers;
        this.path = path;
        this.body = body;
    }

    public Request(String requestMethod, Map<String,String> headers, String path) {
        this.requestMethod = requestMethod;
        this.headers = headers;
        this.path = path;
    }

    public Request(String requestMethod, String path) {
        this.requestMethod = requestMethod;
        this.path = path;
    }
}
