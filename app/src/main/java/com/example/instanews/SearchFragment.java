package com.example.instanews;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView articlesRecyclerView;
    private ArticleAdapter articleAdapter;
    private List<Article> articleList = new ArrayList<>();
    private TextView placeholderText;
    private ProgressBar progressBar;


    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placeholderText = view.findViewById(R.id.placeholderText);
        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        articlesRecyclerView = view.findViewById(R.id.articlesRecyclerView);
        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        articleAdapter = new ArticleAdapter(getContext(), articleList);
        articlesRecyclerView.setAdapter(articleAdapter);
        progressBar = view.findViewById(R.id.progressBar);


        // Show trending topics initially
        placeholderText.setVisibility(View.VISIBLE);

        // Setup the search button click listener
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();

            if (!query.isEmpty()) {
                // Hide trending topics and fetch the data
                placeholderText.setVisibility(View.GONE);
                searchNews(query); // This function will fetch the data and update RecyclerView
            } else {
                // Show the placeholder text when no query
                placeholderText.setVisibility(View.VISIBLE);
                articlesRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void searchNews(String query) {
        progressBar.setVisibility(View.VISIBLE);
        String apiKey = BuildConfig.NEWS_API_KEY; // API key from local.properties
        String url = "https://newsdata.io/api/1/news?" +
                "apikey=" + apiKey +
                "&q=" + query +
                "&country=in" +
                "&language=en";


        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONArray results = response.getJSONArray("results");

                        if (results.length() > 0) {
                            articleList.clear();

                            for (int i = 0; i < results.length(); i++) {
                                JSONObject articleObject = results.getJSONObject(i);

                                String title = articleObject.optString("title", "No Title");
                                String description = articleObject.optString("description", "No Description");
                                String imageUrl = articleObject.optString("image_url", "");
                                String publishedAt = articleObject.optString("pubDate", "");
                                String sourceName = articleObject.optString("source_id", "Unknown");
                                String link = articleObject.optString("link", "");

                                Article article = new Article(title, description, imageUrl, publishedAt, sourceName, link);
                                articleList.add(article);
                            }

                            articleAdapter.notifyDataSetChanged();
                            articlesRecyclerView.setVisibility(View.VISIBLE);

                        } else {
                            Log.d(TAG, "No articles found for query: " + query);
                            articleList.clear();
                            articleAdapter.notifyDataSetChanged();
                        }

                    } catch (Exception e) {
                        Log.e("SearchNews", "Error parsing response", e);
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Log.e("SearchNews", "API Error: ", error);
                }
        );

        queue.add(jsonObjectRequest);
    }
}


