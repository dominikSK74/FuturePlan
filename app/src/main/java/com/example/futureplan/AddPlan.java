package com.example.futureplan;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPlan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPlan extends Fragment {
    int hourStart,minuteStart,hourEnd,minuteEnd, eventColor;
    private String timeStartS, timeEndS;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String subject;

    public AddPlan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditPlan.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPlan newInstance(String param1, String param2) {
        AddPlan fragment = new AddPlan();
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

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        View view = inflater.inflate(R.layout.fragment_add_plan, container, false);

        TextView timeStart = view.findViewById(R.id.timeStart);
        TextView timeEnd = view.findViewById(R.id.timeEnd);

        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinutes) {
                        hourStart = selectedHour;
                        minuteStart = selectedMinutes;
                        timeStart.setText(String.format(Locale.getDefault(),"%02d:%02d",hourStart,minuteStart));
                        timeStartS = String.format(Locale.getDefault(),"%02d:%02d",hourStart,minuteStart);
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),onTimeSetListener,hourStart,minuteStart,true);
                timePickerDialog.setTitle("Select time");
                timePickerDialog.show();
            }
        });

        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListenerEnd = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinutes) {
                        hourEnd = selectedHour;
                        minuteEnd = selectedMinutes;
                        timeEnd.setText(String.format(Locale.getDefault(),"%02d:%02d",hourEnd,minuteEnd));
                        timeEndS = String.format(Locale.getDefault(),"%02d:%02d",hourEnd,minuteEnd);
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),onTimeSetListenerEnd,hourEnd,minuteEnd,true);
                timePickerDialog.setTitle("Select time");
                timePickerDialog.show();
            }
        });

        //Ustawienie koloru ikonek
        TextInputLayout txt = view.findViewById(R.id.txtinputAddTimetable);
        txt.setStartIconTintList(null);

        //Pole wybierz przedmiot
        String[] items = getResources().getStringArray(R.array.subjectsarray);
        AutoCompleteTextView autoCompleteTxt;
        ArrayAdapter<String> adapterItems;

        autoCompleteTxt = view.findViewById(R.id.autoCompleteTextView);
        adapterItems = new ArrayAdapter<String>(requireContext(), R.layout.subjects, items);
        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                subject = autoCompleteTxt.getText().toString();
            }
        });


        String day = getArguments().getString("day");

        RelativeLayout saveButton = view.findViewById(R.id.saveButton);
        TextInputLayout classInputTxt = view.findViewById(R.id.class_text_input);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReference = fStore.collection("users").document(userID).collection("timetable").document("lessons").collection(day).document();
                Map<String,String> lesson = new HashMap<>();
                lesson.put("subject",subject);
                lesson.put("time",timeStartS + "-" + timeEndS);
                lesson.put("classroom",classInputTxt.getEditText().getText().toString());
                documentReference.set(lesson);

                Navigation.findNavController(view).navigate(R.id.action_editPlan2_to_monday);
            }
        });



        return view;
    }
}