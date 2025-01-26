package org.example.integration.client.model;

import lombok.Data;

import java.util.List;

@Data
public class Response {
    private int numFound;
    private List<Doc> docs;
}
