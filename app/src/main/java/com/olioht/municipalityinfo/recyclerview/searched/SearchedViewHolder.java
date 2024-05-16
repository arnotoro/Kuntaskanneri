package com.olioht.municipalityinfo.recyclerview.searched;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.olioht.municipalityinfo.R;

public class SearchedViewHolder extends RecyclerView.ViewHolder {
    Button municipalityName;
    public SearchedViewHolder(@NonNull View itemView) {
        super(itemView);
        municipalityName = itemView.findViewById(R.id.lastSearchButton);
    }
}
