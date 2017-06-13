package com.kehinde.bakingapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class TabAdapter extends FragmentPagerAdapter {

    private List<Fragment> tabFragmentsList;
    private List<String> tabTitleList;

    public TabAdapter(FragmentManager fm, List<Fragment> tabFragmentsList, List<String> tabTitleList) {
        super(fm);
        this.tabFragmentsList=tabFragmentsList;
        this.tabTitleList=tabTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        return tabFragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return tabFragmentsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitleList.get(position);
    }


}
