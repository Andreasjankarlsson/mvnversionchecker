package org.example.service;

import org.apache.maven.project.MavenProject;
import org.example.mapper.DependencyMapper;
import org.example.mapper.PluginMapper;
import org.example.model.MavenDependency;

import java.util.List;

public class DependencyService {
    private static DependencyMapper dependencyMapper = DependencyMapper.INSTANCE;

    public List<MavenDependency> getDependencies(MavenProject project){
        return  project.getDependencies()
                .stream()
                .map(dependency -> (MavenDependency) dependencyMapper.toDependencyModel(dependency))
                .toList();
    }
}
