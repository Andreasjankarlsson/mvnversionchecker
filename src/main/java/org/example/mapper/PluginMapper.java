package org.example.mapper;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.example.model.DependencyModel;
import org.example.model.PluginModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface PluginMapper {

    // Instance of the mapper
    PluginMapper INSTANCE = Mappers.getMapper(PluginMapper.class);

    @Mapping(source = "groupId", target = "groupdId")
    @Mapping(source = "artifactId", target = "artifactId")
    @Mapping(source = "version", target = "version")
    PluginModel toPluginModel(Plugin plugin);
}
