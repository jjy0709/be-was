package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.container.ControllerContainer;
import webserver.http.request.HttpRequest;
import webserver.http.request.HttpRequestParser;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseRenderer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest request = HttpRequestParser.parseHttpRequest(in);

            HttpResponse response = ControllerContainer.getInstance().handleRequest(request);

            HttpResponseRenderer.getInstance().responseRender(dos, response);

        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (InvocationTargetException e) {
            logger.debug("InvocationTargetException");
        } catch (IllegalAccessException e) {
            logger.debug("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            logger.debug("IllegalArgumentException");
        }
    }
}
