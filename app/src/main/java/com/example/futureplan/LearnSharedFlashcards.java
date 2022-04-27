package com.example.futureplan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private ImageView imageProfile;
    private StorageReference storageReference;
    private FirebaseStorage storage;

    String userFlashcardID;

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

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        TextView frontOpis = view.findViewById(R.id.frontOpis);
        TextView frontNotatka = view.findViewById(R.id.frontNotatka);
        TextView backOpis = view.findViewById(R.id.backOpis);
        TextView backNotatka = view.findViewById(R.id.backNotatka);
        ImageView nextFlash = view.findViewById(R.id.nextFlash);
        RelativeLayout deleteFlash = view.findViewById(R.id.deleteFlash);
        ImageView prevFlash = view.findViewById(R.id.prevFlash);
        TextView titleKit = view.findViewById(R.id.titleKit);
        EasyFlipView efv = view.findViewById(R.id.easyFlipView);

        imageProfile = view.findViewById(R.id.imageProfile);

        TextView txtUser = view.findViewById(R.id.textUser);

        String nazwa = getArguments().getString("nazwa");
        titleKit.setText(nazwa);

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
                if(efv.isBackSide())
                    efv.flipTheView();
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
                if(efv.isBackSide())
                    efv.flipTheView();
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
                username = value.getString("username");
                txtUser.setText(value.getString("username"));
                userFlashcardID = value.getString("userID");
                fStore.collection("users").document(userFlashcardID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String mDrawableName = value.getString("avatar");
                        if(mDrawableName == null || mDrawableName.equals("")){
                            downloadFile(userFlashcardID);
                        }else{
                            int resID = getResources().getIdentifier(mDrawableName , "drawable", getContext().getPackageName());
                            imageProfile.setImageResource(resID);
                        }

                    }
                });

                fStore.collection("users").document(userID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value.getString("nickname").equals(username)){
                            deleteFlash.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });


        return view;
    }

    private void downloadFile(String id){
        StorageReference imageRef = storageReference.child("profileImages").child(id + ".jpeg");
        long MAXBYTES = 1024*1024;
        imageRef.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //convert byte[] to bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                imageProfile.setImageBitmap(bitmap);
            }
        });

    }
}