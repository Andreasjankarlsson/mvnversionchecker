package org.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PluginModel implements  MavenDependency{
    private String groupdId;
    private String artifactId;
    private String version;
    private List<Version> versions;

    public String getLatestVersion(String regex) {
        return Version.getLatestVersion(versions,regex).get().getVersion();
    }

    public boolean newerVersionExist(String regex){
        var newestVersion = Version.getLatestVersion(versions,regex).get().getVersion();
        return !newestVersion.equals(this.version);
    }

}
