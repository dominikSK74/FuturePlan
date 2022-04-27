package com.example.futureplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class BasicActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private ImageView imageProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        //------------------------------------\\

        View header = navigationView.getHeaderView(0);
        TextView profileName = header.findViewById(R.id.profileName);
        TextView profileSurname = header.findViewById(R.id.profileSurname);
        TextView profileEmail = header.findViewById(R.id.profileEmail);
        imageProfile = header.findViewById(R.id.imageProfile);

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                profileEmail.setText(documentSnapshot.getString("email"));
                profileName.setText(documentSnapshot.getString("fName"));
                profileSurname.setText(documentSnapshot.getString("sName"));
                String mDrawableName = documentSnapshot.getString("avatar");
                if(mDrawableName == null || mDrawableName.equals("")){
                    downloadFile();
                }else{
                    int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
                    imageProfile.setImageResource(resID);
                }
            }
        });



        //-----------------------------------\\


    }

    private void downloadFile(){
        StorageReference imageRef = storageReference.child("profileImages").child(userID + ".jpeg");
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