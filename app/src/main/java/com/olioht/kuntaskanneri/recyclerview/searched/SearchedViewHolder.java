package com.olioht.kuntaskanneri.recyclerview.searched;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.olioht.kuntaskanneri.R;

public class SearchedViewHolder extends RecyclerView.ViewHolder {
    Button municipalityName;

    public SearchedViewHolder(@NonNull View itemView) {
        super(itemView);
        municipalityName = itemView.findViewById(R.id.previousSearchedButton);
    }
}
