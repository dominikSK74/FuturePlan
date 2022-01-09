package com.example.futureplan;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTimetableTest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTimetableTest extends Fragment {
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

    public EditTimetableTest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditTimetableTest.
     */
    // TODO: Rename and change types and number of parameters
    public static EditTimetableTest newInstance(String param1, String param2) {
        EditTimetableTest fragment = new EditTimetableTest();
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
        View view = inflater.inflate(R.layout.fragment_edit_timetable_test, container, false);

        TextInputLayout txt = view.findViewById(R.id.txtinput);
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
                PreferenceUtils.saveSubject(autoCompleteTxt.getText().toString(),getContext());
                subject = autoCompleteTxt.getText().toString();
            }
        });

        TextView dayEdTxtTests = view.findViewById(R.id.dayEdTxtTests);
        EditText titleEdTxtTests = view.findViewById(R.id.titleEdTxtTests);

        dayEdTxtTests.setOnClickListener(new View.OnClickListener() {
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
                System.out.println("onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                if(day<10){
                    dayString = "0" + day;
                }
                if(month<10){
                    monthString = "0" + month;
                }
                date = dayString + "." + monthString;

                dayEdTxtTests.setText(dayString + "." + monthString);
            }
        };

        subject = PreferenceUtils.getSubject(getContext());

        DataBaseTests dataBaseTests = new DataBaseTests(getContext());

        Button saveBtnTests = view.findViewById(R.id.saveBtnTests);
        saveBtnTests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeworkModel homeworkModel;
                homeworkModel = new HomeworkModel(-1,subject,date,titleEdTxtTests.getText().toString());
                dataBaseTests.insertData(homeworkModel);
                Navigation.findNavController(view).navigate(R.id.action_editTimetableTest_to_timetableTest);
            }
        });
        return view;
    }
}