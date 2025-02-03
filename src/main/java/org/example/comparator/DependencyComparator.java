package org.example.comparator;

import org.example.model.MavenDependency;

import java.util.Comparator;

public class DependencyComparator implements Comparator<MavenDependency> {
    @Override
    public int compare(MavenDependency dep1, MavenDependency dep2) {
        //Compare by type
        int typeComparison = dep1.getDependencyType().name().compareTo(dep2.getDependencyType().name());
        if (typeComparison != 0) {
            return typeComparison;
        }

        // Compare by groupId
        int groupIdComparison = dep1.getGroupdId().compareTo(dep2.getGroupdId());
        if (groupIdComparison != 0) {
            return groupIdComparison; // If groupId is different, return the comparison result
        }

        // If groupId is the same, compare by artifactId
        int artifactIdComparison = dep1.getArtifactId().compareTo(dep2.getArtifactId());
        if (artifactIdComparison != 0) {
            return artifactIdComparison; // If artifactId is different, return the comparison result
        }

        // If groupId and artifactId are the same, compare by version
        return dep1.getVersion().compareTo(dep2.getVersion());
    }
}
