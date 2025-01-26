package org.example.integration.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.example.integration.client.model.MvnSearchResponse;

public interface MvnClient {

    @RequestLine("GET /solrsearch/select?q=g:{groupId}+AND+a:{artifactId}&core=gav&rows={rows}&wt=json")
    @Headers("Content-Type: application/json")
    MvnSearchResponse getArtifactVersions(@Param("groupId") String groupId,
                                          @Param("artifactId") String artifactId,
                                          @Param("rows") int rows);
}