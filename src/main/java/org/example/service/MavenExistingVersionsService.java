package org.example.service;

import org.apache.maven.project.MavenProject;
import org.example.integration.client.MvnSearchService;
import org.example.mapper.DependencyMapper;
import org.example.mapper.PluginMapper;
import org.example.model.MavenDependency;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MavenExistingVersionsService {
    private final MavenProject project;

    public MavenExistingVersionsService(MavenProject project)      {
        this.project = project;
    }

    public List<String> getLogsRowsWithVersions(){
        List<MavenDependency> mavenDependencies = getMavenDependencies();
        return formatToLogRows(mavenDependencies);
    }

    public List<MavenDependency> getMavenDependencies(){
        List<MavenDependency> mavenDependencies = new ArrayList<>();
        mavenDependencies.addAll(getDependencies(project));
        mavenDependencies.addAll(getPlugins(project));
        return mavenDependencies;
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
        DependencyMapper dependencyMapper = DependencyMapper.INSTANCE;

        return  project.getDependencies()
                .stream()
                .map(dependency -> (MavenDependency) dependencyMapper.toDependencyModel(dependency))
                .toList();
    }

    private List<String> formatToLogRows(List<MavenDependency> dependencies){
        List<String> logRows = new LinkedList<>();
        for(var dependency : dependencies){
            String logRow = String.format("%s:%s:%s:%s",
                    toTitleCase(dependency.getDependencyType().name()),
                    dependency.getGroupdId(),
                    dependency.getArtifactId(),
                    dependency.getVersion());
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
