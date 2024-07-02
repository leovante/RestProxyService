package ru.vtb.stub.config;

import io.micronaut.context.annotation.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties("micronaut.http.services.my-service")
public class MyServiceConfiguration {
    public static final String ID = "my-service";
    private URI url;

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }
}
