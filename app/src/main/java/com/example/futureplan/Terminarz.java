package com.example.futureplan;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Terminarz#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Terminarz extends Fragment implements CalendarAdapter.onItemListener{
    private SimpleAdapter sa;
    private String nameOfDay;
    private String dateString;
    String day,month,year;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    private String monthYear;
    private TextView txtDate;
    private  ListView listViewTests;
    private  ListView listViewHomework;
    private  ListView listViewTimetable;
    private  ListView listViewEvents;


    View dividerCalendar;
    View dividerColor;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Terminarz() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Terminarz.
     */
    // TODO: Rename and change types and number of parameters
    public static Terminarz newInstance(String param1, String param2) {
        Terminarz fragment = new Terminarz();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terminarz, container, false);

        txtDate = view.findViewById(R.id.txtDate);
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTxt);
        selectedDate = LocalDate.now();
        setMonthView();

        listViewTests = view.findViewById(R.id.listViewTests);
        listViewHomework = view.findViewById(R.id.listViewHomework);
        listViewTimetable = view.findViewById(R.id.listViewTimetable);
        dividerCalendar = view.findViewById(R.id.dividerCalendar);
        listViewEvents = view.findViewById(R.id.listViewEvents);
        dividerColor = view.findViewById(R.id.dividerColor);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        view.findViewById(R.id.prevMonth).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusMonths(1);
                setMonthView();
            }
        });

        view.findViewById(R.id.nextMonth).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusMonths(1);
                setMonthView();

            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth,this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i=1; i< 42;i++){
            if(i<= dayOfWeek || i > dayOfWeek +daysInMonth){
                daysInMonthArray.add("");
            }else{
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM.yy");
        monthYear = date.format(formatter2);
        return  date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, String dayText, boolean addEvent) {
        if(!dayText.equals("")){
            dividerCalendar.setVisibility(View.VISIBLE);
            String date = (dayText + "." + monthYear);
            day = dayText;
            dateString = date.substring(0, (date.lastIndexOf(".")));
            month = monthYear.substring(0, monthYear.lastIndexOf("."));
            year = monthYear.substring(monthYear.lastIndexOf(".") + 1);
            if(addEvent){
                Bundle bundle = new Bundle();
                bundle.putString("date", dateString);
                Navigation.findNavController(getView()).navigate(R.id.action_menuTerminarz_to_eventEdit, bundle);
            }else {
                txtDate.setText(dateString);
                txtDate.setVisibility(View.VISIBLE);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                switch (dayOfWeek) {
                    case Calendar.SUNDAY:
                        nameOfDay= "Sunday";
                        break;
                    case Calendar.MONDAY:
                        nameOfDay= "Monday";
                        break;
                    case Calendar.TUESDAY:
                        nameOfDay= "Tuesday";
                        break;
                    case Calendar.THURSDAY:
                        nameOfDay= "Thursday";
                        break;
                    case Calendar.SATURDAY:
                        nameOfDay= "Saturday";
                        break;
                    case Calendar.WEDNESDAY:
                        nameOfDay= "Wednesday";
                        break;
                    case Calendar.FRIDAY:
                        nameOfDay= "Friday";
                        break;
                }

                ArrayList<HashMap<String,String>> testsArrayList = new ArrayList<>();

                CollectionReference tests = fStore.collection("users").document(userID).collection("tests");
                tests.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot snapshot : value){
                            if(snapshot.getString("date").equals(dateString)) {
                                HashMap<String, String> item = new HashMap<String, String>();
                                item.put("line1", snapshot.getString("subject"));
                                item.put("line2", snapshot.getString("title"));
                                testsArrayList.add(item);
                            }
                        }
                        sa = new SimpleAdapter(getContext(), testsArrayList,
                                R.layout.list_terminarz,
                                new String[] { "line1","line2" },
                                new int[] {R.id.line_b, R.id.line_a});
                        listViewTests.setAdapter(sa);
                    }
                });

                ArrayList<HashMap<String,String>> arrayListHomework = new ArrayList<>();

                CollectionReference homework = fStore.collection("users").document(userID).collection("homework");
                homework.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot snapshot : value){
                            if(snapshot.getString("date").equals(dateString)) {
                                HashMap<String, String> item = new HashMap<String, String>();
                                item.put("line1", snapshot.getString("subject"));
                                item.put("line2", snapshot.getString("title"));
                                arrayListHomework.add(item);
                            }
                        }
                        sa = new SimpleAdapter(getContext(), arrayListHomework,
                                R.layout.list_terminarz,
                                new String[] { "line1","line2" },
                                new int[] {R.id.line_b, R.id.line_a});
                        listViewHomework.setAdapter(sa);
                    }
                });

                ArrayList<HashMap<String,String>> arrayListTimetable = new ArrayList<>();

                CollectionReference collectionReference = fStore.collection("users").document(userID).collection("timetable").document("lessons").collection(nameOfDay);
                collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot snapshot : value){
                            HashMap<String,String> item = new HashMap<String,String>();
                            item.put( "line1", snapshot.getString("time"));
                            item.put( "line2", snapshot.getString("subject"));
                            arrayListTimetable.add( item );
                        }
                        sa = new SimpleAdapter(getContext(), arrayListTimetable,
                                R.layout.list_terminarz,
                                new String[] { "line1","line2" },
                                new int[] {R.id.line_a, R.id.line_b});
                        listViewTimetable.setAdapter(sa);
                    }
                });

                ArrayList<HashMap<String,String>> arrayListEvents = new ArrayList<>();

                CollectionReference events = fStore.collection("users").document(userID).collection("events");
                events.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot snapshot : value){
                            if(snapshot.get("date").equals(dateString)) {
                                HashMap<String, String> item = new HashMap<String, String>();
                                item.put("line1", snapshot.getString("timeStart") + "-" + snapshot.getString("timeEnd"));
                                item.put("line2", snapshot.getString("title"));
                                dividerColor.setBackgroundColor(Integer.parseInt(snapshot.getString("color")));
                                arrayListEvents.add(item);
                            }
                        }
                        sa = new SimpleAdapter(getContext(), arrayListEvents,
                                R.layout.list_terminarz,
                                new String[] { "line1","line2"},
                                new int[] {R.id.line_a, R.id.line_b});
                        listViewEvents.setAdapter(sa);
                    }
                });



            }
        }
    }
}