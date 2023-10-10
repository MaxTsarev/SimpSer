import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Request {

    private String requestMethod;
    private String path;
    private String[] headers;
    private String body;

    public Request() {
    }


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

    public void getQueryParam(String name) {
        List<NameValuePair> list = URLEncodedUtils.parse(name, StandardCharsets.UTF_8, '/', '?', '&', ';');
        if (list.isEmpty()) return;
        for (NameValuePair nameValuePair : list) {
            if (nameValuePair.getValue() == null) return;
            System.out.println(nameValuePair.getName() + ":" + nameValuePair.getValue());
        }
    }

    public void getQueryParams() {
        List<NameValuePair> list = URLEncodedUtils.parse(this.path, StandardCharsets.UTF_8, '/', '?', '&', ';');
        if (list.isEmpty()) return;
        for (NameValuePair nameValuePair : list) {
            if (nameValuePair.getValue() == null) return;
            System.out.println(nameValuePair.getName() + ":" + nameValuePair.getValue());
        }
    }
}
