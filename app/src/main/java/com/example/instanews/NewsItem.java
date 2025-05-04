package com.example.instanews;

import android.util.Log;

public class NewsItem {
    public String articleId, title, link, imageUrl, pubDate, description, language, category, country, creator, source;

    public NewsItem(String articleId, String title, String link, String imageUrl, String pubDate, String description,
                    String language, String category, String country, String creator, String source) {
        this.articleId = articleId;
        this.title = title;
        this.link = link;
        this.imageUrl = imageUrl;
        Log.d("%%%%%%%%%", imageUrl);
        this.pubDate = pubDate;
        this.description = description;

        this.language = language;
        this.category = category;
        this.country = country;
        this.creator = creator;
        this.source = source;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


