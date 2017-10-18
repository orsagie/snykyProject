package com.snyk.dependency.graph.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.snyk.dependency.graph.client.NPMClient;
import com.snyk.dependency.graph.model.Package;
import com.sun.istack.internal.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DependencyCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyCache.class);

    private final LoadingCache<Package, Set<Package>> packageCache;

    public DependencyCache() {
        this.packageCache = CacheBuilder.newBuilder()
                .expireAfterWrite(7, TimeUnit.DAYS)
                .build(new DependencyCacheLoader());
    }

    public Set<Package> getPackageDependencies(final Package pkg) {
        try {
            return packageCache.get(pkg);
        } catch (Exception e) {
            LOGGER.error(String.format("Error while trying to get package dependencies for [%s]", pkg.toString()), e);
            throw new RuntimeException(e);
        }
    }

    private class DependencyCacheLoader extends CacheLoader<Package, Set<Package>> {
        @Override
        public Set<Package> load(@NotNull final Package pkg) {
            return NPMClient.getPackageDependencies(pkg);
        }
    }
}
