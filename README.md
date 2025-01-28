Add following to run in another repo.

<plugin>
				<groupId>org.example</groupId>
				<artifactId>MvnVersionChecker</artifactId>
				<version>1.0-SNAPSHOT</version>
				<executions>
					<!-- Version goal -->
					<execution>
						<id>version-check</id>
						<goals>
							<goal>version</goal>
						</goals>
						<configuration>
							<versionRegex>^\d+\.\d+\.\d+$</versionRegex>
						</configuration>
					</execution>

					<!-- AllVersions goal -->
					<execution>
						<id>all-versions-check</id>
						<goals>
							<goal>allVersions</goal>
						</goals>
					</execution>
					<!-- version constraint -->
					<!-- AllVersions goal -->
					<execution>
						<id>versions-constraint-check</id>
						<goals>
							<goal>versionsConstraint</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-surefire-plugin</artifactId>
				<version>3.0.0-M5</version>
				<configuration>
					<forkMode>never</forkMode>
					<debugForkedProcess>true</debugForkedProcess>
					<debugPort>5005</debugPort> <!-- Default debug port -->
				</configuration>
			</plugin>
