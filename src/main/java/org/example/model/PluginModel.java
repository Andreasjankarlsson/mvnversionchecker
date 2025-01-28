package org.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Builder
public class PluginModel implements  MavenDependency{
    private final DependencyType dependencyType = DependencyType.PLUGIN;
    private String groupdId;
    private String artifactId;
    private String version;
    private List<DependencyVersion> dependencyVersions;

    public String getLatestVersion(String regex) {
        return DependencyVersion.getLatestVersion(dependencyVersions,regex).get().getVersion();
    }

    public boolean newerVersionExist(String regex){
        if(dependencyVersions == null || dependencyVersions.isEmpty()){
            return false;
        }
        Optional<DependencyVersion> optionalNewestVersion =DependencyVersion.getLatestVersion(dependencyVersions,regex);
        if(optionalNewestVersion.isEmpty()){
            return false;
        }
        return !optionalNewestVersion.get().equals(this.version);
    }

}
