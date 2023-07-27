package webserver.http;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.request.HttpRequest;
import webserver.http.request.HttpRequestParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class HttpRequestParserTest {

    @Test
    @DisplayName("http 요청 String이 잘 파싱되는지 확인한다.")
    void checkParseDoneWell() throws IOException {
        SoftAssertions s = new SoftAssertions();
        String requestString = "GET /test HTTP/1.1\n" +
                "Accept: application/json\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Content-Type: application/json\n" +
                "\n" +
                "Test: Header\n";

        HttpRequest request = HttpRequestParser.parseHttpRequest(new ByteArrayInputStream(requestString.getBytes()));
        s.assertThat(request.method()).isEqualTo("GET");
        s.assertThat(request.uri()).isEqualTo("http://localhost:8080/test");
        s.assertThat(request.version()).isEqualTo(HttpClient.Version.HTTP_1_1);

        Map<String, List<String>> actualHeaders = new HashMap<>();
        actualHeaders.put("Accept", new ArrayList<String>() {{
            add("application/json");
        }});
        actualHeaders.put("Accept-Encoding", new ArrayList<String>() {{
            add("gzip");
            add("deflate");
        }});
        actualHeaders.put("Content-Type", new ArrayList<String>() {{
            add("application/json");
        }});
//        assertEquals(request.headers().keySet(), actualHeaders.keySet());
    }
}
