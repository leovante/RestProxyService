package ru.vtb.stub.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.repository.EndpointRepository;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RequestFilter implements Filter {

    @Value("${path.data}")
    private String dataPath;

    @Value("${path.response}")
    private String forwardPath;

    @Autowired
    private EndpointRepository endpointRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        RequestWrapper wrappedRequest = new RequestWrapper((HttpServletRequest) servletRequest);

        if (wrappedRequest.getRequestURI().equals(dataPath)) {
            filterChain.doFilter(wrappedRequest, servletResponse);
            return;
        }

        String requestKey = wrappedRequest.getRequestURI() + ":" + wrappedRequest.getMethod();

        Set<String> dbKeys = endpointRepository.findAll().stream()
                .map(this::mapToKey)
                .collect(Collectors.toSet());

        String dataKey = dbKeys.contains(requestKey)
                ? requestKey
                : dbKeys.stream()
                    .filter(requestKey::matches)
                    .findFirst()
                    .orElse(null);

        if (Objects.isNull(dataKey)) {
            filterChain.doFilter(wrappedRequest, servletResponse);
            return;
        }

        String forward = ObjectUtils.isEmpty(wrappedRequest.getQueryString())
                ? forwardPath + "?rpsRequest=" + requestKey + "&rpsKey=" + dataKey
                : forwardPath + "?rpsRequest=" + requestKey + "&rpsKey=" + dataKey + "&" + wrappedRequest.getQueryString();

        wrappedRequest.getRequestDispatcher(forward).forward(wrappedRequest, servletResponse);
    }

    private String mapToKey (EndpointEntity endpoint) {
        return endpoint.getPrimaryKey().getPath() + ":" + endpoint.getPrimaryKey().getMethod();
    }
}
