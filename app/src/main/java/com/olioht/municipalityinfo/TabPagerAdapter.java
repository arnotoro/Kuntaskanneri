package com.olioht.municipalityinfo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.olioht.municipalityinfo.fragments.BasicInfoFragment;
import com.olioht.municipalityinfo.fragments.CompareTownFragment;
import com.olioht.municipalityinfo.fragments.QuizFragment;

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
                return new CompareTownFragment();
            case 2:
                return new QuizFragment();
            default:
                // set the title of the info page to the searched municipality
                Fragment fragment = new BasicInfoFragment();
                Bundle args = new Bundle();
                args.putString("municipalityName", municipality);
                fragment.setArguments(args);
                return fragment;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
