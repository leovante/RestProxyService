package ru.vtb.stub.filter;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.ClientFilterChain;
import io.micronaut.http.filter.HttpClientFilter;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

import static ru.vtb.stub.data.DataMap.dataByKeyMap;
import static ru.vtb.stub.data.DataMap.dataByRegexMap;

@Slf4j
@RequiredArgsConstructor
@Requires(property = "rest-proxy-stub.storage-mode", value = "ram")
@Filter("/**")
public class RequestFilter implements HttpClientFilter {

    @Value("${path.data}")
    private String dataPath;
    @Value("${path.response}")
    private String forwardPath;

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest servletRequest, ClientFilterChain filterChain) {
        String uri = servletRequest.getUri().getPath();
        String requestKey = uri + ":" + servletRequest.getMethod();

        if (uri.equals(dataPath)) {
            return filterChain.proceed(servletRequest);
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
            return filterChain.proceed(servletRequest);
        }

        String queryString = servletRequest.getParameters().values().stream().flatMap(Collection::stream).collect(Collectors.joining("&"));
        String forward = queryString.isEmpty()
                ? forwardPath + "?rpsRequest=" + requestKey + "&rpsKey=" + key
                : forwardPath + "?rpsRequest=" + requestKey + "&rpsKey=" + key + "&" + queryString;

        changeUrl(servletRequest, forward);
        return filterChain.proceed(servletRequest);
    }

    private void changeUrl(MutableHttpRequest servletRequest, String forward) {
        servletRequest.uri(URI.create(forward));
    }

    public int getOrder() {
        return Integer.MAX_VALUE;
    }

}
