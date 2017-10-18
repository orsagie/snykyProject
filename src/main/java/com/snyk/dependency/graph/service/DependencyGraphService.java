package com.snyk.dependency.graph.service;

import static spark.Spark.get;

public class DependencyGraphService {

    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
    }
}
