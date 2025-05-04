package com.example.instanews;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        ((TextView) findViewById(R.id.lang_option).findViewById(R.id.setting_title)).setText("🌐 Languages"+"    "+"upcoming");
        ((TextView) findViewById(R.id.notif_option).findViewById(R.id.setting_title)).setText("🔔 Notifications"+"    "+"upcoming");
        ((TextView) findViewById(R.id.preferences_option).findViewById(R.id.setting_title)).setText("⚙️ Your Preferences"+"    "+"upcoming");
        ((TextView) findViewById(R.id.nightmode_option).findViewById(R.id.setting_title)).setText("🌙 Night Mode"+"    "+"upcoming");
        ((TextView) findViewById(R.id.datasaver_option).findViewById(R.id.setting_title)).setText("📉 Data Saver"+"    "+"upcoming");
        ((TextView) findViewById(R.id.autoplay_option).findViewById(R.id.setting_title)).setText("▶️ Autoplay"+"    "+"upcoming");
        ((TextView) findViewById(R.id.textsize_option).findViewById(R.id.setting_title)).setText("🔠 Text Size"+"    "+"upcoming");
        ((TextView) findViewById(R.id.share_option).findViewById(R.id.setting_title)).setText("📤 Share this App"+"    "+"upcoming");
        ((TextView) findViewById(R.id.rate_option).findViewById(R.id.setting_title)).setText("⭐ Rate this App"+"    "+"upcoming");
        ((TextView) findViewById(R.id.logout_option).findViewById(R.id.setting_title)).setText("🚪 Logout"+"    "+"upcoming");
        ((TextView) findViewById(R.id.privacy_option).findViewById(R.id.setting_title)).setText("🔐 Privacy"+"    "+"upcoming");
        ((TextView) findViewById(R.id.delete_option).findViewById(R.id.setting_title)).setText("🗑️ Delete Profile"+"    "+"upcoming");
//        ((TextView) findViewById(R.id.feedback_option).findViewById(R.id.setting_title)).setText("✉️ Feedback");
    }
}