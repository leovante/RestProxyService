package ru.vtb.stub.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnProperty(value = "false", havingValue = "false")
public class HostFilter extends ZuulFilter {

    @Autowired
    private ZuulProperties zuulProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    @SneakyThrows
    public Object run() {
        /*RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        if (body.isEmpty()) {
            log.info("Zuul host filter. {} request to {}", request.getMethod(), request.getRequestURL().toString());
        } else {
            log.info("Zuul host filter. {} request to {} with body {}",
                    request.getMethod(), request.getRequestURL().toString(), body);
        }*/

        return null;
    }

}
