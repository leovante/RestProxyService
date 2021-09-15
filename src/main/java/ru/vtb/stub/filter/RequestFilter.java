package ru.vtb.stub.filter;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.vtb.stub.data.ResponseData.dataMap;

@Slf4j
@Setter
@Component
public class RequestFilter implements Filter {

    @Value("${path.admin}")
    private String adminPath;
    @Value("${path.response}")
    private String redirectPath;
    @Value("${response.error.message}")
    private String defaultErrorMessage;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // Without wrapper - exception:
        // java.lang.IllegalStateException: getReader() has already been called for this request
        // in filterChain.doFilter(servletRequest, servletResponse);
        RequestWrapper wrappedRequest = new RequestWrapper((HttpServletRequest) servletRequest);
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String key = wrappedRequest.getRequestURI() + ":" + wrappedRequest.getMethod();

        if (wrappedRequest.getRequestURI().equals(adminPath) || !dataMap.containsKey(key)) {
            filterChain.doFilter(wrappedRequest, servletResponse);
            return;
        }

        var data = dataMap.get(key);

        if (data.getValidate() != null) {
            log.info("Validate request - TBD");
            response.sendError(500, "Validate request - TBD");
            return;
        }
        if (data.getError() != null) {
            log.info("Request to: {} --> Response with error: {}", key, data.getError().getStatus());
            response.sendError(data.getError().getStatus(), defaultErrorMessage);
            return;
        }
        if (data.getResponse() != null) {
            log.debug("Request to: {} --> Redirect to Response controller", key);
            wrappedRequest.getRequestDispatcher(redirectPath + "?key=" + key).forward(servletRequest, servletResponse);
        }


        // Cannot set admin routes
//        if (admin.containsValue(request.getRequestURI())) {
//            String queryString = request.getQueryString();
//            if (!queryString.contains("key=")) {
//                String error = "Request filter. Request to: " + request.getRequestURI() + " without required param 'key'";
//                log.error(error);
//                response.sendError(500, error);
//                return;
//            }
//            var requestQueryParams = getRequestQueryParams(queryString);
//            String routeToSet = decode(requestQueryParams.get("key"), UTF_8.name()).split(KEY_DELIMITER)[0];
//            if (admin.containsValue(routeToSet)) {
//                String error = "Route: " + routeToSet + " is admin route. See application.yaml --> path.admin";
//                log.error(error);
//                response.sendError(500, error);
//                return;
//            }
//        } else
//            log.info("Request filter. Request to: {}", key);

//        if (validateData.get(key) != null) {
//            List<String> errors = new ArrayList<>();
//            errors.add(validateQueryParams(wrappedRequest, validateData.get(key)));
//            errors.add(validateHeaders(wrappedRequest, validateData.get(key)));
//            errors.add(validateJsonBody(wrappedRequest, validateData.get(key)));
//            errors.removeAll(Collections.singleton(null));
//            if (!errors.isEmpty()) {
//                String errorMessage = String.join("; ", errors);
//                log.error("\tValidation errors: {}", errorMessage);
//                response.sendError(500, errorMessage);
//                return;
//            }
//            log.info("\tSuccessful request validation");
//        }

//        if (errorData.get(key) != null) {
//            var error = errorData.get(key);
//            var status = (Integer) error.get("status");
//            String message = error.get("message") != null ? (String) error.get("message") : defaultErrorMessage;
//            log.error("\tFound data to send error message. Status code: {}. Message: {}", status, message);
//            response.sendError(status, message);
//            return;
//        }
//
//        if (responseData.get(key) != null) {
//            log.info("\tRedirect to Response data controller");
//            wrappedRequest.getRequestDispatcher(redirectPath + "?key=" + key).forward(servletRequest, servletResponse);
//        } else
//            filterChain.doFilter(wrappedRequest, servletResponse);
    }

//    private String validateQueryParams(RequestWrapper wrappedRequest, Map<String, Object> data) {
//        var exceptedQueryParams = data.keySet().stream()
//                .filter(k -> k.startsWith(queryPrefix))
//                .collect(Collectors.toMap(k -> k.split(queryPrefix, 2)[1], data::get));
//
//        if (exceptedQueryParams.isEmpty()) return null;
//
//        if (wrappedRequest.getQueryString() == null) return "empty required query params";
//        var requestQueryParams = getRequestQueryParams(wrappedRequest.getQueryString());
//
//        List<String> errors = new ArrayList<>();
//        for (var entry : exceptedQueryParams.entrySet()) {
//            if (!requestQueryParams.containsKey(entry.getKey())) {
//                errors.add("excepted param: '" + entry.getKey() + "' not found");
//                continue;
//            }
//            Pattern pattern = Pattern.compile((String) entry.getValue());
//            Matcher matcher = pattern.matcher(requestQueryParams.get(entry.getKey()));
//            if (!matcher.matches()) {
//                errors.add("param: '" + entry.getKey() + "' is not matches: " + entry.getValue());
//            }
//        }
//        if (!errors.isEmpty()) return String.join("; ", errors);
//        return null;
//    }
//
//    private String validateHeaders(HttpServletRequest request, Map<String, Object> data) {
//        var exceptedHeaders = data.keySet().stream()
//                .filter(k -> k.startsWith(headerPrefix))
//                .collect(Collectors.toMap(k -> k.split(headerPrefix, 2)[1], data::get));
//        if (exceptedHeaders.isEmpty()) return null;
//
//        var requestHeaderNames = request.getHeaderNames();
//        Map<String, String> desiredRequestHeaders = new HashMap<>();
//        while (requestHeaderNames.hasMoreElements()) {
//            String headerName = requestHeaderNames.nextElement();
//            if (exceptedHeaders.containsKey(headerName))
//                desiredRequestHeaders.put(headerName, request.getHeader(headerName));
//        }
//        if (desiredRequestHeaders.isEmpty()) return "empty required headers";
//
//        List<String> errors = new ArrayList<>();
//        for (var entry : exceptedHeaders.entrySet()) {
//            if (!desiredRequestHeaders.containsKey(entry.getKey())) {
//                errors.add("excepted header: '" + entry.getKey() + "' not found");
//                continue;
//            }
//            Pattern pattern = Pattern.compile((String) entry.getValue());
//            Matcher matcher = pattern.matcher(desiredRequestHeaders.get(entry.getKey()));
//            if (!matcher.matches()) {
//                errors.add("header: '" + entry.getKey() + "'='" + desiredRequestHeaders.get(entry.getKey()) + "' is not matches: " + entry.getValue());
//            }
//        }
//        if (!errors.isEmpty()) return String.join("; ", errors);
//        return null;
//    }
//
//    private String validateJsonBody(RequestWrapper request, Map<String, Object> data) {
//        if (data.get("body") == null) return null;
//
//        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//        if (body.isEmpty()) return "required body is empty";
//
//        try { // only JSON now
//            new JSONTokener(body);
//        } catch (JSONException e) {
//            return e.getMessage();
//        }
//
//        JSONObject jsonSchema = new JSONObject(new JSONTokener((String) data.get("body")));
//        JSONObject jsonSubject = new JSONObject(new JSONTokener(body));
//
//        Schema schema = SchemaLoader.load(jsonSchema);
//        try {
//            schema.validate(jsonSubject);
//        } catch (ValidationException e) {
//            return e.getMessage();
//        }
//        return null;
//    }
}
