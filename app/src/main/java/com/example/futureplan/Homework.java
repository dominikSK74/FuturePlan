package com.example.futureplan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Homework#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Homework extends Fragment {
    private SimpleAdapter sa;
    public static String element;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Homework() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Homework.
     */
    // TODO: Rename and change types and number of parameters
    public static Homework newInstance(String param1, String param2) {
        Homework fragment = new Homework();
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
        View view = inflater.inflate(R.layout.fragment_homework, container, false);

        FloatingActionButton editHomework = view.findViewById(R.id.editHomework);

        DataBaseHomework dataBaseHomework = new DataBaseHomework(getContext());

        ArrayList<HashMap<String,String>> list = dataBaseHomework.getAdapterList(getContext());

        ListView listHomework = view.findViewById(R.id.listHomework);

        sa = new SimpleAdapter(getContext(), list,
                R.layout.list_timetable,
                new String[] { "line1","line2","line3" },
                new int[] {R.id.line_a, R.id.line_b,R.id.line_c});
        ((ListView)view.findViewById(R.id.listHomework)).setAdapter(sa);

        editHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homework_to_editHomework);
            }
        });

        listHomework.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                element =list.get(i).get("line1");
                Navigation.findNavController(view).navigate(R.id.action_homework_to_deleteHomework);
            }
        });



        return view;
    }
}