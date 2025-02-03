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

public class MavenExistingVersionsService {
    private MavenProject project;

    public MavenExistingVersionsService() {

    }

    public List<MavenDependency> getAllDependencies(List<MavenProject> projects) {
        Comparator<MavenDependency> comparator = new DependencyComparator();
        Set<MavenDependency> dependencies = new TreeSet<>(comparator);
        Set<MavenDependency> ownModules = new TreeSet<>(comparator);
        PluginService pluginService = new PluginService();
        DependencyService dependencyService = new DependencyService();

        for (var proj : projects) {
            MavenDependency module = DependencyModel.builder()
                    .groupdId(proj.getGroupId())
                    .artifactId(proj.getArtifactId())
                    .version(proj.getVersion())
                    .build();
            ownModules.add(module);

            dependencies.addAll(pluginService.getPlugins(proj));
            dependencies.addAll(dependencyService.getDependencies(proj));
        }

        return dependencies.stream()
                .filter(dep -> !ownModules.contains(dep))
                .sorted(comparator).toList();
    }

    public List<MavenDependency> getDependenciesWithNewerVersions(List<MavenProject> projects, String regex) {
        MvnSearchService searchService = new MvnSearchService();
        VersionComparator versionComparator = new VersionComparator();

        List<MavenDependency> dependencies = getAllDependencies(projects);
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


}
