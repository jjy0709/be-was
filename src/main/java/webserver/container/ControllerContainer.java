package webserver.container;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controllers.Controller;
import webserver.controllers.annotations.RequestMethod;
import webserver.controllers.annotations.RequestPath;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControllerContainer {
    private static final Logger logger = LoggerFactory.getLogger(ControllerContainer.class);
    private static final ControllerContainer controllerContainer = new ControllerContainer();
    private static final Map<String, Controller> controllerMap = Maps.newConcurrentMap();
    private static final Map<EndPoint, Method> methodMap = Maps.newConcurrentMap();

    private Controller staticFileController;
    private Method staticFileHandle;

    public static ControllerContainer getInstance() {
        return controllerContainer;
    }

    private ControllerContainer() {
    }

    public void initialize(String packageName) throws ClassNotFoundException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        String packagePath = packageName.replace(".", "/");

        File controllerDir = Paths.get(classLoader.getResource(packagePath).toURI()).toFile();

        List<Class> classes = new ArrayList<>();

        addAllClassToList(controllerDir, classes, packageName, classLoader);

        addControllerToMap(classes);

        setStaticController();
    }

    private void addAllClassToList(File dir, List<Class> classes, String packageName, ClassLoader classLoader) throws ClassNotFoundException {
        for(File file: dir.listFiles()) {
            if(file.isDirectory()) {
                String subPackageName = String.format("%s.%s", packageName, file.getName());
                addAllClassToList(file, classes, subPackageName, classLoader);
                continue;
            }
            String className = packageName + "." + file.getName().replace(".class", "");
            Class<?> clazz = classLoader.loadClass(className);
            classes.add(clazz);
        }
    }


    private void setStaticController() {
        staticFileController = controllerMap.get("/");
        staticFileHandle = methodMap.get(new EndPoint("/", "GET"));
    }

    private void addControllerToMap(List<Class> classList) {
        for (Class clazz : classList) {
            if (!clazz.isAnnotationPresent(RequestPath.class)) {
                continue;
            }
            RequestPath requestPath = (RequestPath) clazz.getAnnotation(RequestPath.class);

            try {
                controllerMap.put(requestPath.path(), (Controller) clazz.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            Method[] methods = clazz.getDeclaredMethods();
            addMethodToMap(methods, requestPath);
        }
    }

    private void addMethodToMap(Method[] methods, RequestPath requestPath) {
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RequestMethod.class)) {
                continue;
            }
            RequestMethod requestMethod = method.getAnnotation(RequestMethod.class);
            methodMap.put(new EndPoint(requestPath.path(), requestMethod.method()), method);
        }
    }

    public HttpResponse handleRequest(HttpRequest request) throws InvocationTargetException, IllegalAccessException {
        EndPoint endPoint = new EndPoint(request.uri().getPath(), request.method());

        Controller controller = controllerMap.getOrDefault(endPoint.path, staticFileController);
        Method method = methodMap.getOrDefault(endPoint, staticFileHandle);

        if (method == null) {
            throw new RuntimeException("No Handler Exists");
        }

        return (HttpResponse) method.invoke(controller, request);
    }
}
