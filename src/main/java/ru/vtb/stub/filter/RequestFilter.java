package ru.vtb.stub.filter;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.net.URLDecoder.decode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static ru.vtb.stub.data.ResponseData.*;
import static ru.vtb.stub.utils.CommonUtils.KEY_DELIMITER;
import static ru.vtb.stub.utils.CommonUtils.getRequestQueryParams;

@Slf4j
@Setter
@Component
@ConfigurationProperties(prefix = "path")
public class RequestFilter implements Filter {

    @Value("${path.response}")
    private String redirectPath;
    @Value("${response.error.message}")
    private String defaultErrorMessage;
    @Value("${prefix.header}")
    private  String headerPrefix;
    @Value("${prefix.query}")
    private  String queryPrefix;

    private Map<String, String> admin;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String key = request.getRequestURI() + KEY_DELIMITER + request.getMethod();

        // Cannot set admin routes
        if (admin.containsValue(request.getRequestURI())) {
            String queryString = request.getQueryString();
            if (!queryString.contains("key=")) {
                String error = "Request filter. Request to: " + request.getRequestURI() + " without required param 'key'";
                log.error(error);
                response.sendError(500, error);
                return;
            }
            var requestQueryParams = getRequestQueryParams(queryString);
            String routeToSet = decode(requestQueryParams.get("key"), UTF_8.name()).split(KEY_DELIMITER)[0];
            if (admin.containsValue(routeToSet)) {
                String error = "Route: " + routeToSet + " is admin route. See application.yaml --> path.admin";
                log.error(error);
                response.sendError(500, error);
                return;
            }
        } else
            log.info("Request filter. Request to: {}", key);

        // Without wrapper - exception:
        // java.lang.IllegalStateException: getReader() has already been called for this request
        // in filterChain.doFilter(servletRequest, servletResponse);
        RequestWrapper wrappedRequest = new RequestWrapper(request);

        if (validateData.get(key) != null) {
            List<String> errors = new ArrayList<>();
            errors.add(validateQueryParams(wrappedRequest, validateData.get(key)));
            errors.add(validateHeaders(wrappedRequest, validateData.get(key)));
            errors.add(validateJsonBody(wrappedRequest, validateData.get(key)));
            errors.removeAll(Collections.singleton(null));
            if (!errors.isEmpty()) {
                String errorMessage = String.join("; ", errors);
                log.error("\tValidation errors: {}", errorMessage);
                response.sendError(500, errorMessage);
                return;
            }
            log.info("\tSuccessful request validation");
        }

        if (errorData.get(key) != null) {
            var error = errorData.get(key);
            var status = (Integer) error.get("status");
            String message = error.get("message") != null ? (String) error.get("message") : defaultErrorMessage;
            log.error("\tFound data to send error message. Status code: {}. Message: {}", status, message);
            response.sendError(status, message);
            return;
        }

        if (responseData.get(key) != null) {
            log.info("\tRedirect to Response data controller");
            wrappedRequest.getRequestDispatcher(redirectPath + "?key=" + key).forward(servletRequest, servletResponse);
        } else
            filterChain.doFilter(wrappedRequest, servletResponse);
    }

    private String validateQueryParams(RequestWrapper wrappedRequest, Map<String, String> data) {
        var exceptedQueryParams = data.keySet().stream()
                .filter(k -> k.startsWith(queryPrefix))
                .collect(Collectors.toMap(k -> k.split(queryPrefix, 2)[1], data::get));

        if (exceptedQueryParams.isEmpty()) return null;

        if (wrappedRequest.getQueryString() == null) return "empty required query params";
        var requestQueryParams = getRequestQueryParams(wrappedRequest.getQueryString());

        List<String> errors = new ArrayList<>();
        for (var entry : exceptedQueryParams.entrySet()) {
            if (!requestQueryParams.containsKey(entry.getKey())) {
                errors.add("excepted param: '" + entry.getKey() + "' not found");
                continue;
            }
            Pattern pattern = Pattern.compile(entry.getValue());
            Matcher matcher = pattern.matcher(requestQueryParams.get(entry.getKey()));
            if (!matcher.matches()) {
                errors.add("param: '" + entry.getKey() + "' is not matches: " + entry.getValue());
            }
        }
        if (!errors.isEmpty()) return String.join("; ", errors);
        return null;
    }

    private String validateHeaders(HttpServletRequest request, Map<String, String> data) {
        var exceptedHeaders = data.keySet().stream()
                .filter(k -> k.startsWith(headerPrefix))
                .collect(Collectors.toMap(k -> k.split(headerPrefix, 2)[1], data::get));
        if (exceptedHeaders.isEmpty()) return null;

        var requestHeaderNames = request.getHeaderNames();
        Map<String, String> desiredRequestHeaders = new HashMap<>();
        while (requestHeaderNames.hasMoreElements()) {
            String headerName = requestHeaderNames.nextElement();
            if (exceptedHeaders.containsKey(headerName))
                desiredRequestHeaders.put(headerName, request.getHeader(headerName));
        }
        if (desiredRequestHeaders.isEmpty()) return "empty required headers";

        List<String> errors = new ArrayList<>();
        for (var entry : exceptedHeaders.entrySet()) {
            if (!desiredRequestHeaders.containsKey(entry.getKey())) {
                errors.add("excepted header: '" + entry.getKey() + "' not found");
                continue;
            }
            Pattern pattern = Pattern.compile(entry.getValue());
            Matcher matcher = pattern.matcher(desiredRequestHeaders.get(entry.getKey()));
            if (!matcher.matches()) {
                errors.add("header: '" + entry.getKey() + "'='" + desiredRequestHeaders.get(entry.getKey()) + "' is not matches: " + entry.getValue());
            }
        }
        if (!errors.isEmpty()) return String.join("; ", errors);
        return null;
    }

    private String validateJsonBody(RequestWrapper request, Map<String, String> data) {
        if (data.get("body") == null) return null;

        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        if (body.isEmpty()) return "required body is empty";

        try { // only JSON now
            new JSONTokener(body);
        } catch (JSONException e) {
            return e.getMessage();
        }

        JSONObject jsonSchema = new JSONObject(new JSONTokener(data.get("body")));
        JSONObject jsonSubject = new JSONObject(new JSONTokener(body));

        Schema schema = SchemaLoader.load(jsonSchema);
        try {
            schema.validate(jsonSubject);
        } catch (ValidationException e) {
            return e.getMessage();
        }
        return null;
    }
}
