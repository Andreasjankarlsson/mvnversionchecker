package org.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DependencyModel implements MavenDependency{
    private final DependencyType dependencyType = DependencyType.DEPENDENCY;
    private String groupdId;
    private String artifactId;
    private String version;
    private List<DependencyVersion> dependencyVersions;

    public String getLatestVersion(String regex) {
        return DependencyVersion.getLatestVersion(dependencyVersions,regex).get().getVersion();
    }

    @Override
    public boolean newerVersionExist(String regex){
        var newestVersion = DependencyVersion.getLatestVersion(dependencyVersions,regex).get().getVersion();
        return !newestVersion.equals(this.version);
    }
}
