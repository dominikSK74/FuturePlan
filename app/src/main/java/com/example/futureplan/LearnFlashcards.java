package com.example.futureplan;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LearnFlashcards#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LearnFlashcards extends Fragment {
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

    public LearnFlashcards() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LearnFlashcards.
     */
    // TODO: Rename and change types and number of parameters
    public static LearnFlashcards newInstance(String param1, String param2) {
        LearnFlashcards fragment = new LearnFlashcards();
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
        View view = inflater.inflate(R.layout.fragment_learn_flashcards, container, false);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        TextView frontOpis = view.findViewById(R.id.frontOpis);
        TextView frontNotatka = view.findViewById(R.id.frontNotatka);
        TextView backOpis = view.findViewById(R.id.backOpis);
        TextView backNotatka = view.findViewById(R.id.backNotatka);
        ImageView nextFlash = view.findViewById(R.id.nextFlash);
        RelativeLayout deleteFlash = view.findViewById(R.id.deleteFlash);
        ImageView prevFlash = view.findViewById(R.id.prevFlash);
        RelativeLayout btnShare = view.findViewById(R.id.btnShare);
        TextView titleKit = view.findViewById(R.id.titleKit);
        EasyFlipView efv = view.findViewById(R.id.easyFlipView);

        String nazwa = getArguments().getString("nazwa");
        titleKit.setText(nazwa);

        count =0;
        cardID = 0;

        CollectionReference collectionReference =fStore.collection("users").document(userID).collection("flashcards").document(nazwa).collection("cards");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot snapshot : value){
                    count++;
                }
            }
        });

        DocumentReference documentReference = fStore.collection("users").document(userID).collection("flashcards").document(nazwa).collection("cards").document("" + cardID);
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
                if(efv.isBackSide())
                    efv.flipTheView();
                DocumentReference documentReference = fStore.collection("users").document(userID).collection("flashcards").document(nazwa).collection("cards").document("" + cardID);
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
                if(efv.isBackSide())
                    efv.flipTheView();

                DocumentReference documentReference = fStore.collection("users").document(userID).collection("flashcards").document(nazwa).collection("cards").document("" + cardID);
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

        // DELETE BUTTON
        deleteFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fStore.collection("users").document(userID).collection("flashcards").document(nazwa).delete();
                Navigation.findNavController(view).navigate(R.id.action_learnFlashcards_to_menuFiszki);
            }
        });


        // NEW SHARE BUTTON
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReference = fStore.collection("sharedFlashcards").document(nazwa);
                Map<String,String> flashcard = new HashMap<>();


                DocumentReference user = fStore.collection("users").document(userID);
                user.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        username = value.getString("nickname");
                        PreferenceUtils.saveName(username,getContext());
                    }
                });

                username = PreferenceUtils.getName(getContext());

                flashcard.put("username", username);
                documentReference.set(flashcard);

                CollectionReference collectionReference =fStore.collection("users").document(userID).collection("flashcards").document(nazwa).collection("cards");
                collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        int i=0;
                        for (DocumentSnapshot snapshot : value){
                            DocumentReference flashcard = fStore.collection("sharedFlashcards").document(nazwa).collection("cards").document(""+i);
                            Map<String,String> card = new HashMap<>();
                            card.put("des1",snapshot.getString("des1"));
                            card.put("des2",snapshot.getString("des2"));
                            card.put("n1",snapshot.getString("n1"));
                            card.put("n2",snapshot.getString("n2"));
                            flashcard.set(card);
                            i++;
                        }
                    }
                });
                Toast.makeText(getContext(), "Udostępniono zestaw", Toast.LENGTH_SHORT).show();
            }
        });




        return view;
    }
}