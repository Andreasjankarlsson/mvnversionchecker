package org.example;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.example.service.MavenNewerVersionService;



@Mojo(name = "version", defaultPhase = LifecyclePhase.INSTALL)
public class NewerVersionsMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;
    @Parameter(property = "versionRegex", defaultValue = ".*") //"^\\d+\\.\\d+\\.\\d+$"
    private String versionRegex;

    public void execute() {
        getLog().info("Starting checking versions, with versionRegex: " + versionRegex);
        MavenNewerVersionService versionService = new MavenNewerVersionService(project, versionRegex);
        versionService.getLogsRowsWithNewerVersions().forEach(logRow -> getLog().warn(logRow));
    }
}

