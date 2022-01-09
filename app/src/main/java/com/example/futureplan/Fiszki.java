package com.example.futureplan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ScrollView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fiszki#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fiszki extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fiszki() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fiszki.
     */
    // TODO: Rename and change types and number of parameters
    public static Fiszki newInstance(String param1, String param2) {
        Fiszki fragment = new Fiszki();
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

        View view = inflater.inflate(R.layout.fragment_fiszki, container, false);
        RecyclerView fiszkiRecycler = view.findViewById(R.id.fiszkiRecycler);
        ExtendedFloatingActionButton efab = view.findViewById(R.id.FABfiszki);
        NestedScrollView sv = view.findViewById(R.id.scrollView2);


        fiszkiRecycler.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        // Floating Action Button Hide and Show

        sv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    efab.hide();
                }else {
                    efab.show();
                }
            }
        });



        // Loading images
        List<CardItem> cardItems = new ArrayList<>();
        cardItems.add(new CardItem("Niemiecki"));
        cardItems.add(new CardItem("Historia"));

        fiszkiRecycler.setAdapter(new CardAdapter(cardItems));


        Button fiszka = view.findViewById(R.id.buttonFiszki);



        //Extended Floating Action Button on click to add new flashcards
        efab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_menuFiszki_to_addNewFlashcards);
            }
        });



        return view;
    }
}