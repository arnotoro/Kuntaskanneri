package com.olioht.kuntaskanneri.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.olioht.kuntaskanneri.R;
import com.olioht.kuntaskanneri.api.DataRetriever;
import com.olioht.kuntaskanneri.api.MunicipalityData;
import com.olioht.kuntaskanneri.api.TrafficData;
import com.olioht.kuntaskanneri.api.WeatherData;
import com.olioht.kuntaskanneri.recyclerview.weathercam.WeatherCamListAdapter;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompareTownFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
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
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView pageTitle = view.findViewById(R.id.pageTitle);
        ImageView weatherIcon = view.findViewById(R.id.weatherIcon);
        TextView temperature = view.findViewById(R.id.currentTemperature);
        TextView humidity = view.findViewById(R.id.currentHumidity);
        TextView windSpeed = view.findViewById(R.id.currentWindSpeed);
        TextView pressure = view.findViewById(R.id.currentPressure);
        TextView rainAmount = view.findViewById(R.id.currentRainAmountLastHour);
        TextView weatherDescription = view.findViewById(R.id.weatherDescription);

        Bundle bundle = getArguments();
        assert bundle != null;
        String location = bundle.getString("municipalityName");


        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            DataRetriever dr = new DataRetriever();
            MunicipalityData md = dr.getMunicipalityData(getContext(), location);
            assert md != null;

            requireActivity().runOnUiThread(() -> {
                WeatherData wd = md.getWeatherData();

                if (wd == null) {
                    pageTitle.setText("Säätiedot eivät saatavilla");
                    return;
                }


                pageTitle.setText(location);
                weatherIcon.setBackgroundColor(Color.parseColor("#d1dff6"));
                temperature.setText(wd.getCurrentTemperature() + "°C");
                weatherDescription.setText(wd.getCurrentWeather());
                humidity.setText("Suht. kosteus: " + wd.getCurrentHumidity() + "%");
                windSpeed.setText("Tuuli: "+ wd.getCurrentWindSpeed() + " m/s");
                pressure.setText("Ilmanpaine: " + wd.getCurrentPressure() + "hPa");
                rainAmount.setText("Sademäärä (viim. 1h): " + wd.getCurrentRainAmountLastHour() + "mm");


                System.out.println("WeatherFragment onViewCreated: " + wd.getWeatherIconId());

                switch(wd.getWeatherIconId()) {
                    case "02d":
                        weatherIcon.setImageResource(R.drawable.weather_few_clouds);
                        break;
                    case "03d":
                        weatherIcon.setImageResource(R.drawable.weather_scattered_clouds);
                        break;
                    case "04d":
                        weatherIcon.setImageResource(R.drawable.weather_broken_clouds);
                        break;
                    case "09d":
                        weatherIcon.setImageResource(R.drawable.weather_rain);
                        break;
                    case "10d":
                        weatherIcon.setImageResource(R.drawable.weather_rain_with_sun);
                        break;
                    case "11d":
                        weatherIcon.setImageResource(R.drawable.weather_thunder);
                        break;
                    case "13d":
                        weatherIcon.setImageResource(R.drawable.weather_snow);
                        break;
                    case "50d":
                        weatherIcon.setImageResource(R.drawable.weather_fog);
                        break;
                    default:
                        weatherIcon.setImageResource(R.drawable.weather_sunny);
                        break;
                }
            });
        });

    }
}