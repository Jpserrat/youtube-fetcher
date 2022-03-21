package com.example.youtubechannelfetcher.mock;

import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;

import java.util.List;

public class SearchResultMock {

    public static SearchListResponse getSearchListResponse() {
        SearchListResponse searchListResponse = new SearchListResponse();
        searchListResponse.setItems(List.of(getSearchResult()));
        return searchListResponse;
    }

    public static SearchResult getSearchResult() {
        SearchResult searchResult = new SearchResult();
        searchResult.setId(getResourceId());
        searchResult.setSnippet(getSearchResultSnippet());
        return searchResult;
    }

    public static SearchResultSnippet getSearchResultSnippet() {
        SearchResultSnippet searchResultSnippet = new SearchResultSnippet();
        searchResultSnippet.setDescription("description");
        return searchResultSnippet;
    }

    public static ResourceId getResourceId() {
        ResourceId resourceId = new ResourceId();
        resourceId.setVideoId("videoId");
        return resourceId;
    }
}
