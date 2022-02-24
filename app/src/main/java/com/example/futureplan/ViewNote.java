package com.example.futureplan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewNote#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewNote extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewNote() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewNote.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewNote newInstance(String param1, String param2) {
        ViewNote fragment = new ViewNote();
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
        View view = inflater.inflate(R.layout.fragment_view_note, container, false);

        String jsonFileString = MyJSON.getData(getContext(), "notes.json");
        Gson gson = new Gson();
        Type listNotesType = new TypeToken<List<Notes>>() { }.getType();
        List<Notes> notes = gson.fromJson(jsonFileString, listNotesType);

        int note_id = PreferenceUtils.getNoteID(getContext());

        String note = notes.get(note_id).getNote();
        String titleNote = notes.get(note_id).getTitle();

        TextInputLayout titleInputTxt = view.findViewById(R.id.title_text_input);
        EditText editTextNote = view.findViewById(R.id.editTextNote);

        titleInputTxt.getEditText().setText(titleNote);
        editTextNote.setText(note);

        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_viewNote_to_notesList);
            }
        });

        Button btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notes.remove(note_id);

                String jsonString = gson.toJson(notes,listNotesType);

                try {
                    File file = new File(getContext().getFilesDir(),"notes.json");
                    FileWriter fileWriter = null;
                    fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(jsonString);
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Navigation.findNavController(view).navigate(R.id.action_viewNote_to_notesList);
            }
        });

        Button btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleNote = titleInputTxt.getEditText().getText().toString();
                String note = editTextNote.getText().toString();

                notes.get(note_id).setNote(note);
                notes.get(note_id).setTitle(titleNote);

                String jsonString = gson.toJson(notes,listNotesType);

                try {
                    File file = new File(getContext().getFilesDir(),"notes.json");
                    FileWriter fileWriter = null;
                    fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(jsonString);
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
}