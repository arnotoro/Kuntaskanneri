package com.olioht.municipalityinfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.olioht.municipalityinfo.api.DataRetriever;
import com.olioht.municipalityinfo.fragments.BasicInfoFragment;

public class MunicipalityPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_municipality_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.municipalityInfoPage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Get the selected municipality name from the intent
        Intent intent = getIntent();
        String municipality = intent.getStringExtra("municipalityName");


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 fragmentArea = findViewById(R.id.viewArea);
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), getLifecycle(), municipality);
        fragmentArea.setAdapter(tabPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentArea.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        fragmentArea.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


        // handle back button press and return the municipality name to the main activity
        OnBackPressedCallback onBack = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.i("MunicipalityPage", "Back button pressed");
                Intent resultData = new Intent();
                resultData.putExtra("municipalityName", municipality);
                setResult(RESULT_OK, resultData);
                finish();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, onBack);
    }
}