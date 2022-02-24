package com.example.futureplan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotesList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotesList.
     */
    // TODO: Rename and change types and number of parameters
    public static NotesList newInstance(String param1, String param2) {
        NotesList fragment = new NotesList();
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
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);

        Button btnNewNote = view.findViewById(R.id.btnNewNote);
        btnNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_notesList_to_addNewNote);
            }
        });

        if(MyJSON.getData(getContext(), "notes.json") == null){
            try {
                File file = new File(getContext().getFilesDir(), "notes.json");
                FileWriter fileWriter = null;
                fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("[]");
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String jsonFileString = MyJSON.getData(getContext(), "notes.json");
        Gson gson = new Gson();
        Type listNotesType = new TypeToken<List<Notes>>() { }.getType();
        List<Notes> note = gson.fromJson(jsonFileString, listNotesType);

        GridView gridView = view.findViewById(R.id.gridViewNotes);
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        int numberOfNotes = note.size();

        HashMap<String,String> item;
        for(int i=0;i<numberOfNotes;i++){
            item = new HashMap<String,String>();
            item.put( "line1", note.get(i).getTitle());
            item.put( "line2", note.get(i).getNote());
            list.add( item );
        }

        SimpleAdapter sa = new SimpleAdapter(getContext(), list,
                R.layout.grid_view_note,
                new String[] { "line1","line2"},
                new int[] {R.id.txtTitle, R.id.txtNote});
        ((GridView)view.findViewById(R.id.gridViewNotes)).setAdapter(sa);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Navigation.findNavController(view).navigate(R.id.action_notesList_to_viewNote);
                PreferenceUtils.saveNoteID(i,getContext());
            }
        });


        return view;
    }
}