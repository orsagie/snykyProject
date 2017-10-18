package com.snyk.dependency.graph.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snyk.dependency.graph.builder.DependencyGraphBuilder;

import static spark.Spark.get;

public class DependencyGraphResource {
    public static void main(String[] args) {
        get("/package/:package/version/:version/dependencies", (req, res) ->
                DependencyGraphBuilder.buildDependencies(req.params(":package"), req.params(":version")), getMapper()::writeValueAsString);
    }

    private static ObjectMapper getMapper() {
        return new ObjectMapper();
    }
}
