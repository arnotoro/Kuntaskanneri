package com.olioht.municipalityinfo.recyclerview.searched;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.olioht.municipalityinfo.R;

import java.util.ArrayList;

public class SearchedListAdapter extends RecyclerView.Adapter<SearchedViewHolder> {
    private ArrayList<Search> searches;
    private Context context;

    public SearchedListAdapter(Context context, ArrayList<Search> searches) {
        this.context = context;
        this.searches = searches;
    }

    @NonNull
    @Override
    public SearchedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchedViewHolder(LayoutInflater.from(context).inflate(R.layout.list_search, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull SearchedViewHolder holder, int position) {
        Log.d("SearchedListAdapter", "onBindViewHolder: " + position);
        Log.d("SearchedListAdapter", "onBindViewHolder searches: " + searches.get(position).getMunicipalityName());
        holder.municipalityName.setText(searches.get(position).getMunicipalityName());
    }

    @Override
    public int getItemCount() {
        return searches.size();
    }

}
