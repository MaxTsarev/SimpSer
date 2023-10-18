import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Request {

    private String requestMethod;
    private String path;
    private String queryParam;
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

    public Request(String requestMethod, String path, String queryParam, String[] headers, String body) {
        this.requestMethod = requestMethod;
        this.path = path;
        this.queryParam = queryParam;
        this.headers = headers;
        this.body = body;

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

    public String getQueryParamOne() {
        return queryParam;
    }

    public void getQueryParam(String name) {
        if (this.queryParam == null) {
            System.out.println("QueryString is empty!");
        }
        List<NameValuePair> list = URLEncodedUtils.parse(this.queryParam, StandardCharsets.UTF_8, '/', '?', '&', ';');
        if (list.isEmpty()) return;
        for (NameValuePair nameValuePair : list) {
            if (nameValuePair.getValue() == null) continue;
            if (nameValuePair.getName().equals(name)) {
                System.out.println(nameValuePair.getName() + ":" + nameValuePair.getValue());
            }
        }
    }

    public void getQueryParams() {
        if (this.queryParam == null) return;
        List<NameValuePair> list = URLEncodedUtils.parse(this.queryParam, StandardCharsets.UTF_8, '/', '?', '&', ';');
        if (list.isEmpty()) return;
        for (NameValuePair nameValuePair : list) {
            if (nameValuePair.getValue() == null) continue;
            System.out.println(nameValuePair.getName() + ":" + nameValuePair.getValue());
        }
    }

    public void getPostParam(String name) {
        List<NameValuePair> list = URLEncodedUtils.parse(this.body, StandardCharsets.UTF_8);
        for (NameValuePair nameValuePair : list) {
            if (nameValuePair.getName().equals(name)) {
                System.out.println(nameValuePair.getName() + ":" + nameValuePair.getValue());
            }
        }
    }

    public void getPostParams() {
        List<NameValuePair> list = URLEncodedUtils.parse(this.body, StandardCharsets.UTF_8);
        for (NameValuePair nameValuePair : list) {
            System.out.println(nameValuePair.getName() + ":" + nameValuePair.getValue());
        }
    }
}
