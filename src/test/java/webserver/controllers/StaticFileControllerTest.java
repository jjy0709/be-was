package webserver.controllers;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static webserver.http.enums.HttpResponseStatus.NOT_FOUND;
import static webserver.http.enums.HttpResponseStatus.OK;

class StaticFileControllerTest {
    SoftAssertions softly = new SoftAssertions();
    StaticFileController staticFileController = new StaticFileController();

    @ParameterizedTest
    @ValueSource(strings = {"/index.html", "/user/form.html"})
    @DisplayName("정적 html 파일 리턴 기능 확인 테스트")
    void handleStaticFileReturn(String fileName) throws IOException, InvocationTargetException, IllegalAccessException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        HttpRequest testRequest = builder
                .uri(fileName)
                .method("GET")
                .version("HTTP/1.1")
                .build();

        HttpResponse response = staticFileController.handleGet(testRequest);

        String template_path = "src/main/resources/templates/" + fileName;
        HttpResponse actual = HttpResponse.newBuilder()
                .status(OK)
                .version("HTTP/1.1")
                .fileName(template_path)
                .build();

        softly.assertThat(response.version()).isEqualTo(actual.version());
        softly.assertThat(response.status()).isEqualTo(actual.status());
        softly.assertThat(response.fileName()).isEqualTo(actual.fileName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/notExistingFile.html"})
    @DisplayName("정적 html 파일 리턴 기능 확인 테스트2")
    void handleStaticFileReturn2(String fileName) throws IOException, InvocationTargetException, IllegalAccessException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        HttpRequest testRequest = builder
                .uri(fileName)
                .method("GET")
                .version("HTTP/1.1")
                .build();

        HttpResponse response = staticFileController.handleGet(testRequest);

        HttpResponse actual = HttpResponse.newBuilder()
                .version("HTTP/1.1")
                .status(NOT_FOUND)
                .build();

        softly.assertThat(response.version()).isEqualTo(actual.version());
        softly.assertThat(response.status()).isEqualTo(actual.status());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/css/bootstrap.min.css", "/css/styles.css", "/js/scripts.js", "/favicon.ico"})
    @DisplayName("정적 파일 리턴 기능 확인 테스트")
    void handleStaticOtherFilesReturn(String fileName) throws IOException, InvocationTargetException, IllegalAccessException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        HttpRequest testRequest = builder
                .uri(fileName)
                .method("GET")
                .version("HTTP/1.1")
                .build();

        HttpResponse response = staticFileController.handleGet(testRequest);

        String template_path = "src/main/resources/static/" + fileName;
        HttpResponse actual = HttpResponse.newBuilder()
                .status(OK)
                .version("HTTP/1.1")
                .fileName(template_path)
                .build();

        softly.assertThat(response.version()).isEqualTo(actual.version());
        softly.assertThat(response.status()).isEqualTo(actual.status());
        softly.assertThat(response.fileName()).isEqualTo(actual.fileName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/styles.css", "/scripts.js", "images/favicon.ico"})
    @DisplayName("정적 파일 리턴 기능 확인 테스트2")
    void handleStaticOtherFilesReturn2(String fileName) throws IOException, InvocationTargetException, IllegalAccessException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        HttpRequest testRequest = builder
                .uri(fileName)
                .method("GET")
                .version("HTTP/1.1")
                .build();

        HttpResponse response = staticFileController.handleGet(testRequest);

        HttpResponse actual = HttpResponse.newBuilder()
                .version("HTTP/1.1")
                .status(NOT_FOUND)
                .build();

        softly.assertThat(response.version()).isEqualTo(actual.version());
        softly.assertThat(response.status()).isEqualTo(actual.status());
    }
}