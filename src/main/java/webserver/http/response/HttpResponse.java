package webserver.http.response;

import com.google.common.collect.Maps;
import webserver.http.enums.HttpResponseStatus;

import java.util.Map;

public class HttpResponse {
    private String version;
    private HttpResponseStatus status;
    private String fileName;
    private String sessionId;
    private String redirect;
    private Map<String, String> attributes;

    private HttpResponse(String version, HttpResponseStatus status, String fileName,
                         String sessionId, String redirect, Map<String, String> attributes) {
        this.version = version;
        this.status = status;
        this.fileName = fileName;
        this.sessionId = sessionId;
        this.redirect = redirect;
        this.attributes = attributes;
    }

    public static class Builder {
        private String version;
        private HttpResponseStatus status;
        private String fileName;
        private String sessionId;
        private String redirect;
        private Map<String, String> attributes;

        public Builder() {
            this.attributes = Maps.newHashMap();
        }

        public HttpResponse.Builder version(String version) {
            this.version = version;
            return this;
        }

        public HttpResponse.Builder status(HttpResponseStatus status) {
            this.status = status;
            return this;
        }

        public HttpResponse.Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public HttpResponse.Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public HttpResponse.Builder redirect(String redirect) {
            this.redirect = redirect;
            this.fileName = redirect;
            return this;
        }

        public HttpResponse.Builder setAttribute(Map<String, String> attributes) {
            this.attributes = attributes;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(version, status, fileName, sessionId, redirect, attributes);
        }

    }

    public static HttpResponse.Builder newBuilder() {
        return new Builder();
    }

    public String version() {
        return this.version;
    }

    public HttpResponseStatus status() {
        return this.status;
    }

    public String fileName() {
        return this.fileName;
    }

    public String sessionId() {
        return this.sessionId;
    }

    public String redirect() {
        return this.redirect;
    }

    public Map<String, String> attributes() {
        return this.attributes;
    }

}
