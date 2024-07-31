package com.olioht.kuntaskanneri.recyclerview.searched;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.olioht.kuntaskanneri.MunicipalityPage;
import com.olioht.kuntaskanneri.R;

public class SearchedListAdapter extends RecyclerView.Adapter<SearchedViewHolder> {
    private ListSearches searches;
    private Context context;

    public SearchedListAdapter(Context context, ListSearches searches) {
        this.context = context;
        this.searches = searches;
    }

    @NonNull
    @Override
    public SearchedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchedViewHolder(LayoutInflater.from(context).inflate(R.layout.previous_searches, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull SearchedViewHolder holder, int position) {
        Log.d("SearchedListAdapter", "onBindViewHolder: " + position);
        Log.d("SearchedListAdapter", "onBindViewHolder searches: " + searches.getSearch(position).getMunicipalityName());
        holder.municipalityName.setText(searches.getSearch(position).getMunicipalityName());

        holder.municipalityName.setOnClickListener(v -> {
            Log.d("SearchedListAdapter", "Button clicked: " + searches.getSearch(position).getMunicipalityName());

            Intent intent = new Intent(context, MunicipalityPage.class);
            intent.putExtra("municipalityName", searches.getSearch(position).getMunicipalityName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return searches.getSize();
    }

}
