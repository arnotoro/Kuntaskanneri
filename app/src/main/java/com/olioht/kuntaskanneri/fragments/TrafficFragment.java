package com.olioht.kuntaskanneri.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olioht.kuntaskanneri.R;
import com.olioht.kuntaskanneri.api.DataRetriever;
import com.olioht.kuntaskanneri.api.MunicipalityData;
import com.olioht.kuntaskanneri.api.TrafficData;
import com.olioht.kuntaskanneri.recyclerview.weathercam.WeatherCamListAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrafficFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrafficFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;

    public TrafficFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuizFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrafficFragment newInstance(String param1, String param2) {
        TrafficFragment fragment = new TrafficFragment();
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
        return inflater.inflate(R.layout.fragment_traffic, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("TrafficFramgnet", "TrafficFragment onViewCreated");

        TextView pageTitle = view.findViewById(R.id.pageTitle);
        TextView roadName = view.findViewById(R.id.roadName);

        Bundle bundle = getArguments();
        assert bundle != null;

        // Get the location name from the bundle
        String location = bundle.getString("municipalityName");
        Log.d("TrafficFragment", "TrafficFragment municipalityName: " + location);


        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            DataRetriever dr = new DataRetriever();
            MunicipalityData md = dr.getMunicipalityData(getContext(), location);
            assert md != null;

            requireActivity().runOnUiThread(() -> {
                TrafficData tfd = md.getTrafficData();

                // If there is no traffic data available
                if (tfd == null) {
                    pageTitle.setText("Tieliikennekameroita ei saatavilla");
                    return;
                }


                pageTitle.setText(md.getTrafficData().getCamLocationName());

                // Images are loaded into the recyclerview
                recyclerView = view.findViewById(R.id.weathercamImages);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(new WeatherCamListAdapter(getContext(), tfd));

            });
        });

    }
}