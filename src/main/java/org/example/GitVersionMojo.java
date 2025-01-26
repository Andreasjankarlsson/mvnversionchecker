package org.example;


import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.example.integration.client.MvnSearchService;
import org.example.mapper.DependencyMapper;
import org.example.mapper.PluginMapper;
import org.example.model.DependencyModel;
import org.example.model.MavenDependency;

import java.util.List;

/**
 * An example Maven Mojo that resolves the current project's git revision and adds
 * that a new {@code exampleVersion} property to the current Maven project.
 */
@Mojo(name = "version", defaultPhase = LifecyclePhase.COMPILE)
public class GitVersionMojo extends AbstractMojo {
    private static final String OSS_INDEX_URL = "https://ossindex.sonatype.org/api/v3/component-report";

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    private MvnSearchService searchService = new MvnSearchService();
    private static String VERSION_MATCH = "^\\d+\\.\\d+\\.\\d+$";

    public void execute() {
        List<MavenDependency> test5 = getDependencies(project);
        List<MavenDependency> dependencies = getPlugins(project);
        dependencies.forEach(dependency -> dependency.setVersions(searchService.getVersions(dependency)));

        List<MavenDependency> dependenciesWithNewerVersions = dependencies.stream()
                .filter(dependency -> dependency.newerVersionExist(VERSION_MATCH))
                .toList();
        for(var dependency : dependenciesWithNewerVersions){
            var newestVersion = dependency.getLatestVersion(VERSION_MATCH);
            var currentVersion = dependency.getVersion();
            var test=true;
        }

        var test =true;
        var test2=true;
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
                "org.apache.maven.plugins:maven-dependency-plugin" // Maven Dependency Plugin
        );
        List<Plugin> test = project.getBuild()
                .getPlugins()
                .stream()
                .filter(plugin -> !defaultPluginKeys.contains(plugin.getKey()))
                .toList();
        var test123 = test.get(0);
        var test2 = project.getBuildPlugins();

        PluginMapper pluginMapper = PluginMapper.INSTANCE;
        var plugins = project.getBuildPlugins()
                .stream()
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

}
