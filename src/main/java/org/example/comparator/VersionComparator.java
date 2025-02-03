package org.example.comparator;

import org.example.model.DependencyVersion;

import java.util.Comparator;

public class VersionComparator implements Comparator<DependencyVersion> {
    public int compare(DependencyVersion ver1, DependencyVersion ver2) {
        // Handle null versions
        if (ver1 == null || ver1.getVersion() == null) {
            return (ver2 == null || ver2.getVersion() == null) ? 0 : -1; // If ver1 is null or version is null, it's smaller
        }
        if (ver2 == null || ver2.getVersion() == null) {
            return 1; // If ver2 is null or version is null, ver1 is greater
        }

        // Both versions are non-null, proceed with string comparison
        String version1 = ver1.getVersion();
        String version2 = ver2.getVersion();

        // Split the version strings into parts based on dot ('.')
        String[] versionParts1 = version1.split("\\.");
        String[] versionParts2 = version2.split("\\.");

        // Compare the version parts one by one
        int length = Math.max(versionParts1.length, versionParts2.length);
        for (int i = 0; i < length; i++) {
            // Get the parts for comparison, treat missing parts as '0' (e.g., '1.0' vs '1.0.0')
            String part1 = (i < versionParts1.length) ? versionParts1[i] : "0";
            String part2 = (i < versionParts2.length) ? versionParts2[i] : "0";

            // Compare the parts as strings directly (lexicographical comparison)
            int comparisonResult = part1.compareTo(part2);
            if (comparisonResult != 0) {
                return comparisonResult; // Return if the parts are not equal
            }
        }

        // If all parts are equal, the versions are the same
        return 0;
    }
}
