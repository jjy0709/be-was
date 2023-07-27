package webserver.controllers;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.controllers.user.UserCreateController;
import webserver.controllers.user.UserLoginController;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

class UserLoginControllerTest {
    SoftAssertions softly = new SoftAssertions();
    UserLoginController userLoginController = new UserLoginController();
    static UserCreateController userCreateController = new UserCreateController();

    @BeforeAll
    static void addTestUserToDatabase() throws IOException {
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


        userCreateController.handlePost(testRequest);

    }

    @Test
    @DisplayName("존재하는 유저이고 정확한 값으로 로그인 할 경우 세션을 생성한다.")
    void makeSessionWhenCorrectPassword() {
        HttpRequest.Builder builder = HttpRequest.newBuilder();

        HttpRequest testRequest = builder
                .setHeader("Host", "localhost:8080")
                .uri("/user/login")
                .setBody("userId=javajigi")
                .setBody("password=password")
                .setBody("name=%EB%B0%95%EC%9E%AC%EC%84%B1")
                .setBody("email=javajigi%40slipp.net")
                .version("HTTP/1.1")
                .build();

        HttpResponse response = userLoginController.handlePost(testRequest);

        softly.assertThat(response.attributes().get("Set-Cookie")).isNotEqualTo(null);
    }

    @Test
    @DisplayName("틀린 패스워드 값으로 로그인 할 경우 세션을 생성한다.")
    void makeSessionWhenWrongPassword() {
        HttpRequest.Builder builder = HttpRequest.newBuilder();

        HttpRequest testRequest = builder
                .setHeader("Host", "localhost:8080")
                .uri("/user/login")
                .setBody("userId=javajigi")
                .setBody("password=wrong")
                .setBody("name=%EB%B0%95%EC%9E%AC%EC%84%B1")
                .setBody("email=javajigi%40slipp.net")
                .version("HTTP/1.1")
                .build();

        HttpRequest testRequest2 = builder
                .setHeader("Host", "localhost:8080")
                .uri("/user/login")
                .setBody("userId=javajigi1")
                .setBody("password=password")
                .setBody("name=%EB%B0%95%EC%9E%AC%EC%84%B1")
                .setBody("email=javajigi%40slipp.net")
                .version("HTTP/1.1")
                .build();

        HttpResponse response = userLoginController.handlePost(testRequest);
        HttpResponse response2 = userLoginController.handlePost(testRequest2);

        softly.assertThat(response.attributes().get("Set-Cookie")).isEqualTo(null);
        softly.assertThat(response.redirect()).isEqualTo("/login_failed.html");
        softly.assertThat(response2.attributes().get("Set-Cookie")).isEqualTo(null);
        softly.assertThat(response2.redirect()).isEqualTo("/login_failed.html");
    }


}