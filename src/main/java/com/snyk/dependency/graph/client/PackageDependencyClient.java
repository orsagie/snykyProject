package com.snyk.dependency.graph.client;

import com.snyk.dependency.graph.model.Package;

import java.util.List;

public interface PackageDependencyClient {
    List<Package> getPackageDependencies(final Package pkg);
}
