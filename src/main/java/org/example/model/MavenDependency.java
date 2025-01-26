package org.example.model;

import java.util.List;

public interface MavenDependency {
    String getGroupdId();
    String getArtifactId();
    String getVersion();
    String getLatestVersion(String regex);
    void setVersions(List<Version> versions);
    boolean newerVersionExist(String regex);
}
