package com.example.futureplan;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Saturday#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Saturday extends Fragment {
    private SimpleAdapter sa;

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

    public Saturday() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Saturday.
     */
    // TODO: Rename and change types and number of parameters
    public static Saturday newInstance(String param1, String param2) {
        Saturday fragment = new Saturday();
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
        View view = inflater.inflate(R.layout.fragment_saturday, container, false);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        ImageView next = view.findViewById(R.id.next);
        ImageView prev = view.findViewById(R.id.prev);
        FloatingActionButton edit = view.findViewById(R.id.editPlan);

        String dayS = "Saturday";

        Bundle bundle= new Bundle();
        bundle.putString("day",dayS);

        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        ArrayList<String> lessonID = new ArrayList<>();

        CollectionReference collectionReference = fStore.collection("users").document(userID).collection("timetable").document("lessons").collection(dayS);
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot snapshot : value){
                    lessonID.add(snapshot.getId());
                    HashMap<String,String> item = new HashMap<String,String>();
                    item.put( "line1", snapshot.getString("subject"));
                    item.put( "line2", snapshot.getString("time"));
                    item.put( "line3", snapshot.getString("classroom"));
                    list.add( item );
                }

                for(int i=0;i<list.size()-1;i++){
                    String[] parts = list.get(i).get("line2").split(":");
                    String[] parts2 = list.get(i+1).get("line2").split(":");
                    if(Integer.parseInt(parts[0]) > Integer.parseInt(parts2[0])){
                        Collections.swap(list, i, i+1);
                    }else if (Integer.parseInt(parts[0]) == Integer.parseInt(parts2[0])){
                        String[] parts3 = parts[1].split("-");
                        String[] parts4 = parts2[1].split("-");
                        if(Integer.parseInt(parts3[0]) > Integer.parseInt(parts4[0])){
                            Collections.swap(list, i, i+1);
                        }
                    }

                }
                sa = new SimpleAdapter(getContext(), list,
                        R.layout.list_timetable,
                        new String[] { "line1","line2","line3" },
                        new int[] {R.id.line_a, R.id.line_b,R.id.line_c});
                ((ListView)view.findViewById(R.id.listTimetable)).setAdapter(sa);
            }
        });

        ListView listTimetable = view.findViewById(R.id.listTimetable);
        listTimetable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = lessonID.get(i);
                Bundle bundle = new Bundle();
                bundle.putString("lessonID",id);
                bundle.putString("day",dayS);
                Navigation.findNavController(view).navigate(R.id.action_saturday_to_editTimetable,bundle);
            }
        });


        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_saturday_to_sunday);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_saturday_to_friday);
            }
        });

        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_saturday_to_editPlan2,bundle);
            }
        });

        return view;
    }
}