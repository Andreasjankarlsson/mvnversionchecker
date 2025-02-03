package org.example.model;

import java.util.List;

public interface MavenDependency {
    DependencyType getDependencyType();

    String getGroupdId();

    String getArtifactId();

    String getVersion();

    String getLatestVersion(String regex);

    List<DependencyVersion> getDependencyVersions();

    void setDependencyVersions(List<DependencyVersion> dependencyVersions);

    boolean newerVersionExist(String regex);
}
