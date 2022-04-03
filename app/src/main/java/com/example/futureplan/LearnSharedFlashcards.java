package com.example.futureplan;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LearnSharedFlashcards#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LearnSharedFlashcards extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    private int cardID;
    private int count;
    private String username;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LearnSharedFlashcards() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LearnSharedFlashcards.
     */
    // TODO: Rename and change types and number of parameters
    public static LearnSharedFlashcards newInstance(String param1, String param2) {
        LearnSharedFlashcards fragment = new LearnSharedFlashcards();
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
        View view = inflater.inflate(R.layout.fragment_learn_shared_flashcards, container, false);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        TextView frontOpis = view.findViewById(R.id.frontOpis);
        TextView frontNotatka = view.findViewById(R.id.frontNotatka);
        TextView backOpis = view.findViewById(R.id.backOpis);
        TextView backNotatka = view.findViewById(R.id.backNotatka);
        Button nextFlash = view.findViewById(R.id.nextFlash);
        Button deleteFlash = view.findViewById(R.id.deleteFlash);
        Button prevFlash = view.findViewById(R.id.prevFlash);

        TextView txtUser = view.findViewById(R.id.textUser);

        String nazwa = getArguments().getString("nazwa");

        count =0;
        cardID = 0;

        CollectionReference collectionReference = fStore.collection("sharedFlashcards").document(nazwa).collection("cards");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot snapshot : value){
                    count++;
                }
            }
        });

        DocumentReference documentReference = fStore.collection("sharedFlashcards").document(nazwa).collection("cards").document("" + cardID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                frontOpis.setText(value.getString("des1"));
                frontNotatka.setText(value.getString("n1"));
                backOpis.setText(value.getString("des2"));
                backNotatka.setText(value.getString("n2"));
            }
        });

        nextFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cardID < (count -1) ){
                    cardID++;
                }
                DocumentReference documentReference = fStore.collection("sharedFlashcards").document(nazwa).collection("cards").document("" + cardID);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        frontOpis.setText(value.getString("des1"));
                        frontNotatka.setText(value.getString("n1"));
                        backOpis.setText(value.getString("des2"));
                        backNotatka.setText(value.getString("n2"));
                    }
                });
            }
        });

        prevFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(cardID == 0)){
                    cardID--;
                }
                DocumentReference documentReference = fStore.collection("sharedFlashcards").document(nazwa).collection("cards").document("" + cardID);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        frontOpis.setText(value.getString("des1"));
                        frontNotatka.setText(value.getString("n1"));
                        backOpis.setText(value.getString("des2"));
                        backNotatka.setText(value.getString("n2"));
                    }
                });
            }
        });

        deleteFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fStore.collection("sharedFlashcards").document(nazwa).delete();
                Navigation.findNavController(view).navigate(R.id.action_learnFlashcards_to_menuFiszki);
            }
        });

        DocumentReference userName = fStore.collection("sharedFlashcards").document(nazwa);
        userName.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                txtUser.setText(value.getString("username"));
            }
        });


        return view;
    }
}