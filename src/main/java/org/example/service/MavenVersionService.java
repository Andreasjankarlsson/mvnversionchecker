package org.example.service;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.example.integration.client.MvnSearchService;
import org.example.mapper.DependencyMapper;
import org.example.mapper.PluginMapper;
import org.example.model.MavenDependency;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MavenVersionService {
    private final MvnSearchService searchService;
    private static String VERSION_MATCH;
    private final MavenProject project;

    public MavenVersionService(MavenProject project, String versionFilter)      {
        this.VERSION_MATCH = versionFilter;
        this.searchService = new MvnSearchService();
        this.project = project;
    }

    public List<String> getLogsRowsWithNewerVersions(){
        List<MavenDependency> mavenDependencies = getMavenDependenciesWithNewerVersions();
        return formatToLogRows(mavenDependencies);
    }

    public List<MavenDependency> getMavenDependenciesWithNewerVersions(){
        List<MavenDependency> mavenDependencies = new ArrayList<>();
        mavenDependencies.addAll(getDependencies(project));
        mavenDependencies.addAll(getPlugins(project));
        mavenDependencies.forEach(dependency -> dependency.setDependencyVersions(searchService.getVersions(dependency)));

        return mavenDependencies .stream()
                .filter(dependency -> dependency.newerVersionExist(VERSION_MATCH))
                .toList();
    }

    private List<MavenDependency> getPlugins(MavenProject project){
        List<String> defaultPluginKeys = List.of(
                "org.apache.maven.plugins:maven-compiler-plugin", // Maven Compiler Plugin
                "org.apache.maven.plugins:maven-surefire-plugin", // Maven Surefire Plugin
                "org.apache.maven.plugins:maven-jar-plugin",      // Maven Jar Plugin
                "org.apache.maven.plugins:maven-install-plugin",  // Maven Install Plugin
                "org.apache.maven.plugins:maven-deploy-plugin",   // Maven Deploy Plugin
                "org.apache.maven.plugins:maven-clean-plugin",    // Maven Clean Plugin
                "org.apache.maven.plugins:maven-site-plugin",     // Maven Site Plugin
                "org.apache.maven.plugins:maven-dependency-plugin", // Maven Dependency Plugin
                "org.apache.maven.plugins:maven-resources-plugin" // Maven Resources Plugin
        );

        PluginMapper pluginMapper = PluginMapper.INSTANCE;
        var plugins = project.getBuildPlugins()
                .stream()
                .filter(plugin -> !defaultPluginKeys.contains(plugin.getKey()))
                .map(plugin -> (MavenDependency) pluginMapper.toPluginModel(plugin))
                .toList();

        return plugins;
    }

    private List<MavenDependency> getDependencies(MavenProject project){
        var test1 = project.getDependencies();
        var test2= project.getBuild();
        DependencyMapper dependencyMapper = DependencyMapper.INSTANCE;

        return  project.getDependencies()
                .stream()
                .map(dependency -> (MavenDependency) dependencyMapper.toDependencyModel(dependency))
                .toList();
    }

    private List<String> formatToLogRows(List<MavenDependency> dependencies){
        List<String> logRows = new LinkedList<>();
        for(var dependency : dependencies){
            String logRow = String.format("%s %s:%s:%s has an newer version (%s)",
                    toTitleCase(dependency.getDependencyType().name()),
                    dependency.getGroupdId(),
                    dependency.getArtifactId(),
                    dependency.getVersion(),
                    dependency.getLatestVersion(VERSION_MATCH));
            logRows.add(logRow);
        }
        return logRows;
    }

    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
