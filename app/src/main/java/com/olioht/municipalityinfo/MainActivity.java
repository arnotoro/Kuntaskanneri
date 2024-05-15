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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
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

                // Back button pressed, so we update the list of previous searches
                res -> {
                    if (res.getResultCode() == Activity.RESULT_OK) {
                        Intent data = res.getData();
                        if (data != null) {
                            String municipalityName = data.getStringExtra("municipalityName");
                            Log.d("MainActivity", "Municipality name from search activity: " + municipalityName);

                            // Initialize recyclerView if null
                            if (recyclerView == null) {
                                recyclerView = findViewById(R.id.lastSearches);
                            }


                            // Set adapter for recyclerView and update the list of previous searches
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            recyclerView.setAdapter(new SearchedListAdapter(getApplicationContext(), ListSearches.getInstance().getSearches()));
                        } else {
                            Log.e("MainActivity", "Intent data is null");
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