package com.github.informramiz.popularmovies.model;

import java.util.List;

/**
 * Created by Ramiz Raja on 11/07/2018.
 */
public class MovieApiResponse {
    private int page;
    private int totalResults;
    private int totalPages;
    private List<Movie> results;

    public MovieApiResponse() {
    }

    public MovieApiResponse(int page, int totalResults, int totalPages, List<Movie> results) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
