package com.fofa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryResultEntity {

    @JsonProperty("size")
    private int size;

    @JsonProperty("query")
    private String query;

    @JsonProperty("results")
    private List<List<String>> results;

    private List<ResultEntity> finalResults;
}
