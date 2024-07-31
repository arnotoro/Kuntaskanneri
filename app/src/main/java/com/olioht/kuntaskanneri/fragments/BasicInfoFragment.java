package com.olioht.kuntaskanneri.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;

import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.olioht.kuntaskanneri.R;
import com.olioht.kuntaskanneri.api.DataRetriever;
import com.olioht.kuntaskanneri.api.MunicipalityData;
import com.olioht.kuntaskanneri.api.PopulationData;

import java.util.ArrayList;
import java.util.List;
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
        TextView txtEmploymentSuffiencyData = view.findViewById(R.id.txtEmploymentSuffiencyData);
        TextView txtEmploymentData = view.findViewById(R.id.txtEmploymentData);
        AnyChartView populationChartView = view.findViewById(R.id.populationChangeChartView);


        // Initialize the chart for population change data
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);

        // OnClick crosshair for the chart
        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        // Chart styling and legend settings
        cartesian.title("Asukasmäärän kehitys (1990 - 2023)");
        cartesian.title().fontSize(16d);
        cartesian.title().fontColor("black");

        cartesian.background().stroke("2 black");

        cartesian.yAxis(0).title("Asukasmäärä");
        cartesian.yAxis(0).title().fontSize(15d);

        cartesian.xAxis(0).title("Vuosi");
        cartesian.xAxis(0).title().fontSize(15d);
        cartesian.xAxis(0).labels().padding(2d, 2d, 2d, 2d);


        Bundle bundle = getArguments();
        if (bundle != null) {

            // Get location from arguments
            String location = bundle.getString("municipalityName");

            // Fetch api data in separate thread
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                Context context = getContext();
                assert context != null;

                // API call to get municipality data
                DataRetriever dataRetriever = new DataRetriever();
                MunicipalityData municipality = dataRetriever.getMunicipalityData(context, location);

                // Data fetch successful
                if (municipality != null) {
                    PopulationData populationData = municipality.getPopulationData();
                    List<DataEntry> seriesData = new ArrayList<>();

                    for (Integer key : populationData.getPopulationChange().keySet()) {
                        // Add population data to the line chart
                        seriesData.add(new CustomDataEntry(key, populationData.getPopulationChange().get(key)));
                    }

                    Set set = Set.instantiate();
                    set.data(seriesData);
                    Mapping seriesMapping = set.mapAs("{ year: 'year', population: 'population' }");

                    Line series1 = cartesian.line(seriesMapping);
                    series1.name("Population");
                    series1.hovered().markers().enabled(true);
                    series1.hovered().markers()
                            .type(MarkerType.CIRCLE)
                            .size(4d);
                    series1.tooltip()
                            .position("right")
                            .anchor(Anchor.LEFT_CENTER)
                            .offsetX(5d)
                            .offsetY(5d);
                }

                // update UI after API call
                requireActivity().runOnUiThread(() -> {
                    if (municipality != null) {
                        // Last element in the population data is the current population
                        String population = String.valueOf(municipality.getPopulationData().getPopulation());
                        txtPopulationData.setText("Nykyinen asukasmäärä: " + population);

                        // Set page title to location with styling
                        pageTitle.setText(location);
                        pageTitle.setBackgroundColor(Color.GRAY);
                        pageTitle.setTextColor(Color.WHITE);
                        pageTitle.setShadowLayer(10, 2, 2, Color.BLACK);
                        pageTitle.setAlpha(0.8f);

                        // Set silhouette based on population
                        if (municipality.getPopulationData().getPopulation() < 20000) {
                            pageTitle.setBackgroundResource(R.drawable.silhouette_small);
                        } else if (municipality.getPopulationData().getPopulation() < 100000) {
                            pageTitle.setBackgroundResource(R.drawable.silhouette_medium);
                        } else {
                            pageTitle.setBackgroundResource(R.drawable.silhouette_large);
                        }

                        populationChartView.setChart(cartesian);

                        // Employment data for the municipality
                        txtEmploymentData.setText("Työllisyysaste: " + municipality.getPoliticalData().getEmploymentRate() + "%");
                        txtEmploymentSuffiencyData.setText("Työpaikkaomavaraisuusaste: " + municipality.getPoliticalData().getEmploymentSelfSuffiency() + "%");
                    } else {
                        // Create alert dialog if data is not available (e.g. municipality name wrong, api call failed)
                        AlertDialog dialog = getAlertDialog();
                        dialog.show();
                    }
                });
            });
        }

    }

    private AlertDialog getAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setNegativeButton("Takaisin", (dialog, id) -> {
            dialog.dismiss();
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        builder.setMessage("Dataa ei saatavilla. Tarkista kunnan nimi ja yritä uudelleen.");

        return builder.create();
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(Number year, Number population) {
            super(year, population);
        }
    }
}