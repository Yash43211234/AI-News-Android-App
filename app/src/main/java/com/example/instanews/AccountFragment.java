package com.example.instanews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment {

    private FirebaseAuth mAuth;
    private AdView adView;

    public AccountFragment() {
        super(R.layout.fragment_account); // Uses fragment_account.xml layout
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Show email or UID
        TextView userDetailsText = view.findViewById(R.id.profile_name);
        if (currentUser != null) {
            String email = currentUser.getEmail();
            userDetailsText.setText("Logged in as: " + (email != null ? email : currentUser.getUid()));
        } else {
            userDetailsText.setText("User not logged in.");
        }

        // Navigate to Settings
        ImageView settingsIcon = view.findViewById(R.id.settings_icon);
        settingsIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        });

        // Logout
        Button logoutBtn = view.findViewById(R.id.logout_button);
        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Initialize Mobile Ads
        MobileAds.initialize(requireContext(), initializationStatus -> {});

        // Load Banner Ad
        adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                super.onAdFailedToLoad(adError);
                Log.e("AdMob", "Ad failed to load: " + adError.getMessage());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("AdMob", "Ad successfully loaded");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroyView() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroyView();
    }
}
