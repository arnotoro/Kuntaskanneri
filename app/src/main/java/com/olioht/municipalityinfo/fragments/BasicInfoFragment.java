package com.olioht.municipalityinfo.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olioht.municipalityinfo.R;
import com.olioht.municipalityinfo.api.DataRetriever;
import com.olioht.municipalityinfo.api.MunicipalityData;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BasicInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BasicInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasicInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BasicInfoFragment newInstance(String param1, String param2) {
        BasicInfoFragment fragment = new BasicInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basic_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView pageTitle = view.findViewById(R.id.pageTitle);
        TextView txtPopulationData = view.findViewById(R.id.txtPopulation);

        Bundle bundle = getArguments();
        if (bundle != null) {

            // get location from bundle arguments
            String location = bundle.getString("municipalityName");

            // set page title
            pageTitle.setText(location);

            // fetch api data
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                Context context = getContext();
                DataRetriever dataRetriever = new DataRetriever();
                ArrayList<MunicipalityData> populationData = dataRetriever.getPopulationData(getContext(), location);

                // update UI
                requireActivity().runOnUiThread(() -> {
                    if (populationData != null) {
                        // get the last element of the list, which is the most recent data
                        String population = String.valueOf(populationData.get(populationData.size() - 1).getPopulation());
                        txtPopulationData.setText("Asukasmäärä: " + population);
                    } else {
                        txtPopulationData.setText("Data fetch failed.");
                    }
                });
            });
        }
    }
}