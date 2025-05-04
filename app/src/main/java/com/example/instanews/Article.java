package com.example.instanews;

import java.io.Serializable;

public class Article implements Serializable {
    private final String title;
    private final String description;
    private final String imageUrl;
    private final String publishedAt;
    private final String sourceName;
    private final String link;

    public Article(String title, String description, String imageUrl, String publishedAt, String sourceName, String link) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.publishedAt = publishedAt;
        this.sourceName = sourceName;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getLink() {
        return link;
    }
}
