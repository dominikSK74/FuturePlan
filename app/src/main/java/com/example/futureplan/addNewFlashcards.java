package com.example.futureplan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addNewFlashcards#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addNewFlashcards extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;
    private int flashID;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addNewFlashcards() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addNewFlashcards.
     */
    // TODO: Rename and change types and number of parameters
    public static addNewFlashcards newInstance(String param1, String param2) {
        addNewFlashcards fragment = new addNewFlashcards();
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
        View view = inflater.inflate(R.layout.fragment_add_new_flashcards, container, false);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        FloatingActionButton addNewFlash = view.findViewById(R.id.addNewFlashcardsButton);
        RelativeLayout saveFlashcard = view.findViewById(R.id.saveFlashcards);
        EditText flashcardsName = view.findViewById(R.id.nazwaZestawu);
        EditText opis1 = view.findViewById(R.id.opis);
        EditText notatka1 = view.findViewById(R.id.notatka);
        EditText opis2 = view.findViewById(R.id.opis2);
        EditText notatka2 = view.findViewById(R.id.notatka2);

        flashID = 0;

        addNewFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View  view) {
                String nazwa = flashcardsName.getText().toString();
                String op1 = opis1.getText().toString();
                String n1 = notatka1.getText().toString();
                String op2 = opis2.getText().toString();
                String n2 = notatka2.getText().toString();

                DocumentReference documentReference2 = fStore.collection("users").document(userID).collection("flashcards").document(nazwa);
                Map<String,Object> name = new HashMap<>();
                name.put("name",nazwa);
                documentReference2.set(name);

                DocumentReference documentReference = fStore.collection("users").document(userID).collection("flashcards").document(nazwa).collection("cards").document("" + flashID);
                Map<String,Object> flashcard = new HashMap<>();
                flashcard.put("des1",op1);
                flashcard.put("n1",n1);
                flashcard.put("des2",op2);
                flashcard.put("n2",n2);
                documentReference.set(flashcard);
                flashID++;

                opis1.setText("");
                notatka1.setText("");
                opis2.setText("");
                notatka2.setText("");
            }
        });

        saveFlashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.menuFiszki);
            }
        });

        return view;
    }
}