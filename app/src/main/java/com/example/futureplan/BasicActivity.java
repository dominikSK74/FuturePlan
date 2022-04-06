package com.example.futureplan;

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
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class BasicActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;

    //String mDrawableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        //finish();
        //startActivity(new Intent(this,LogActivity.class));

        //if(PreferenceUtils.getEmail(this) == null || PreferenceUtils.getEmail(this).equals("")){
        //    startActivity(new Intent(BasicActivity.this, LogActivity.class));
        //}
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
        ImageView imageProfile = header.findViewById(R.id.imageProfile);

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                profileEmail.setText(documentSnapshot.getString("email"));
                profileName.setText(documentSnapshot.getString("fName"));
                profileSurname.setText(documentSnapshot.getString("sName"));
                String mDrawableName = documentSnapshot.getString("avatar");
                int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
                imageProfile.setImageResource(resID);
            }
        });



        //-----------------------------------\\
    }
}