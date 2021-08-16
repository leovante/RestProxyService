package ru.vtb.stub.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.vtb.stub.data.ResponseData.errorData;
import static ru.vtb.stub.data.ResponseData.responseData;

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

        var error = errorData.get(key);
        if (error != null) {
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

        var data = responseData.get(key);
        if (data != null) {
            log.info("Request filter. Found response data for key: {}. Redirect to Response data controller", key);
            request.getRequestDispatcher(redirectPath + "?key=" + key).forward(servletRequest, servletResponse);
        } else
            filterChain.doFilter(servletRequest, servletResponse);
    }
}
