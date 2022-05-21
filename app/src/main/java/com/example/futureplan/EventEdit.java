package com.example.futureplan;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import petrov.kristiyan.colorpicker.ColorPicker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventEdit extends Fragment {
    int hourStart,minuteStart,hourEnd,minuteEnd, eventColor;
    private String timeStartS, timeEndS;
    MaterialButton changeColorBtn;

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

    public EventEdit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventEdit.
     */
    // TODO: Rename and change types and number of parameters
    public static EventEdit newInstance(String param1, String param2) {
        EventEdit fragment = new EventEdit();
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
        View view = inflater.inflate(R.layout.fragment_event_edit, container, false);

        TextView timeStart = view.findViewById(R.id.timeStart);
        TextView timeEnd = view.findViewById(R.id.timeEnd);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();
        EditText titleEdTxtEvent = view.findViewById(R.id.titleEdTxtEvent);

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

        changeColorBtn = view.findViewById(R.id.changeColorBtn);
        changeColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPicker colorPicker = new ColorPicker(getActivity());
                colorPicker.show();
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        changeColorBtn.setBackgroundColor(color);
                        eventColor = color;
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

        view.findViewById(R.id.btnBackEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_eventEdit_to_menuTerminarz);
            }
        });

        view.findViewById(R.id.btnSaveEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEdTxtEvent.getText().toString();
                String date = getArguments().getString("date");

                DocumentReference documentReference = fStore.collection("users").document(userID).collection("events").document();
                Map<String,String> calendar = new HashMap<>();
                calendar.put("title",title);
                calendar.put("timeStart",timeStartS);
                calendar.put("timeEnd",timeEndS);
                calendar.put("color",String.valueOf(eventColor));
                calendar.put("date",date);
                documentReference.set(calendar);

                Navigation.findNavController(view).navigate(R.id.action_eventEdit_to_menuTerminarz);
            }
        });

        return view;
    }
}