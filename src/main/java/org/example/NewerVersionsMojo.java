package org.example;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.example.model.MavenDependency;
import org.example.service.MavenExistingVersionsService;

import java.util.List;


@Mojo(name = "version", defaultPhase = LifecyclePhase.INSTALL)
public class NewerVersionsMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;
    @Parameter(property = "versionRegex", defaultValue = ".*") //"^\\d+\\.\\d+\\.\\d+$"
    private String versionRegex;
    @Parameter(defaultValue = "${reactorProjects}", readonly = true)
    private List<MavenProject> reactorProjects;

    public void execute() {
        if (project.isExecutionRoot()) {
            MavenExistingVersionsService service = new MavenExistingVersionsService();
            List<MavenDependency> dependencies = service.getDependenciesWithNewerVersions(reactorProjects, versionRegex);
            if (dependencies.isEmpty()) {
                getLog().info("No dependencies with newer versions exists");
            } else {
                getLog().info("The following dependencies has newer versions");
            }

            for (var dependency : dependencies) {
                String logRow = String.format("%s:%s:%s:(%s -> %s)",
                        toTitleCase(dependency.getDependencyType().name()),
                        dependency.getGroupdId(),
                        dependency.getArtifactId(),
                        dependency.getVersion(),
                        dependency.getDependencyVersions().get(0).getVersion());
                getLog().info(logRow);
            }
        }

        //getLog().info("Starting checking versions, with versionRegex: " + versionRegex);
        //MavenNewerVersionService versionService = new MavenNewerVersionService(project, versionRegex);
        //versionService.getLogsRowsWithNewerVersions().forEach(logRow -> getLog().warn(logRow));
    }

    public String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}

