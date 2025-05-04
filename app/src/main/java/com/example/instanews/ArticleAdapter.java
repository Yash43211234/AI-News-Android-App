package com.example.instanews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private final List<Article> articleList;
    private Context context;

    public ArticleAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);

        holder.titleTextView.setText(article.getTitle());
        holder.sourceTextView.setText(article.getSourceName() + " • " + article.getPublishedAt());

        Glide.with(holder.itemView.getContext())
                .load(article.getImageUrl())
                .placeholder(R.drawable.newsheadlines)
                .error(R.drawable.newsheadlines)
                .into(holder.articleImageView);

        // Home Page: Open article in WebViewActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WebViewActivity.class);
            Log.d("LINK", article.getLink());
            intent.putExtra("url", article.getLink());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return articleList.size();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
//        TextView descriptionTextView;
        TextView sourceTextView;
        ImageView articleImageView;


        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.articleTitleTextView);
//            descriptionTextView = itemView.findViewById(R.id.articleDescriptionTextView);
            sourceTextView = itemView.findViewById(R.id.articleSourceTextView);
            articleImageView = itemView.findViewById(R.id.articleImageView);

        }

    }
}
