package com.fullcycle.catalogo.infrastructure.configuration.properties;

public class RestClientProperties {

    private String baseUrl;

    private int readTimeout;

    public String baseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int readTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
