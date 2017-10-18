package com.snyk.dependency.graph.client;

import com.snyk.dependency.graph.model.Package;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class MavenClient implements PackageDependencyClient {
    @Override
    public List<Package> getPackageDependencies(final Package pkg) {
        throw new NotImplementedException();
    }
}
