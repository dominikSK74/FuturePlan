package com.example.futureplan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PomoceNaukowe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PomoceNaukowe extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PomoceNaukowe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PomoceNaukowe.
     */
    // TODO: Rename and change types and number of parameters
    public static PomoceNaukowe newInstance(String param1, String param2) {
        PomoceNaukowe fragment = new PomoceNaukowe();
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
        View view = inflater.inflate(R.layout.fragment_pomoce_naukowe, container, false);

        GridView gridView = view.findViewById(R.id.gridView);

        String items[] = {"Matematyka", "Informatyka", "Język Polski", "Historia", "Chemia", "Biologia"};

        ArrayList<String> carL = new ArrayList<String>();
        carL.addAll( Arrays.asList(items) );

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.grid_layout, carL);

        gridView.setAdapter(adapter);



        return view;
    }
}