package com.example.futureplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //finish();
        //startActivity(new Intent(this,MainActivity.class));

        if(PreferenceUtils.getEmail(this) == null || PreferenceUtils.getEmail(this).equals("")){
            startActivity(new Intent(BasicActivity.this, LogActivity.class));
        }
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
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);


        Cursor cursor = dataBaseHelper.fetch();

        cursor.moveToFirst();

        profileName.setText(cursor.getString(0));
        profileSurname.setText(cursor.getString(1));
        profileEmail.setText(cursor.getString(3));

        String mDrawableName = cursor.getString(6);

        int resID = getResources().getIdentifier(mDrawableName , "drawable", this.getPackageName());

        imageProfile.setImageResource(resID);



        //-----------------------------------\\
    }
}