package com.example.futureplan;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Homework#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Homework extends Fragment {
    private SimpleAdapter sa;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    public static String homework;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Homework() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Homework.
     */
    // TODO: Rename and change types and number of parameters
    public static Homework newInstance(String param1, String param2) {
        Homework fragment = new Homework();
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
        View view = inflater.inflate(R.layout.fragment_homework, container, false);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        FloatingActionButton editHomework = view.findViewById(R.id.editHomework);

        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        ArrayList<String> homeworkID = new ArrayList<>();

        CollectionReference collectionReference = fStore.collection("users").document(userID).collection("homework");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot snapshot : value){
                    homeworkID.add(snapshot.getId());
                    HashMap<String,String> item = new HashMap<String,String>();
                    item.put( "line1", snapshot.getString("title"));
                    item.put( "line2", snapshot.getString("subject"));
                    item.put( "line3", snapshot.getString("date"));
                    list.add( item );
                }
                sa = new SimpleAdapter(getContext(), list,
                        R.layout.list_timetable,
                        new String[] { "line1","line2","line3" },
                        new int[] {R.id.line_a, R.id.line_b,R.id.line_c});
                ((ListView)view.findViewById(R.id.listHomework)).setAdapter(sa);
            }
        });


        ListView listHomework = view.findViewById(R.id.listHomework);

        editHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homework_to_editHomework);
            }
        });

        listHomework.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("homeworkID", homeworkID.get(i));
                homework = homeworkID.get(i);
                Navigation.findNavController(view).navigate(R.id.action_homework_to_deleteHomework);
            }
        });



        return view;
    }
}