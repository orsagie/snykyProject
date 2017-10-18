package com.snyk.dependency.graph.builder;

import com.snyk.dependency.graph.cache.DependencyCache;
import com.snyk.dependency.graph.model.Package;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DependencyGraphBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyGraphBuilder.class);

    private static Map<String, Package> dependencyMap;
    private static DependencyCache dependencyCache = new DependencyCache();

    public static Package buildDependencies(String packageName, String packageVersion) {
        LOGGER.info("Building dependency tree for package[{}:{}]", packageName, packageVersion);
        dependencyMap = new HashMap<>();

        final Package headPackage = new Package(packageName, packageVersion);

        dependencyMap.put(packageName, headPackage);

        buildInternal(headPackage);
        return dependencyMap.get(packageName);
    }

    private static Set<Package> buildInternal(final Package pkg) {
        LOGGER.info("Processing dependencies for {}", pkg);
        Set<Package> npmDependencies = dependencyCache.getPackageDependencies(pkg);
        if (npmDependencies.isEmpty()) {
            LOGGER.info("No dependencies for {}", pkg);
            return new HashSet<>();
        }

        LOGGER.info("Found {} dependencies for {}", npmDependencies.size(), pkg);
        for (final Package npmDependency : npmDependencies) {
            if (!dependencyMap.containsKey(npmDependency.getPackageName())) {
                dependencyMap.put(npmDependency.getPackageName(), npmDependency);
                final Set<Package> foundDependencies = buildInternal(npmDependency);
                dependencyMap.get(npmDependency.getPackageName()).getDependencies().addAll(foundDependencies);
            }
            final Package dependencyToAdd = dependencyMap.get(npmDependency.getPackageName());
            pkg.getDependencies().add(dependencyToAdd);
        }
        return pkg.getDependencies();
    }
}
