package org.example;


import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.version.Version;
import org.example.model.DependencyModel;

import java.util.List;
import java.util.Optional;

/**
 * An example Maven Mojo that resolves the current project's git revision and adds
 * that a new {@code exampleVersion} property to the current Maven project.
 */
@Mojo(name = "version", defaultPhase = LifecyclePhase.COMPILE)
public class GitVersionMojo extends AbstractMojo {
    private static final String OSS_INDEX_URL = "https://ossindex.sonatype.org/api/v3/component-report";

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${repositorySystemSession}", readonly = true)
    private RepositorySystemSession repoSession;

    @Component
    private RepositorySystem repoSystem;

    public void execute() throws MojoExecutionException {
        List<Dependency> dependencies = project.getDependencies();

        for (Dependency dependency : dependencies) {

            DependencyModel dependencyModel = DependencyModel.builder()
                    .groupdId(dependency.getGroupId())
                    .artifactId(dependency.getArtifactId())
                    .version(dependency.getVersion())
                    .type(dependency.getType())
                    .scope(dependency.getScope())
                    .build();

            String packageUrl = String.format("pkg:maven/%s/%s@%s",
                    dependencyModel.getGroupdId(), dependency.getArtifactId(), dependency.getVersion());
            String url = OSS_INDEX_URL + "?coordinates=" + packageUrl;

            dependencyModel.setNewestVersion(getNewestVersion(dependencyModel));
            String output = String.format("(%s)->(%s)",
                    dependencyModel.getVersion(),
                    dependencyModel.getNewestVersion());
            getLog().info("Newer version available for " + dependency.getArtifactId() + ":" +output);
        }
    }

    private String getNewestVersion(DependencyModel dependency){
        Artifact artifact = new DefaultArtifact(dependency.getGroupdId(),
                dependency.getArtifactId(),
                null,
                "[0,)");
        VersionRangeRequest rangeRequest = new VersionRangeRequest();
        rangeRequest.setArtifact(artifact);
        rangeRequest.setRepositories(project.getRemoteProjectRepositories());
        try {
            VersionRangeResult rangeResult = repoSystem.resolveVersionRange(repoSession, rangeRequest);
            Version highestVersion = rangeResult.getHighestVersion();
            return highestVersion.toString();

        } catch (VersionRangeResolutionException e) {
            getLog().warn("Failed to retrieve version range for " + dependency.getArtifactId(), e);
        }
        return null;
    }
}
