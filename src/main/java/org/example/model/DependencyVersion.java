package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DependencyVersion {
    private long timestamp;
    private String version;

    public static Optional<DependencyVersion> getLatestVersion(List<DependencyVersion> dependencyVersions, String regex) {
        return dependencyVersions.stream()
                .filter(version -> version.getVersion().matches(regex))
                .max(Comparator.comparingLong(DependencyVersion::getTimestamp));
    }
}
