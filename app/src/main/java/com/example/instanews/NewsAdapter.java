package com.example.instanews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final List<NewsItem> newsList;
    private final Context context;
    private InterstitialAd mInterstitialAd;
    private int scrollCount = 0;
    private final String adUnitId = "ca-app-pub-8008647138936037/5225133834";

    public NewsAdapter(Context context, List<NewsItem> newsList) {
        this.context = context;
        this.newsList = newsList;
        MobileAds.initialize(context, initializationStatus -> {});
        loadInterstitialAd();
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, adUnitId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                mInterstitialAd = null;
            }
        });
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news_fullscreen, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem item = newsList.get(position);

        holder.title.setText(item.title);
        holder.description.setText(item.description);
        holder.date.setText("Published: " + item.pubDate);

        Glide.with(context)
                .load(item.imageUrl)
                .placeholder(R.drawable.newsheadlines)
                .into(holder.image);

        AdRequest adRequest = new AdRequest.Builder().build();
        holder.adView.loadAd(adRequest);

        // Show interstitial ad every 5th scroll
        scrollCount++;
        if (scrollCount % 5 == 0 && mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    loadInterstitialAd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                    loadInterstitialAd();
                }
            });

            mInterstitialAd.show((MainActivity) context);
            mInterstitialAd = null;
        }

        // Click-based ad before opening article
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            NewsItem clickedItem = newsList.get(pos);

            if (mInterstitialAd != null) {
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        loadInterstitialAd();
                        openArticle(clickedItem.link);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                        loadInterstitialAd();
                        openArticle(clickedItem.link);
                    }
                });

                mInterstitialAd.show((MainActivity) context);
                mInterstitialAd = null;
            } else {
                openArticle(clickedItem.link);
            }
        });
    }

    private void openArticle(String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, date;
        ImageView image;
        AdView adView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.newsTitle);
            description = itemView.findViewById(R.id.newsDescription);
            date = itemView.findViewById(R.id.newsDate);
            image = itemView.findViewById(R.id.newsImage);
            adView = itemView.findViewById(R.id.adView);
        }
    }
}
