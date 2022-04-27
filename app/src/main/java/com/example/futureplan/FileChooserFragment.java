package com.example.futureplan;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileChooserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileChooserFragment extends Fragment {

    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;

    private static final String LOG_TAG = "AndroidExample";

    public static String pathToFile;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FileChooserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FileChooserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FileChooserFragment newInstance(String param1, String param2) {
        FileChooserFragment fragment = new FileChooserFragment();
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
        View view = inflater.inflate(R.layout.fragment_file_chooser, container, false);

        Button btnBrowse = view.findViewById(R.id.btnBrowse);
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });



        return view;
    }

    int requestcode = 1;
    private void proImportCSV(File from) {
        System.out.println(from.getPath());
        //System.out.println(Environment.getExternalStorageDirectory() + "/Download/plan.csv");
        File file = new File(from.getPath());
        System.out.println(file.exists());
        System.out.println(file.getPath());
        if(file.exists()) {

            List<List<String>> records = new ArrayList<>();
            try {
                CSVReader csvReader = new CSVReader(new FileReader(from));
                String[] values = null;
                while ((values = csvReader.readNext()) != null) {
                    records.add(Arrays.asList(values));
                    System.out.println(Arrays.asList(values));
                }

            } catch (IOException | CsvValidationException e) {
                e.printStackTrace();
            }
            System.out.println(records);
        }

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("text/comma-separated-values");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Open CSV"),requestcode);
    }

    @Override
    public void onActivityResult(int requestcode, int resulCode, @Nullable Intent data){
        super.onActivityResult(requestcode,resulCode,data);
        if(requestcode == requestcode && data!=null && data.getData()!=null){
            proImportCSV(new File(data.getData().getPath()));
        }
    }


}