package webserver.controllers;

import model.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import webserver.controllers.user.UserCreateController;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static webserver.http.enums.HttpResponseStatus.BAD_REQUEST;
import static webserver.http.enums.HttpResponseStatus.FOUND;

class UserCreateControllerTest {

    SoftAssertions softly = new SoftAssertions();
    UserCreateController userCreateController = new UserCreateController();

    @Test
    @DisplayName("handleUserCreateRequest의 기능 확인 테스트")
    void handleUserCreateRequest() throws IOException, InvocationTargetException, IllegalAccessException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        HttpRequest testRequest = builder
                .setHeader("Host","localhost:8080")
                .uri("/user/create")
                .setBody("userId=javajigi")
                .setBody("password=password")
                .setBody("name=%EB%B0%95%EC%9E%AC%EC%84%B1")
                .setBody("email=javajigi%40slipp.net")
                .version("HTTP/1.1")
                .build();

        HttpResponse response = userCreateController.handlePost(testRequest);

        User actualUser = new User("javajigi", "password", "박재성", "javajigi@slipp.net");

        HttpResponse actual = HttpResponse.newBuilder()
                .status(FOUND)
                .version("HTTP/1.1")
                .redirect("/index.html")
                .build();

        softly.assertThat(response.version()).isEqualTo(actual.version());
        softly.assertThat(response.status()).isEqualTo(actual.status());
        softly.assertThat(response.redirect()).isEqualTo(actual.redirect());
    }

    @Test
    @DisplayName("중복된 userId로 create 요청이 들어올 경우 두번째 요청은 created_failed.html을 반환")
    void handleUserCreateRequestWithSameUserId() throws IOException, InvocationTargetException, IllegalAccessException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        HttpRequest testRequest = builder
                .setHeader("Host", "localhost:8080")
                .uri("/user/create")
                .setBody("userId=javajigi")
                .setBody("password=password")
                .setBody("name=%EB%B0%95%EC%9E%AC%EC%84%B1")
                .setBody("email=javajigi%40slipp.net")
                .version("HTTP/1.1")
                .build();

        HttpResponse response = userCreateController.handleGet(testRequest);

        HttpResponse actual = HttpResponse.newBuilder()
                .status(FOUND)
                .version("HTTP/1.1")
                .redirect("/create_failed.html")
                .build();

        softly.assertThat(response.version()).isEqualTo(actual.version());
        softly.assertThat(response.status()).isEqualTo(actual.status());
        softly.assertThat(response.redirect()).isEqualTo(actual.redirect());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net"
            , "userId=&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net"
            , "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email="})
    @DisplayName("handleUserCreateRequest의 기능 확인 테스트2")
    void handleUserCreateRequest2(String wrongUri) throws IOException, InvocationTargetException, IllegalAccessException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        String[] wrongBody = wrongUri.split("&");
        for (String wrongParam : wrongBody) {
            builder.setBody(wrongParam);
        }
        HttpRequest testRequest = builder
                .uri("/user/create")
                .version("HTTP/1.1")
                .build();

        HttpResponse response = userCreateController.handlePost(testRequest);

        HttpResponse actual = HttpResponse.newBuilder()
                .status(BAD_REQUEST)
                .version("HTTP/1.1")
                .build();

        softly.assertThat(response.version()).isEqualTo(actual.version());
        softly.assertThat(response.status()).isEqualTo(actual.status());
    }

}