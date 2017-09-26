package com.example.rohithreddy.hkwikmint;

/**
 * Created by rohithreddy on 25/07/17.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohith reddy on 6/3/2017.
 */

public class SectionsPageAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentlist =new ArrayList<>();
    private final List<String>mFragmentTitlelist =new ArrayList<>();
    public void addFrament(Fragment Fragment,String title){
        mFragmentlist.add(Fragment);
        System.out.println(mFragmentlist+"[--------po=====}");
        mFragmentTitlelist.add(title);
        System.out.println(mFragmentTitlelist);
    }

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return mFragmentlist.get(position);        }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return mFragmentlist.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitlelist.get(position);
    }
}
