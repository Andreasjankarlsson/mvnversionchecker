package org.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.example.service.MavenExistingVersionsService;
import org.example.service.MavenNewerVersionService;

@Mojo(name = "allVersions", defaultPhase = LifecyclePhase.INSTALL)
public class AllVersionsMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    public void execute() {
        getLog().info("All existing dependencies and plugins");
        MavenExistingVersionsService service = new MavenExistingVersionsService(project);
        service.getLogsRowsWithVersions().forEach(logRow -> getLog().info(logRow));
    }
}
