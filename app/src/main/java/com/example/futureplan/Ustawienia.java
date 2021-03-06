package com.example.futureplan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import android.widget.Toast;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ustawienia#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ustawienia extends Fragment {
    Context context = getContext();
    Resources resources;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Ustawienia() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ustawienia.
     */
    // TODO: Rename and change types and number of parameters
    public static Ustawienia newInstance(String param1, String param2) {
        Ustawienia fragment = new Ustawienia();
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

    private void RestartActivity(){
        startActivity(new Intent(getContext(),BasicActivity.class));
    }

    private void localeHelp(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = getContext().getResources();
        Configuration config = new Configuration();
        config.locale = locale;
        resources.updateConfiguration(config, null);
        RestartActivity();
    }

    SharedPreferences sharedPreferences = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ustawienia, container, false);

        // DARK THEME
        Button dark = view.findViewById(R.id.dark);
        Button light = view.findViewById(R.id.light);

        sharedPreferences = getContext().getSharedPreferences("night", 0);
        Boolean booleanValue = sharedPreferences.getBoolean("night_mode", true);

        if(booleanValue){
            dark.setVisibility(View.INVISIBLE);
            light.setVisibility(View.VISIBLE);
        }else{
            dark.setVisibility(View.VISIBLE);
            light.setVisibility(View.INVISIBLE);
        }

        dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("night_mode", true);
                editor.commit();
                RestartActivity();
                Toast.makeText(getContext(), "Dark Mode is ON", Toast.LENGTH_SHORT).show();
            }
        });

        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("night_mode", false);
                editor.commit();
                RestartActivity();
                Toast.makeText(getContext(), "Dark Mode is OFF", Toast.LENGTH_SHORT).show();
            }
        });













        String[] items = getResources().getStringArray(R.array.languageArray);
        AutoCompleteTextView autoCompleteLanguage;
        ArrayAdapter<String> adapterItems;

        autoCompleteLanguage = view.findViewById(R.id.autoCompleteLanguage);
        adapterItems = new ArrayAdapter<String>(requireContext(), R.layout.subjects, items);
        autoCompleteLanguage.setAdapter(adapterItems);
        autoCompleteLanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        localeHelp("pl");
                        break;
                    case 1:
                        localeHelp("en");
                        break;
                    case 2:
                        localeHelp("de");
                        break;
                }

            }
        });

        return view;
    }
}

