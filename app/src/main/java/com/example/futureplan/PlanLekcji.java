package com.example.futureplan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;


public class PlanLekcji extends Fragment {

    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_plan_lekcji, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        pager2 = view.findViewById(R.id.view_pager2);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
        pager2.setAdapter(adapter);

        String[] items = getResources().getStringArray(R.array.tabs);

        tabLayout.addTab(tabLayout.newTab().setText(items[0]));
        tabLayout.addTab(tabLayout.newTab().setText(items[1]));
        tabLayout.addTab(tabLayout.newTab().setText(items[2]));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        return view;
    }
}