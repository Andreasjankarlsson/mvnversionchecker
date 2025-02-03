package org.example.service;

import org.apache.maven.project.MavenProject;
import org.example.mapper.PluginMapper;
import org.example.model.MavenDependency;

import java.util.List;

public class PluginService {
    private static PluginMapper pluginMapper = PluginMapper.INSTANCE;

    public List<MavenDependency> getPlugins(MavenProject project) {
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


        var plugins = project.getBuildPlugins()
                .stream()
                .filter(plugin -> !defaultPluginKeys.contains(plugin.getKey()))
                .map(plugin -> (MavenDependency) pluginMapper.toPluginModel(plugin))
                .toList();

        return plugins;
    }
}
