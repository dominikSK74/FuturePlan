package com.example.futureplan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;


import android.widget.ImageView;

import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profil#newInstance} factory method to
 * create an instance of this fragment.
 */



public class Profil extends Fragment {
    String userID;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String avatar;

    String mDrawableName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profil() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profil.
     */
    // TODO: Rename and change types and number of parameters
    public static Profil newInstance(String param1, String param2) {
        Profil fragment = new Profil();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        int images[]={R.drawable.awatar1,R.drawable.awatar2,R.drawable.awatar3,R.drawable.awatar4,R.drawable.awatar5,R.drawable.awatar6,R.drawable.awatar7, R.drawable.awatar8};

        ShapeableImageView profileImage = view.findViewById(R.id.profileImage);

        EditText PeditTextEmail = view.findViewById(R.id.PeditTextEmail);
        EditText PeditTextN = view.findViewById(R.id.PeditTextN);
        EditText PeditTextName = view.findViewById(R.id.PeditTextName);
        EditText PeditTextSName = view.findViewById(R.id.PeditTextSName);
        EditText PeditTextNumber = view.findViewById(R.id.PeditTextNumber);
        EditText PeditTextDate = view.findViewById(R.id.PeditTextDate);

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener((Activity) getContext(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                PeditTextEmail.setText(documentSnapshot.getString("email"));
                PeditTextN.setText(documentSnapshot.getString("nickname"));
                PeditTextName.setText(documentSnapshot.getString("fName"));
                PeditTextSName.setText(documentSnapshot.getString("sName"));
                PeditTextNumber.setText(documentSnapshot.getString("phone"));
                PeditTextDate.setText(documentSnapshot.getString("birthdate"));
                mDrawableName = documentSnapshot.getString("avatar");
                System.out.println(mDrawableName);
            }
        });

        System.out.println(mDrawableName);

       // Resources resources = getContext().getResources();
        //final int resourceId = resources.getIdentifier(mDrawableName, "drawable", getContext().getPackageName());
        //int resID = getResources().getIdentifier(mDrawableName , "drawable", getContext().getPackageName());
        //profileImage.setImageResource(resID);

        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                PreferenceUtils.saveEmail("", getContext());
                startActivity(new Intent(getContext(), LogActivity.class));
            }
        });

        Button btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = PeditTextName.getText().toString();
                String sName = PeditTextSName.getText().toString();
                String email = PeditTextEmail.getText().toString();
                String name = PeditTextN.getText().toString();
                String phone = PeditTextNumber.getText().toString();
                String date = PeditTextDate.getText().toString();

                DocumentReference documentReference = fStore.collection("users").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("nickname",name);
                user.put("email",email);
                user.put("fName",fName);
                user.put("sName",sName);
                user.put("phone",phone);
                user.put("birthdate",date);
                user.put("avatar",avatar);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG","onSuccess: user profile is saved " + userID);
                    }
                });
            }
        });

        FloatingActionButton btnImage = view.findViewById(R.id.btnImage);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }

            void showAlertDialog() {
                GridView gridView = new GridView(getActivity());

                gridView.setAdapter(new ImageAdapter(view.getContext()));
                gridView.setNumColumns(3);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        profileImage.setImageResource(images[position]);
                        avatar = "awatar" + (position+1);
                        //PreferenceUtils.saveAvatar("awatar" + (position+1),getContext());
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(gridView);
                builder.setTitle("Wybierz zdjecie profilowe:");
                builder.show();
            }
        });

        return view;
    }

}