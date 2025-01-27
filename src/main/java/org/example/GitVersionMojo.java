package org.example;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.example.service.MavenVersionService;



@Mojo(name = "version", defaultPhase = LifecyclePhase.COMPILE)
public class GitVersionMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;
    private static String VERSION_MATCH = "^\\d+\\.\\d+\\.\\d+$";

    public void execute() {
        MavenVersionService versionService = new MavenVersionService(project, VERSION_MATCH);
        versionService.getLogsRowsWithNewerVersions().forEach(logRow -> getLog().info(logRow));
    }
}
