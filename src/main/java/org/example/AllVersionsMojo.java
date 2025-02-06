package org.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.example.model.MavenDependency;
import org.example.service.MavenVersionsService;

import java.util.List;

@Mojo(name = "allVersions", defaultPhase = LifecyclePhase.INSTALL)
public class AllVersionsMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${reactorProjects}", readonly = true)
    private List<MavenProject> reactorProjects;

    public void execute() {
        if (project.isExecutionRoot()) {
            MavenVersionsService service = new MavenVersionsService();
            getLog().info("All external dependencies and plugins");
            getLog().info("Dependency type:Group id:Artifact id:Version ");
            List<MavenDependency> dependencies = service.getAllExternalDependencies(reactorProjects);
            for (var dependency : dependencies) {
                String logRow = String.format("%s:%s:%s:%s",
                        toTitleCase(dependency.getDependencyType().name()),
                        dependency.getGroupdId(),
                        dependency.getArtifactId(),
                        dependency.getVersion());
                getLog().info(logRow);
            }
        }
    }

    public String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
