package com.example.futureplan;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTimetable#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTimetable extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;


    int hourStart,minuteStart,hourEnd,minuteEnd, eventColor;
    private String timeStartS, timeEndS;
    private String subject, classroom;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditTimetable() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeleteTimetable.
     */
    // TODO: Rename and change types and number of parameters
    public static EditTimetable newInstance(String param1, String param2) {
        EditTimetable fragment = new EditTimetable();
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
        View view = inflater.inflate(R.layout.fragment_edit_timetable, container, false);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        TextInputLayout txt = view.findViewById(R.id.txtinputEditHomework);
        txt.setStartIconTintList(null);
        String[] items = getResources().getStringArray(R.array.subjectsarray);

        ArrayAdapter<String> adapterItems;
        AutoCompleteTextView autoCompleteTxt = view.findViewById(R.id.autoCompleteTextView);
        adapterItems = new ArrayAdapter<String>(requireContext(), R.layout.subjects, items);
        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                subject = autoCompleteTxt.getText().toString();
            }
        });

        String dayS = getArguments().getString("day");
        String lessonID = getArguments().getString("lessonID");

        TextInputEditText class_edit_text = view.findViewById(R.id.class_edit_text);

        TextView timeStart = view.findViewById(R.id.timeStart);
        TextView timeEnd = view.findViewById(R.id.timeEnd);

        DocumentReference documentReference = fStore.collection("users").document(userID).collection("timetable").document("lessons").collection(dayS).document(lessonID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                autoCompleteTxt.setText(value.getString("subject"),false);
                subject = value.getString("subject");
                class_edit_text.setText(value.getString("classroom"));
                classroom = value.getString("classroom");
                String time = value.getString("time");
                timeStart.setText(time.split("-")[0]);
                timeStartS = time.split("-")[0];
                timeEnd.setText(time.split("-")[1]);
                timeEndS = time.split("-")[1];

                hourStart = Integer.parseInt(timeStartS.split(":")[0]);
                minuteStart = Integer.parseInt(timeStartS.split(":")[1]);

                hourEnd = Integer.parseInt(timeEndS.split(":")[0]);
                minuteEnd = Integer.parseInt(timeEndS.split(":")[1]);
            }
        });



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


        RelativeLayout btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_editTimetable_to_monday);
            }
        });
        RelativeLayout btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentReference.delete();
                Navigation.findNavController(view).navigate(R.id.action_editTimetable_to_monday);
            }
        });

        RelativeLayout btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String day = getArguments().getString("day");
                String lessonID = getArguments().getString("lessonID");
                subject = autoCompleteTxt.getText().toString();
                classroom = class_edit_text.getText().toString();
                DocumentReference documentReference = fStore.collection("users").document(userID).collection("timetable").document("lessons").collection(day).document(lessonID);
                Map<String,String> lesson = new HashMap<>();
                lesson.put("subject",subject);
                lesson.put("time",timeStartS + "-" + timeEndS);
                lesson.put("classroom",classroom);
                documentReference.set(lesson).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Snackbar snackbar = Snackbar
                                .make(view, "Zapisano dane", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });

            }
        });

        return view;
    }
}