package webserver.controllers;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.controllers.user.UserLoginController;
import webserver.controllers.user.UserLogoutController;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

import static webserver.http.enums.HttpResponseStatus.FOUND;

class UserLogoutControllerTest {
    SoftAssertions softly = new SoftAssertions();
    UserLogoutController userLogoutController = new UserLogoutController();
    static UserLoginController userLoginController = new UserLoginController();
    static String sessionID;

    @BeforeAll
    static void addTestUserToDatabase() throws IOException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        HttpRequest loginRequest = builder
                .setHeader("Host","localhost:8080")
                .uri("/user/login")
                .setBody("userId=user")
                .setBody("password=pass")
                .version("HTTP/1.1")
                .build();

        HttpResponse response = userLoginController.handlePost(loginRequest);
        sessionID = response.sessionId();
    }

    @Test
    @DisplayName("로그아웃 기능 작동 테스트")
    void handleLogoutRequest() {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        HttpRequest logoutRequest = builder
                .uri("/user/logout")
                .setHeader("Cookie", "sid="+sessionID)
                .version("HTTP/1.1")
                .build();

        HttpResponse response = userLogoutController.handleGet(logoutRequest);

        softly.assertThat(response.status()).isEqualTo(FOUND);
        softly.assertThat(response.redirect()).isEqualTo("/index.html");
        softly.assertThat(response.sessionId()).isNotEqualTo(null);
    }

    @Test
    @DisplayName("로그인 되지 않은 상태에서 로그아웃 시에도 index.html로 이동")
    void handleLogoutWhenNotLogin() {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        HttpRequest logoutRequest = builder
                .uri("/user/logout")
                .version("HTTP/1.1")
                .build();

        HttpResponse response = userLogoutController.handleGet(logoutRequest);

        softly.assertThat(response.status()).isEqualTo(FOUND);
        softly.assertThat(response.redirect()).isEqualTo("/index.html");
        softly.assertThat(response.sessionId()).isEqualTo(null);
    }


}