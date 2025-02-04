package org.example.service;

import org.apache.maven.project.MavenProject;
import org.example.comparator.DependencyComparator;
import org.example.comparator.VersionComparator;
import org.example.integration.client.MvnSearchService;
import org.example.model.DependencyModel;
import org.example.model.DependencyVersion;
import org.example.model.MavenDependency;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MavenVersionsService {
    private static final Comparator<MavenDependency> dependencyComparator = new DependencyComparator();
    private static final VersionComparator versionComparator = new VersionComparator();
    private static final MvnSearchService searchService = new MvnSearchService();
    private static final PluginService pluginService = new PluginService();
    private static final DependencyService dependencyService = new DependencyService();

    public MavenVersionsService() {
    }

    public List<MavenDependency> getAllExternalDependencies(List<MavenProject> projects) {
        Set<MavenDependency> dependencies = new TreeSet<>(dependencyComparator);
        Set<MavenDependency> ownModules = new TreeSet<>(dependencyComparator);

        for (var project : projects) {
            MavenDependency module = DependencyModel.builder()
                    .groupdId(project.getGroupId())
                    .artifactId(project.getArtifactId())
                    .version(project.getVersion())
                    .build();
            ownModules.add(module);

            dependencies.addAll(pluginService.getPlugins(project));
            dependencies.addAll(dependencyService.getDependencies(project));
        }

        return dependencies.stream()
                .filter(dep -> !ownModules.contains(dep))
                .sorted(dependencyComparator).toList();
    }

    public List<MavenDependency> getExternalDependenciesWithNewerVersions(List<MavenProject> projects, String regex) {
        List<MavenDependency> dependencies = getAllExternalDependencies(projects);
        for (var dependency : dependencies) {
            DependencyVersion currentVersion = DependencyVersion.builder()
                    .version(dependency.getVersion())
                    .build();

            List<DependencyVersion> versions = searchService.getVersions(dependency)
                    .stream()
                    .filter(version -> version.getVersion().matches(regex))
                    .filter(version -> versionComparator.compare(currentVersion, version) < 0)
                    .sorted(versionComparator)
                    .toList();

            dependency.setDependencyVersions(versions);
        }

        return dependencies.stream()
                .filter(dependency -> !dependency.getDependencyVersions().isEmpty())
                .toList();
    }


    public List<MavenDependency> filterNumberOfVersions(List<MavenDependency> dependencies, int numberOfNewerVersions) {
        return dependencies.stream()
                .filter(dep -> dep.getDependencyVersions().size() > numberOfNewerVersions)
                .toList();
    }
}
