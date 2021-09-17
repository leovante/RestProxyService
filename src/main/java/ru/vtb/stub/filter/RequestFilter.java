package ru.vtb.stub.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.vtb.stub.data.DataMap.dataMap;

@Slf4j
@Component
public class RequestFilter implements Filter {

    @Value("${path.admin}")
    private String adminPath;
    @Value("${path.response}")
    private String redirectPath;

    private static final String ERROR_MESSAGE = "Stub test error message";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RequestWrapper wrappedRequest = new RequestWrapper((HttpServletRequest) servletRequest);
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String key = wrappedRequest.getRequestURI() + ":" + wrappedRequest.getMethod();

        if (wrappedRequest.getRequestURI().equals(adminPath) || !dataMap.containsKey(key)) {
            filterChain.doFilter(wrappedRequest, servletResponse);
            return;
        }

        var data = dataMap.get(key);

        if (data.getError() != null) {
            log.info("Request to: {} --> Response with error: {}", key, data.getError().getStatus());
            response.sendError(data.getError().getStatus(), ERROR_MESSAGE);
            return;
        }
        if (data.getResponse() != null) {
            log.debug("Request to: {} --> Redirect to Response controller", key);
            String queryString = wrappedRequest.getQueryString();
            String forward = queryString == null || queryString.isEmpty()
                    ? redirectPath + "?key=" + key
                    : redirectPath + "?key=" + key + "&" + queryString;
            wrappedRequest.getRequestDispatcher(forward).forward(wrappedRequest, servletResponse);
        }
    }
}
