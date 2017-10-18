package com.snyk.dependency.graph.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Set;

public class Package {
    @JsonProperty
    private String packageName;
    @JsonProperty
    private String packageVersion;
    @JsonProperty
    private Set<Package> dependencies;

    public Package(String packageName, String packageVersion) {
        this.packageName = packageName;
        this.packageVersion = packageVersion;
        this.dependencies = new HashSet<>();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public Set<Package> getDependencies() {
        return dependencies;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return toString().equals(other);
    }
}
