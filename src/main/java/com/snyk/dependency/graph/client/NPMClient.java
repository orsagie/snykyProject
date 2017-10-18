package com.snyk.dependency.graph.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snyk.dependency.graph.model.Package;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NPMClient {

    private static final String PACKAGE_REGISTRY_URL = "http://registry.npmjs.org/%s/%s";

    public static Set<Package> getPackageDependencies(final Package pkg) {
        final Set<Package> packages = new HashSet<>();
        try {
            final HttpClient client = HttpClientBuilder.create().build();
            final HttpResponse response = client.execute(buildRequest(pkg));
            final ObjectMapper objectMapper = new ObjectMapper();
            final Map<String, Object> responseMap = objectMapper.readValue(response.getEntity().getContent(), Map.class);
            if (responseMap.containsKey("dependencies")) {
                final Map<String, String> dependencyMap = objectMapper.readValue(objectMapper.writeValueAsString(responseMap.get("dependencies")), Map.class);

                for (final Map.Entry<String, String> entry : dependencyMap.entrySet()) {
                    packages.add(new Package(entry.getKey(), entry.getValue()));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return packages;
    }

    private static HttpGet buildRequest(final Package pkg) throws Exception {

        final HttpGet get = new HttpGet(
                String.format(PACKAGE_REGISTRY_URL,
                        URLEncoder.encode(pkg.getPackageName(), "UTF-8"),
                        URLEncoder.encode(pkg.getPackageVersion(), "UTF-8")));
        get.setHeader("Content-type", "application/json");
        return get;
    }
}
