package com.client.factory;

import org.apache.http.HttpHost;
import org.apache.http.client.AuthCache;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class RestTemplateFactory implements FactoryBean<RestTemplate>, InitializingBean {

    @Value("${rest.server}")
    private String restServer;

    @Value("${rest.server.user}")
    private String restServerUser;

    @Value("${rest.server.password}")
    private String restServerPassword;

    private RestTemplate restTemplate;

    public RestTemplate getObject() {
        return restTemplate;
    }

    public Class<RestTemplate> getObjectType() {
        return RestTemplate.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() {
        HttpHost host = new HttpHost(restServer);
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactoryBasicAuth(host));

        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(restServerUser, restServerPassword));
    }

    class HttpComponentsClientHttpRequestFactoryBasicAuth extends HttpComponentsClientHttpRequestFactory {

        private HttpHost host;

        public HttpComponentsClientHttpRequestFactoryBasicAuth(HttpHost host) {
            super();
            this.host = host;
        }

        protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
            return createHttpContext();
        }

        private HttpContext createHttpContext() {
            AuthCache authCache = new BasicAuthCache();
            BasicScheme basicAuth = new BasicScheme();
            authCache.put(host, basicAuth);
            BasicHttpContext localcontext = new BasicHttpContext();
            localcontext.setAttribute(HttpClientContext.AUTH_CACHE, authCache);
            return localcontext;
        }
    }
}