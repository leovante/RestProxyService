package ru.vtb.stub.filter;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.client.ProxyHttpClient;
import io.micronaut.http.filter.FilterChain;
import io.micronaut.http.filter.HttpFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import ru.vtb.stub.config.MyServiceConfiguration;
import ru.vtb.stub.db.dao.EndpointDao;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Requires(property = "rest-proxy-stub.storage-mode", value = "db")
@Filter(Filter.MATCH_ALL_PATTERN)
public class DbRequestFilter implements HttpFilter {

    @Value("${path.data}")
    private String dataPath;
    @Value("${path.response}")
    private String dataResponse;
    @Value("${path.response}")
    private String forwardPath;

    private final EndpointDao endpointDao;
    private final ProxyHttpClient proxyHttpClient;
    private final MyServiceConfiguration myServiceConfiguration;

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(HttpRequest<?> request, FilterChain chain) {
        String uri = request.getUri().getPath();
        String team = uri.split("/")[1];
        String method = request.getMethod().name();
        String requestKey = uri + ":" + method;

        if (uri.equals(dataPath) || uri.equals(dataResponse)) {
            return chain.proceed(request);
        }

        var endpointEntity = endpointDao.getOneByTeam(team);
        if (endpointEntity.isEmpty()) {
            log.info("Не найден путь для редиректа:" + uri);
            return chain.proceed(request);
        }

        String queryString = request.getParameters().values().stream().flatMap(Collection::stream).collect(Collectors.joining("&"));
        String forward = queryString.isEmpty()
                ? forwardPath + "?rpsRequest=" + requestKey + "&rpsKey=" + team
                : forwardPath + "?rpsRequest=" + requestKey + "&rpsKey=" + team + "&" + queryString;

        return proxyHttpClient.proxy(request.mutate()
                .uri(b -> b
                        .scheme("http")
                        .host(myServiceConfiguration.getUrl().getHost())
                        .port(myServiceConfiguration.getUrl().getPort())
                        .replacePath(forward)));
    }

    public int getOrder() {
        return Integer.MAX_VALUE;
    }

}
