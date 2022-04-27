package com.example.futureplan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PomoceNaukoweRozdzialy#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PomoceNaukoweRozdzialy extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PomoceNaukoweRozdzialy() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PomoceNaukoweRozdzialy.
     */
    // TODO: Rename and change types and number of parameters
    public static PomoceNaukoweRozdzialy newInstance(String param1, String param2) {
        PomoceNaukoweRozdzialy fragment = new PomoceNaukoweRozdzialy();
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
        View view = inflater.inflate(R.layout.fragment_pomoce_naukowe_rozdzialy, container, false);

        RelativeLayout btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_pomoceNaukoweRozdzialy_to_gridviewPomoce);
            }
        });

        String jsonFileString = MyJSON.getAssetJsonData(getContext(), "pomoce_naukowe.json");
        Gson gson = new Gson();
        Type listSubjectsType = new TypeToken<List<Subjects>>() { }.getType();
        List<Subjects> subject = gson.fromJson(jsonFileString, listSubjectsType);


        ListView listView = view.findViewById(R.id.listViewParagraphs);

        ArrayList<String> items = new ArrayList<String>();

        int subject_id = PreferenceUtils.getSubjectID(getContext());
        int numberOfParagraphs = subject.get(subject_id).getParagraphs().size();

        for(int j=0; j<numberOfParagraphs;j++){
            items.add(subject.get(subject_id).getParagraphs().get(j).getNameOfParagraph());
        }

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, items);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //System.out.println(items.get(i));
                PreferenceUtils.saveSubjectIDtext(i,getContext());
                Navigation.findNavController(view).navigate(R.id.action_pomoceNaukoweRozdzialy_to_pomoceNaukoweTekst);
            }
        });





        return view;
    }
}