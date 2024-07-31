package com.olioht.kuntaskanneri;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.olioht.kuntaskanneri.api.MunicipalityData;
import com.olioht.kuntaskanneri.fragments.BasicInfoFragment;
import com.olioht.kuntaskanneri.fragments.WeatherFragment;
import com.olioht.kuntaskanneri.fragments.TrafficFragment;

public class TabPagerAdapter extends FragmentStateAdapter {
    private String municipality;

    public TabPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String municipality) {
        super(fragmentManager, lifecycle);
        this.municipality = municipality;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                Fragment weatherFragment = new WeatherFragment();
                Bundle args = new Bundle();
                args.putString("municipalityName", municipality);
                weatherFragment.setArguments(args);
                return weatherFragment;
            case 2:
                Fragment trafficFragment = new TrafficFragment();
                Bundle args2 = new Bundle();
                args2.putString("municipalityName", municipality);
                trafficFragment.setArguments(args2);
                return trafficFragment;
            default:
                // set the title of the info page to the searched municipality
                Fragment fragment = new BasicInfoFragment();
                Bundle argsDefault = new Bundle();
                argsDefault.putString("municipalityName", municipality);
                fragment.setArguments(argsDefault);
                return fragment;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
