package com.example.futureplan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Wednesday#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Wednesday extends Fragment {

    private SimpleAdapter sa;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Wednesday() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Wednesday.
     */
    // TODO: Rename and change types and number of parameters
    public static Wednesday newInstance(String param1, String param2) {
        Wednesday fragment = new Wednesday();
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
        View view = inflater.inflate(R.layout.fragment_wednesday, container, false);

        ImageView next = view.findViewById(R.id.next);
        ImageView prev = view.findViewById(R.id.prev);
        FloatingActionButton edit = view.findViewById(R.id.editPlan);

        TextView day = view.findViewById(R.id.dzien);
        String dayS = "Środa";

        PreferenceUtils.saveDay(dayS, getContext());

        DataBaseTimetable dataBaseTimetable = new DataBaseTimetable(getContext());

        ArrayList<HashMap<String,String>> list = dataBaseTimetable.getAdapterList(getContext(),dayS);
        sa = new SimpleAdapter(getContext(), list,
                R.layout.list_timetable,
                new String[] { "line1","line2","line3" },
                new int[] {R.id.line_a, R.id.line_b,R.id.line_c});
        ((ListView)view.findViewById(R.id.listTimetable)).setAdapter(sa);

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_wednesday_to_thursday);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_wednesday_to_tuesday);
            }
        });

        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_wednesday_to_editPlan2);
            }
        });

        return view;
    }
}