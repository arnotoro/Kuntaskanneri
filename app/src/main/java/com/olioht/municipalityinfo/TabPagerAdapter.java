package com.olioht.municipalityinfo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.olioht.municipalityinfo.fragments.BasicInfoFragment;
import com.olioht.municipalityinfo.fragments.CompareTownFragment;
import com.olioht.municipalityinfo.fragments.QuizFragment;

public class TabPagerAdapter extends FragmentStateAdapter {

    public TabPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 2:
                return new CompareTownFragment();
            case 3:
                return new QuizFragment();
            default:
                return new BasicInfoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
