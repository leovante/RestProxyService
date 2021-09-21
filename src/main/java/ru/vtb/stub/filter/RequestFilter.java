package ru.vtb.stub.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static ru.vtb.stub.data.DataMap.dataMap;

@Slf4j
@Component
public class RequestFilter implements Filter {

    @Value("${path.data}")
    private String adminPath;
    @Value("${path.response}")
    private String redirectPath;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RequestWrapper wrappedRequest = new RequestWrapper((HttpServletRequest) servletRequest);
        String uri = wrappedRequest.getRequestURI();
        String method = wrappedRequest.getMethod();
        String key = uri + ":" + method;

        if (uri.equals(adminPath) || !dataMap.containsKey(key)) {
            filterChain.doFilter(wrappedRequest, servletResponse);
            return;
        }
        String queryString = wrappedRequest.getQueryString();
        String forward = queryString == null || queryString.isEmpty()
                ? redirectPath + "?key=" + key
                : redirectPath + "?key=" + key + "&" + queryString;
        wrappedRequest.getRequestDispatcher(forward).forward(wrappedRequest, servletResponse);
    }
}
