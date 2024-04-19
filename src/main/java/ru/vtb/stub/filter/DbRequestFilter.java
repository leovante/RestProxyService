package ru.vtb.stub.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vtb.stub.db.dao.EndpointDao;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DbRequestFilter implements Filter {

    @Value("${path.data}")
    private String dataPath;

    @Value("${path.response}")
    private String forwardPath;

    private final EndpointDao endpointDao;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        RequestWrapper wrappedRequest = new RequestWrapper((HttpServletRequest) servletRequest);
        String uri = wrappedRequest.getRequestURI();
        String method = wrappedRequest.getMethod();
        String team = uri.split("/")[1];
        String requestKey = uri + ":" + method;

        if (uri.equals(dataPath)) {
            filterChain.doFilter(wrappedRequest, servletResponse);
            return;
        }

        var endpointEntity = endpointDao.getOneByTeam(team);
        if (endpointEntity.isEmpty()) {
            filterChain.doFilter(wrappedRequest, servletResponse);
            return;
        }

        String forward = Optional.ofNullable(wrappedRequest.getQueryString())
                .map(it -> forwardPath + "?rpsRequest=" + requestKey + "&rpsKey=" + team + "&" + it)
                .orElse(forwardPath + "?rpsRequest=" + requestKey + "&rpsKey=" + team);

        wrappedRequest.getRequestDispatcher(forward).forward(wrappedRequest, servletResponse);
    }
}
