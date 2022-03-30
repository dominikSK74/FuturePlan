package com.example.futureplan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Terminarz#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Terminarz extends Fragment {
    private SimpleAdapter sa, sa2, sa3;
    private String nameOfDay;
    private String dateString;
    private String dayString;
    private String monthString;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terminarz, container, false);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        CalendarView simpleCalendarView = view.findViewById(R.id.simpleCalendarView);

        TextView date = view.findViewById(R.id.date);

        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
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
                month = month + 1;

                dayString = day + "";
                monthString = month + "";
                if(day<10){
                    dayString = "0" + day;
                }
                if(month<10){
                    monthString = "0" + month;
                }

                dateString = dayString + "." + monthString;

                date.setText(dateString);
                date.setVisibility(View.VISIBLE);
                ListView listViewCalendar = view.findViewById(R.id.listViewCalendar);

                ArrayList<HashMap<String,String>> list = new ArrayList<>();

                CollectionReference collectionReference = fStore.collection("users").document(userID).collection("timetable").document("lessons").collection(nameOfDay);
                collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot snapshot : value){
                            HashMap<String,String> item = new HashMap<String,String>();
                            item.put( "line1", snapshot.getString("time"));
                            item.put( "line2", snapshot.getString("subject"));
                            list.add( item );
                        }
                        sa = new SimpleAdapter(getContext(), list,
                                R.layout.list_terminarz,
                                new String[] { "line1","line2" },
                                new int[] {R.id.line_b, R.id.line_a});
                        ((ListView)view.findViewById(R.id.listViewCalendar)).setAdapter(sa);
                    }
                });

                listViewCalendar.setVisibility(View.VISIBLE);

                //---------------------------------------\\

                ArrayList<HashMap<String,String>> list2 = new ArrayList<>();

                CollectionReference tests = fStore.collection("users").document(userID).collection("tests");
                tests.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot snapshot : value){
                            System.out.println(snapshot.getString("date"));
                            System.out.println(dateString);
                            if(snapshot.getString("date").equals(dateString)) {
                                HashMap<String, String> item = new HashMap<String, String>();
                                item.put("line1", snapshot.getString("subject"));
                                item.put("line2", snapshot.getString("title"));
                                list.add(item);
                            }
                        }
                        sa2 = new SimpleAdapter(getContext(), list2,
                                R.layout.list_terminarz,
                                new String[] { "line1","line2" },
                                new int[] {R.id.line_b, R.id.line_a});
                        ((ListView)view.findViewById(R.id.listViewTests)).setAdapter(sa2);
                    }
                });

                //---------------------------------------\\

                ArrayList<HashMap<String,String>> list3 = new ArrayList<>();

                CollectionReference homework = fStore.collection("users").document(userID).collection("homework");
                homework.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot snapshot : value){
                            if(snapshot.getString("date").equals(dateString)) {
                                HashMap<String, String> item = new HashMap<String, String>();
                                item.put("line1", snapshot.getString("subject"));
                                item.put("line2", snapshot.getString("title"));
                                list.add(item);
                            }
                        }
                        sa2 = new SimpleAdapter(getContext(), list2,
                                R.layout.list_terminarz,
                                new String[] { "line1","line2" },
                                new int[] {R.id.line_b, R.id.line_a});
                        ((ListView)view.findViewById(R.id.listViewHomework)).setAdapter(sa2);
                    }
                });
            }
        });

        return view;
    }
}