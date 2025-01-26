package org.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.maven.artifact.Artifact;

@Getter
@Setter
@Builder
public class DependencyModel {
    String groupdId;
    String artifactId;
    String version;
    String type;
    String scope;
    String newestVersion;
}
