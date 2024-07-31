package com.olioht.kuntaskanneri;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.olioht.kuntaskanneri.recyclerview.searched.ListSearches;
import com.olioht.kuntaskanneri.recyclerview.searched.Search;
import com.olioht.kuntaskanneri.recyclerview.searched.SearchedListAdapter;

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
    }

    public void onSearchBtnClick(View view) {
        TextView search = findViewById(R.id.editTextSeach);

        // Check if search field is empty and show error message to user
        if (search.getText().toString().isEmpty()) {
            search.setError("Syötä kunta");
            return;
        }


        String searchQuery = search.getText().toString();

        // First letter to uppercase, rest to lowercase
        searchQuery = searchQuery.substring(0, 1).toUpperCase() + searchQuery.substring(1).toLowerCase();

        // Add search to list of previous searches
        ListSearches.getInstance().addSearch(new Search(searchQuery));

        // Start new activity with search query as extra information
        Intent intent = new Intent(this, MunicipalityPage.class);
        intent.putExtra("municipalityName", searchQuery);
        municipalityActivityResultLauncher.launch(intent);
    }

    public void handleMunicipalityActivityResult(int resultCode, Intent data) {
        // Handles the exit from the MunicipalityPage activity and updates the list of previous searches

        // Check if the result is OK
        if (resultCode == Activity.RESULT_OK) {
            String municipalityName = data.getStringExtra("municipalityName");
            Log.d("MainActivity", "Municipality name from search activity: " + municipalityName);

            // Initialize recyclerView if null
            if (recyclerView == null) {
                recyclerView = findViewById(R.id.lastSearches);
            }

            // Set adapter for recyclerView and update the list of previous searches
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(new SearchedListAdapter(getApplicationContext(), ListSearches.getInstance()));
        } else {
            Log.e("MainActivity", "Intent data is null");
        }
    }

    public final ActivityResultLauncher<Intent> municipalityActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        // Back button pressed, so we update the list of previous searches
        res -> handleMunicipalityActivityResult(res.getResultCode(), res.getData())
    );
}