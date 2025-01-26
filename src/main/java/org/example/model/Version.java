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
public class Version {
    private long timestamp;
    private String version;

    public static Optional<Version> getLatestVersion(List<Version> versions, String regex) {
        return versions.stream()
                .filter(version -> version.getVersion().matches(regex))
                .max(Comparator.comparingLong(Version::getTimestamp));
    }
}
