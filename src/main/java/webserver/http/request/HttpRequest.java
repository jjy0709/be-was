package webserver.http.request;

import com.google.common.collect.Maps;
import webserver.http.Cookie;
import webserver.http.Headers;
import webserver.http.Uri;

import java.util.HashMap;
import java.util.Map;

import static webserver.utils.StringUtils.getKeyString;
import static webserver.utils.StringUtils.getValueString;

public class HttpRequest {
    private String method;
    private Uri uri;
    private String version;
    private Headers headers;
    private Map<String, String> body;
    private Cookie cookie;

    private HttpRequest(String method, Uri uri, String version, Headers headers,
                        Map<String, String> body, Cookie cookie) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.headers = headers;
        this.body = body;
        this.cookie = cookie;
    }

    public static class Builder {
        private String method;
        private Uri uri;
        private String version;
        private Headers headers;
        private Map<String, String> body;
        private Cookie cookie;

        public Builder() {
            this.headers = new Headers();
            this.body = new HashMap<>();
        }

        public HttpRequest.Builder uri(String uri) {
            this.uri = new Uri(uri);
            return this;
        }

        public HttpRequest.Builder version(String version) {
            this.version = version;
            return this;
        }

        public HttpRequest.Builder setHeader(String header, String value) {
            this.headers.setHeader(header, value);
            if ("Cookie".equals(header))
                setCookie(value);
            return this;
        }

        private void setCookie(String value) {
            Map<String, String> attributes = Maps.newHashMap();
            String[] attrStrings = value.split(";");
            for (String attrString : attrStrings) {
                int splitIndex = attrString.indexOf("=");
                attributes.put(getKeyString(attrString, splitIndex), getValueString(attrString, splitIndex));
            }
            cookie = new Cookie(attributes);
        }

        public int getContentLength() {
            if (this.headers.getHeader("Content-Length") == null) {
                return 0;
            }
            return Integer.parseInt(this.headers.getHeader("Content-Length"));
        }

        public HttpRequest.Builder setBody(String bodyString) {
            int splitIndex = bodyString.indexOf("=");
            this.body.put(getKeyString(bodyString, splitIndex), getValueString(bodyString, splitIndex));
            return this;
        }

        public HttpRequest.Builder method(String method) {
            this.method = method;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(method, uri, version, headers, body, cookie);
        }
    }

    public static HttpRequest.Builder newBuilder() {
        return new Builder();
    }

    public String method() {
        return method;
    }

    public Uri uri() {
        return uri;
    }

    public String version() {
        return version;
    }

    public Headers headers() {
        return headers;
    }

    public String getHeader(String field) {
        return headers.getHeader(field);
    }

    public Map<String, String> body() {
        return body;
    }

    public Cookie cookie() { return cookie; }
}
