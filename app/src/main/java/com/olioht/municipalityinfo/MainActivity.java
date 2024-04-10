package com.olioht.municipalityinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.olioht.municipalityinfo.recyclerview.searched.ListSearches;
import com.olioht.municipalityinfo.recyclerview.searched.Search;
import com.olioht.municipalityinfo.recyclerview.searched.SearchedListAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Search> pastSearches = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.municipalityInfoPage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityResultLauncher<Intent> municipalityActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult res) {
                        if (res.getResultCode() == Activity.RESULT_OK) {
                            Intent data = res.getData();
                            if (data != null) {
                                String municipalityName = data.getStringExtra("municipalityName");
                                Log.d("MainActivity", "Municipality name from search activity: " + municipalityName);

                                // Initialize recyclerView if null
                                if (recyclerView == null) {
                                    recyclerView = findViewById(R.id.lastSearches);
                                }

                                // Initialize pastSearches if null
                                if (pastSearches == null) {
                                    pastSearches = new ArrayList<>();
                                }

                                // Add the data to pastSearches
                                Search search = new Search(municipalityName);
                                pastSearches.add(search);

                                Log.d("MainActivity", "Past searches: " + pastSearches);

                                // Set adapter for recyclerView
                                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                recyclerView.setAdapter(new SearchedListAdapter(getApplicationContext(), pastSearches));
                            } else {
                                Log.e("MainActivity", "Intent data is null");
                            }
                        }
                    }
                });



        TextView search = findViewById(R.id.editTextSeach);
        Button searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(v -> {
            if (search.getText().toString().isEmpty()) {
                search.setError("Please enter a municipality name");
                return;
            }
            String searchQuery = search.getText().toString();
            ListSearches.getInstance().addSearch(new Search(searchQuery));
            Intent intent = new Intent(this, MunicipalityPage.class);
            intent.putExtra("municipalityName", searchQuery);
            municipalityActivityResultLauncher.launch(intent);
        });

    }

}