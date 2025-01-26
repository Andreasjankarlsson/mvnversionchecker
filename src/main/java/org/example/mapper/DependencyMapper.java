package org.example.mapper;

import org.apache.maven.model.Dependency;
import org.example.model.DependencyModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface DependencyMapper {

    // Instance of the mapper
    DependencyMapper INSTANCE = Mappers.getMapper(DependencyMapper.class);

    @Mapping(source = "groupId", target = "groupdId")
    @Mapping(source = "artifactId", target = "artifactId")
    @Mapping(source = "version", target = "version")
    DependencyModel toDependencyModel(Dependency dependency);
}
