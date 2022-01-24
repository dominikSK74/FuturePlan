package com.example.futureplan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PomoceNaukoweTekst#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PomoceNaukoweTekst extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PomoceNaukoweTekst() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PomoceNaukoweTekst.
     */
    // TODO: Rename and change types and number of parameters
    public static PomoceNaukoweTekst newInstance(String param1, String param2) {
        PomoceNaukoweTekst fragment = new PomoceNaukoweTekst();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pomoce_naukowe_tekst, container, false);

        TextView txtParagraph = view.findViewById(R.id.txtParagraph);

        Button btnback = view.findViewById(R.id.btnBack);

        String jsonFileString = MyJSON.getAssetJsonData(getContext());
        Gson gson = new Gson();
        Type listSubjectsType = new TypeToken<List<Subjects>>() { }.getType();
        List<Subjects> subject = gson.fromJson(jsonFileString, listSubjectsType);

        int subject_id = PreferenceUtils.getSubjectID(getContext());
        int subject_text = PreferenceUtils.getSubjectIDtext(getContext());

        txtParagraph.setText(subject.get(subject_id).getParagraphs().get(subject_text).getText());

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_pomoceNaukoweTekst_to_pomoceNaukoweRozdzialy);

            }
        });



        return view;
    }
}