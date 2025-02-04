package org.example.comparator;

import org.example.model.DependencyVersion;

import java.util.Comparator;

public class VersionComparator implements Comparator<DependencyVersion> {
    @Override
    public int compare(DependencyVersion ver1, DependencyVersion ver2) {
        if (isNullOrEmpty(ver1)) return isNullOrEmpty(ver2) ? 0 : -1;
        if (isNullOrEmpty(ver2)) return 1;

        String[] parts1 = ver1.getVersion().split("\\.");
        String[] parts2 = ver2.getVersion().split("\\.");

        int maxLength = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < maxLength; i++) {
            String part1 = (i < parts1.length) ? parts1[i] : "0";
            String part2 = (i < parts2.length) ? parts2[i] : "0";

            int result = compareVersionParts(part1, part2);
            if (result != 0) return result;
        }
        return 0;
    }

    private boolean isNullOrEmpty(DependencyVersion version) {
        return version == null || version.getVersion() == null;
    }

    private int compareVersionParts(String part1, String part2) {
        int num1 = extractLeadingNumber(part1);
        int num2 = extractLeadingNumber(part2);

        if (num1 != num2) return Integer.compare(num1, num2);
        return part1.compareTo(part2);
    }

    private int extractLeadingNumber(String part) {
        String num = part.replaceAll("\\D.*", "");
        return num.isEmpty() ? 0 : Integer.parseInt(num);
    }
}
