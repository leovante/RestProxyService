package ru.vtb.stub.filter;

import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ru.vtb.stub.data.ResponseData.*;

@Slf4j
@Component
public class RequestFilter implements Filter {

    @Value("${path.response}")
    private String redirectPath;

    @Value("${response.error.message}")
    private String defaultErrorMessage;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String key = request.getRequestURI() + ":" + request.getMethod();

        if (validateData.get(key) != null) {
            List<String> errors = new ArrayList<>();

            errors.add(validateQueryParams(request, validateData.get(key)));
            errors.add(validateHeaders(request, validateData.get(key)));
            errors.add(validateJsonBody(request, validateData.get(key)));
            errors.removeAll(Collections.singleton(null));

            if (!errors.isEmpty()) {
                String errorMessage = String.join("; ", errors);
                log.info("Request filter. Found validate request errors: {}", errorMessage);
                response.sendError(500, errorMessage);
                return;
            }

            log.info("Request filter. Request successfully validated");
        }

        if (errorData.get(key) != null) {
            var error = errorData.get(key);
            var status = (Integer) error.get("status");
            log.info("Request filter. Found error status code: {}", status);

            String message = (String) error.get("message");
            if (message != null)
                log.info("Request filter. Found error message: {}", message);
            else
                message = defaultErrorMessage;

            log.info("Request filter. Throw exception. Status code: {}", status);
            response.sendError(status, message);
            return;
        }

        if (responseData.get(key) != null) {
            log.info("Request filter. Found response data for key: {}. Redirect to Response data controller", key);
            request.getRequestDispatcher(redirectPath + "?key=" + key).forward(servletRequest, servletResponse);
        } else
            filterChain.doFilter(servletRequest, servletResponse);
    }

    private String validateQueryParams(HttpServletRequest request, Map<String, String> data) {
        var exceptedQueryParams = data.keySet().stream()
                .filter(k -> k.startsWith("query-"))
                .collect(Collectors.toMap(k -> k.split("-", 2)[1], data::get));

        if (exceptedQueryParams.isEmpty()) return null;

        if (request.getQueryString() == null) return "Empty required query params";

        List<String> errors = new ArrayList<>();
        var requestQueryParams = Arrays.stream(request.getQueryString().split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(param -> param[0], param -> param[1]));
        for (var entry : exceptedQueryParams.entrySet()) {
            if (!requestQueryParams.containsKey(entry.getKey())) {
                errors.add("Excepted param: '" + entry.getKey() + "' not found");
                continue;
            }
            Pattern pattern = Pattern.compile(entry.getValue());
            Matcher matcher = pattern.matcher(requestQueryParams.get(entry.getKey()));
            if (!matcher.matches()) {
                errors.add("Param: '" + entry.getKey() + "' is not matches: " + entry.getValue());
            }
        }
        if (!errors.isEmpty()) return String.join("; ", errors);

        return null;
    }

    private String validateHeaders(HttpServletRequest request, Map<String, String> data) {
        var exceptedHeaders = data.keySet().stream()
                .filter(k -> k.startsWith("header-"))
                .collect(Collectors.toMap(k -> k.split("-", 2)[1], data::get));
        if (exceptedHeaders.isEmpty()) return null;

        var requestHeaderNames = request.getHeaderNames();
        Map<String, String> desiredRequestHeaders = new HashMap<>();
        while (requestHeaderNames.hasMoreElements()) {
            String headerName = requestHeaderNames.nextElement();
            if (exceptedHeaders.containsKey(headerName))
                desiredRequestHeaders.put(headerName, request.getHeader(headerName));
        }
        if (desiredRequestHeaders.isEmpty()) return "Empty required headers";

        List<String> errors = new ArrayList<>();
        for (var entry : exceptedHeaders.entrySet()) {
            if (!desiredRequestHeaders.containsKey(entry.getKey())) {
                errors.add("Excepted header: '" + entry.getKey() + "' not found");
                continue;
            }
            Pattern pattern = Pattern.compile(entry.getValue());
            Matcher matcher = pattern.matcher(desiredRequestHeaders.get(entry.getKey()));
            if (!matcher.matches()) {
                errors.add("Header: '" + entry.getKey() + "' is not matches: " + entry.getValue());
            }
        }
        if (!errors.isEmpty()) return String.join("; ", errors);

        return null;
    }

    private String validateJsonBody(HttpServletRequest request, Map<String, String> data) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        if (body.isEmpty()) return null;

        JSONObject jsonSchema = new JSONObject(new JSONTokener(data.get("body")));

        try {
            new JSONTokener(body);
        } catch (JSONException e) {
            return e.getMessage();
        }
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
