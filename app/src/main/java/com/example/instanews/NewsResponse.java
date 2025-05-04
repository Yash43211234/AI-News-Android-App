package com.example.instanews;

import java.util.List;

public class NewsResponse {

    private String status;
    private int totalResults;
    private List<NewsModel> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<NewsModel> getResults() {
        return results;
    }

    public void setResults(List<NewsModel> results) {
        this.results = results;
    }
}
