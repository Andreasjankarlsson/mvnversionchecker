package org.example.integration.client;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import lombok.extern.slf4j.Slf4j;
import org.example.integration.client.model.MvnSearchResponse;
import org.example.model.DependencyVersion;
import org.example.model.MavenDependency;

import java.util.List;

@Slf4j
public class MvnSearchService {
    private static String BASE_URL = "https://search.maven.org";
    private static MvnClient client = Feign.builder()
            .decoder(new JacksonDecoder())
            .target(MvnClient.class, BASE_URL);

    public List<DependencyVersion> getVersions(MavenDependency dependency) {
        // Make the request to the API with the query parameters
        log.debug("Fetching newer versions for " + dependency.getGroupdId() + ":" + dependency.getArtifactId());
        MvnSearchResponse response = client.getArtifactVersions(dependency.getGroupdId(),
                dependency.getArtifactId(),
                100);

        return response.getResponse().getDocs().stream().map(
                        item -> DependencyVersion.builder()
                                .timestamp(item.getTimestamp())
                                .version(item.getV())
                                .build())
                .toList();
    }
}
