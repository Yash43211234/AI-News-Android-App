package com.example.instanews;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private final String BASE_URL = "https://newsdata.io/api/1/latest?apikey=" + BuildConfig.NEWS_API_KEY + "&category=politics&country=";
    private RequestQueue requestQueue;

    private ViewPager2 newsViewPager;
    private List<NewsItem> newsItemList = new ArrayList<>();
    private NewsAdapter newsPagerAdapter;
    private Spinner filterSpinner;

    private int currentPage = 1;
    private boolean isLoading = false;
    private String selectedCountryCode = "in";
    private String nextPage = "";
    private boolean isLastPage = false;
    boolean[] isLogoOn = {true};
    ShimmerFrameLayout shimmerLayout;
    ProgressBar paginationLoader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        newsViewPager = view.findViewById(R.id.newsViewPager);
        filterSpinner = view.findViewById(R.id.filterSpinner);
        ImageView langIcon = view.findViewById(R.id.lang);

        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        shimmerLayout.startShimmer();
        shimmerLayout.setVisibility(View.VISIBLE);
        paginationLoader = view.findViewById(R.id.paginationLoader);

        newsPagerAdapter = new NewsAdapter(getContext(), newsItemList);
        newsViewPager.setAdapter(newsPagerAdapter);
        newsViewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        newsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (!isLoading && !isLastPage && position == newsItemList.size() - 1) {
                    paginationLoader.setVisibility(View.VISIBLE);
                    fetchNews(selectedCountryCode, nextPage);
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.country_list,
                R.layout.spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = parentView.getItemAtPosition(position).toString();
                selectedCountryCode = getCountryCode(selectedOption);
                currentPage = 1;
                newsItemList.clear();
                nextPage = "";
                isLastPage = false;
                fetchNews(selectedCountryCode, nextPage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        langIcon.setOnClickListener(v -> {
            if (isLogoOn[0]) {
                langIcon.setImageResource(R.drawable.logo);
            } else {
                langIcon.setImageResource(R.drawable.logo); // original icon
            }
            isLogoOn[0] = !isLogoOn[0]; // toggle the state
        });

        requestQueue = Volley.newRequestQueue(getContext());
        fetchNews(selectedCountryCode, nextPage);
        return view;
    }

    private void fetchNews(String countryCode, String nextPageToken) {
        isLoading = true;

        String url;
        if(isLogoOn[0]){
            url = BASE_URL + countryCode + "&language=hi";
        }
        else{
            url = BASE_URL + countryCode + "&language=en";
        }
        if (!nextPageToken.isEmpty()) {
            url += "&page=" + nextPageToken;
        }

        Log.d("API_URL", "Fetching: " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("API_RESPONSE", response.toString());
                    try {
                        if (response.has("results")) {
                            shimmerLayout.stopShimmer();
                            shimmerLayout.setVisibility(View.GONE);
                            paginationLoader.setVisibility(View.GONE);

                            JSONArray results = response.getJSONArray("results");

                            for (int i = 0; i < results.length(); i++) {
                                JSONObject article = results.getJSONObject(i);
                                String articleId = article.optString("article_id", "N/A");
                                String description = article.optString("description", "");
                                String imageUrl = sanitizeUrl(article.optString("image_url"));

                                if (description != null && !description.trim().isEmpty() &&
                                        !description.equalsIgnoreCase("null") &&
                                        imageUrl != null && !imageUrl.trim().isEmpty()) {
                                    NewsItem item = new NewsItem(
                                            articleId,
                                            article.optString("title", "No Title"),
                                            article.optString("link", ""),
                                            imageUrl,
                                            article.optString("pubDate", ""),
                                            description,
                                            article.optString("language", "en"),
                                            getFirstValue(article.optJSONArray("category")),
                                            getFirstValue(article.optJSONArray("country")),
                                            getFirstValue(article.optJSONArray("creator")),
                                            article.optString("source_name", "N/A")
                                    );
                                    newsItemList.add(item);
                                }
                            }

                            newsPagerAdapter.notifyDataSetChanged();

                            if (response.has("nextPage")) {
                                nextPage = response.getString("nextPage");
                            } else {
                                isLastPage = true;
                            }
                        } else {
                            Toast.makeText(getContext(), "No results found.", Toast.LENGTH_SHORT).show();
                            isLastPage = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing news", Toast.LENGTH_SHORT).show();
                    } finally {
                        isLoading = false;
                    }
                },
                error -> {
                    paginationLoader.setVisibility(View.GONE);
                    Log.e("API_ERROR", error.toString());
                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        Log.e("API_ERROR_CODE", "Code: " + statusCode);
                        if (statusCode == 429) {
                            Toast.makeText(getContext(), "Rate limit reached. Please wait.", Toast.LENGTH_SHORT).show();
                        } else if (statusCode == 401 || statusCode == 403) {
                            Toast.makeText(getContext(), "Invalid API key or unauthorized.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Server error: " + statusCode, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Network error. Check connection.", Toast.LENGTH_SHORT).show();
                    }
                    isLoading = false;
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }

    private String getFirstValue(JSONArray jsonArray) {
        if (jsonArray != null && jsonArray.length() > 0) {
            try {
                return jsonArray.getString(0);
            } catch (JSONException e) {
                Log.e("HomeFragment", "JSONArray error: ", e);
            }
        }
        return "N/A";
    }

    private String sanitizeUrl(String url) {
        if (url == null || url.equals("null")) return "";
        return url;
    }

    private String getCountryCode(String countryName) {
        switch (countryName.toLowerCase()) {
            case "india": return "in";
            case "united states": return "us";
            case "russia": return "ru";
            case "france": return "fr";
            case "germany": return "de";
            case "china": return "cn";
            case "united kingdom": return "gb";
            default: return "in";
        }
    }
}
