--
Run with enforce: org.example:MvnVersionChecker:1.0-SNAPSHOT:version -DenforceRule=2
List newer versions: org.example:MvnVersionChecker:1.0-SNAPSHOT:version
List newer versions with regex: org.example:MvnVersionChecker:1.0-SNAPSHOT:version -DversionRegex=^(?!.*RC).*$

List all versions: org.example:MvnVersionChecker:1.0-SNAPSHOT:allVersions