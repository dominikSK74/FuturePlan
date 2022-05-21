package com.example.futureplan;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTimetableTests#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTimetableTests extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    private String title;
    private String subject;
    private  String dayString;
    private  String date;
    private  String monthString;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditTimetableTests() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeleteTimetableTests.
     */
    // TODO: Rename and change types and number of parameters
    public static EditTimetableTests newInstance(String param1, String param2) {
        EditTimetableTests fragment = new EditTimetableTests();
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
        View view = inflater.inflate(R.layout.fragment_edit_timetable_tests, container, false);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        TextInputLayout title_text_input = view.findViewById(R.id.title_text_input);
        TextView dayEdTxt = view.findViewById(R.id.dayEdTxt);

        TextInputLayout txt = view.findViewById(R.id.txtinputEditHomework);
        txt.setStartIconTintList(null);
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

        dayEdTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                dayString = day + "";
                monthString = month + "";
                if(day<10){
                    dayString = "0" + day;
                }
                if(month<10){
                    monthString = "0" + month;
                }
                date = dayString + "." + monthString;

                dayEdTxt.setText(date);
            }
        };

        DocumentReference documentReference = fStore.collection("users").document(userID).collection("tests").document(TimetableTest.element);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                title_text_input.getEditText().setText(value.getString("title"));
                title = value.getString("title");
                dayEdTxt.setText(value.getString("date"));
                date = value.getString("date");
                autoCompleteTxt.setText(value.getString("subject"),false);
                subject = value.getString("subject");
            }
        });

        RelativeLayout btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentReference.delete();
                Navigation.findNavController(view).navigate(R.id.action_deleteTimetableTests_to_timetableTest);
            }
        });
        RelativeLayout btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_deleteTimetableTests_to_timetableTest);
            }
        });
        RelativeLayout btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = title_text_input.getEditText().getText().toString();
                Map<String,String> test = new HashMap<>();
                test.put("date",date);
                test.put("title",title);
                test.put("subject",subject);
                documentReference.set(test);
                Navigation.findNavController(view).navigate(R.id.action_deleteTimetableTests_to_timetableTest);
            }
        });
        return view;
    }
}