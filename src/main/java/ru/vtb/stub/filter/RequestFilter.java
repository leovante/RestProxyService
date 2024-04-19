package ru.vtb.stub.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static ru.vtb.stub.data.DataMap.dataByKeyMap;
import static ru.vtb.stub.data.DataMap.dataByRegexMap;

@Slf4j
@Component
@ConditionalOnProperty(value = "false", havingValue = "false")
public class RequestFilter implements Filter {

    @Value("${path.data}")
    private String dataPath;

    @Value("${path.response}")
    private String forwardPath;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        RequestWrapper wrappedRequest = new RequestWrapper((HttpServletRequest) servletRequest);
        String uri = wrappedRequest.getRequestURI();
        String requestKey = uri + ":" + wrappedRequest.getMethod();

        if (uri.equals(dataPath)) {
            filterChain.doFilter(wrappedRequest, servletResponse);
            return;
        }

        boolean containsDataByKey = dataByKeyMap.containsKey(requestKey);

        String regexKey = dataByRegexMap.keySet().stream()
                .filter(requestKey::matches)
                .findFirst()
                .orElse(null);

        String key = containsDataByKey
                ? requestKey
                : (regexKey != null ? URLEncoder.encode(regexKey, StandardCharsets.UTF_8) : null);

        if (key == null) {
            filterChain.doFilter(wrappedRequest, servletResponse);
            return;
        }

        String queryString = wrappedRequest.getQueryString();
        String forward = queryString == null || queryString.isEmpty()
                ? forwardPath + "?rpsRequest=" + requestKey + "&rpsKey=" + key
                : forwardPath + "?rpsRequest=" + requestKey + "&rpsKey=" + key + "&" + queryString;

        wrappedRequest.getRequestDispatcher(forward).forward(wrappedRequest, servletResponse);
    }
}
