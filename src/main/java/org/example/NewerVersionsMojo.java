package org.example;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;
import org.example.exception.EnforceVersionException;
import org.example.model.MavenDependency;
import org.example.service.MavenVersionsService;

import java.util.List;


@Mojo(name = "version", defaultPhase = LifecyclePhase.INSTALL)
public class NewerVersionsMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;
    @Parameter(defaultValue = "${reactorProjects}", readonly = true)
    private List<MavenProject> reactorProjects;
    @Parameter(property = "versionRegex", defaultValue = ".*") //"^\\d+\\.\\d+\\.\\d+$"
    private String versionRegex;
    @Parameter(property = "enforceRule")
    private Integer numberOfAllowedNewerVersions;

    public void execute() {
        if (project.isExecutionRoot()) {
            MavenVersionsService service = new MavenVersionsService();
            List<MavenDependency> dependencies = service.getExternalDependenciesWithNewerVersions(reactorProjects, versionRegex);
            if (dependencies.isEmpty()) {
                getLog().info("No dependencies with newer versions exists");
                return;
            }

            getLog().info("The following dependencies has newer versions");
            if (numberOfAllowedNewerVersions != null) {
                dependencies = service.filterNumberOfVersions(dependencies, numberOfAllowedNewerVersions);
                errorLog(dependencies);
                throw new EnforceVersionException("Maven dependencies has to be updated");
            } else {
                infoLog(dependencies);
            }
        }
    }


    private String toTitleCase(String input) {
        if (StringUtils.isBlank(input)) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    private void infoLog(List<MavenDependency> dependencies) {
        int longestKey = getLongestKey(dependencies);
        for (var dependency : dependencies) {
            String leftSide = String.format("%s:%s:%s)",
                    toTitleCase(dependency.getDependencyType().name()),
                    dependency.getGroupdId(),
                    dependency.getArtifactId());

            String rightSide = String.format("(%s -> %s)",
                    dependency.getVersion(),
                    dependency.getDependencyVersions().get(0).getVersion());
            int totalLength = longestKey + 2;
            int dotsCount = totalLength - leftSide.length();
            String dots = ".".repeat(dotsCount);
            getLog().info(leftSide + dots + rightSide);
        }
    }

    private void errorLog(List<MavenDependency> dependencies) {
        int longestKey = getLongestKey(dependencies);
        for (var dependency : dependencies) {
            // Combine groupId and artifactId
            String leftSide = String.format("%s:%s", dependency.getGroupdId(), dependency.getArtifactId());
            String rightSide = String.format("%s newer versions exists (%s -> %s)",
                    dependency.getDependencyVersions().size(),
                    dependency.getVersion(),
                    dependency.getDependencyVersions().get(0).getVersion());
            int totalLength = longestKey + 2;
            int dotsCount = totalLength - leftSide.length();
            String dots = ".".repeat(dotsCount);

            getLog().error(leftSide + dots + rightSide);
        }
    }

    private int getLongestKey(List<MavenDependency> dependencies) {
        int longestKey = 0;
        for (var dependency : dependencies) {
            int keyLength = String.format("%s:%s",
                            dependency.getGroupdId(),
                            dependency.getArtifactId())
                    .length();
            if (longestKey < keyLength) {
                longestKey = keyLength;
            }
        }
        return longestKey;
    }
}

