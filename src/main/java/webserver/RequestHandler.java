package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.container.ControllerContainer;
import webserver.controllers.Controller;
import webserver.http.HttpRequest;
import webserver.http.HttpRequestParser;
import webserver.http.HttpResponse;
import webserver.controllers.FrontController;
import webserver.http.HttpResponseRenderer;

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

            HttpResponse response = ControllerContainer.getInstance().getController(request);

//            HttpResponse response = FrontController.getInstance().resolveRequest(request);

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

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
