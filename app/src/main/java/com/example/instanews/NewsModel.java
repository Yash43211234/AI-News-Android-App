package com.example.instanews;

public class NewsModel {
    String title, link, imageUrl, description;

    public NewsModel(String title, String link, String imageUrl, String description) {
        this.title = title;
        this.link = link;
        this.imageUrl = imageUrl;
        this.description = description;

    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
