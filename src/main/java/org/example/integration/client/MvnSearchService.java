package org.example.integration.client;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.example.integration.client.model.MvnSearchResponse;
import org.example.model.MavenDependency;
import org.example.model.DependencyVersion;

import java.util.List;

public class MvnSearchService {
    private static String BASE_URL = "https://search.maven.org";
    private static MvnClient client = Feign.builder()
            .decoder(new JacksonDecoder())
            .target(MvnClient.class, BASE_URL);

    public List<DependencyVersion> getVersions(MavenDependency dependency){
        // Make the request to the API with the query parameters
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
