package com.example.futureplan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;


import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.xml.transform.Result;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profil#newInstance} factory method to
 * create an instance of this fragment.
 */



public class Profil extends Fragment {
    String userID;
    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    String avatar;

    String mDrawableName;

    private RoundedImageView profileImage;

    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    AlertDialog.Builder builder;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

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
        avatar = "";
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        int images[]={R.drawable.awatar1,R.drawable.awatar2,R.drawable.awatar3,R.drawable.awatar4,R.drawable.awatar5,R.drawable.awatar6,R.drawable.awatar7, R.drawable.awatar8};

        profileImage = view.findViewById(R.id.profileImage);

        imageUri=Uri.parse("android.resource://my.package.name/"+profileImage);

        TextView PeditTextEmail = view.findViewById(R.id.PeditTextEmail);
        EditText PeditTextN = view.findViewById(R.id.PeditTextN);
        EditText PeditTextName = view.findViewById(R.id.PeditTextName);
        EditText PeditTextSName = view.findViewById(R.id.PeditTextSName);
        EditText PeditTextNumber = view.findViewById(R.id.PeditTextNumber);
        TextView PeditTextDate = view.findViewById(R.id.PeditTextDate);

        PeditTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                String date = day + "-" + month + "-" + year;
                SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
                Date newDate = null;
                try {
                    newDate = spf.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                spf= new SimpleDateFormat("dd-MM-yyyy");
                date = spf.format(newDate);
                PeditTextDate.setText(date);

            }
        };

        DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.addSnapshotListener((Activity) getContext(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(!(mAuth.getCurrentUser() == null)) {
                    PeditTextEmail.setText(documentSnapshot.getString("email"));
                    PeditTextN.setText(documentSnapshot.getString("nickname"));
                    PeditTextName.setText(documentSnapshot.getString("fName"));
                    PeditTextSName.setText(documentSnapshot.getString("sName"));
                    PeditTextNumber.setText(documentSnapshot.getString("phone"));
                    PeditTextDate.setText(documentSnapshot.getString("birthdate"));
                    mDrawableName = documentSnapshot.getString("avatar");

                    if (mDrawableName == null || mDrawableName.equals("")) {
                        downloadFile();
                    } else {
                        int resID = getResources().getIdentifier(mDrawableName, "drawable", getContext().getPackageName());
                        profileImage.setImageResource(resID);
                    }
                }

            }
        });



        RelativeLayout btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),LogActivity.class));
                FirebaseAuth.getInstance().signOut();
            }
        });

        RelativeLayout btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = PeditTextName.getText().toString();
                String sName = PeditTextSName.getText().toString();
                String email = PeditTextEmail.getText().toString();
                String name = PeditTextN.getText().toString();
                String phone = PeditTextNumber.getText().toString();
                String date = PeditTextDate.getText().toString();

                if(phone.length() > 9 ){
                    Toast.makeText(getContext(), "To long phone number ", Toast.LENGTH_SHORT).show();
                }else {
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("nickname", name);
                    user.put("email", email);
                    user.put("fName", fName);
                    user.put("sName", sName);
                    user.put("phone", phone);
                    user.put("birthdate", date);
                    user.put("avatar", avatar);

                    documentReference.set(user);
                    if(avatar.equals("")){
                        uploadPicture();
                    }

                }
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
                        if(position == 8){
                            choosePicture();
                        }else {
                            profileImage.setImageResource(images[position]);
                            avatar = "awatar" + (position + 1);
                        }
                    }
                });

                builder = new AlertDialog.Builder(getActivity());
                builder.setView(gridView);
                builder.setTitle("Wybierz zdjecie profilowe:");
                builder.show();
            }
        });
        return view;
    }
    public static final int PICK_IMAGE = 1;
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data!=null && data.getData()!=null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Uploading image...");
        pd.show();

        StorageReference ref = storageReference.child("profileImages").child(userID + ".jpeg");
        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fStore.collection("users").document(userID).update("avatar","");
                        pd.dismiss();
                        Toast.makeText(getContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Progress: " + progressPercent + "%");
                    }
                });
    }

    private void downloadFile(){
        StorageReference imageRef = storageReference.child("profileImages").child(userID + ".jpeg");
        long MAXBYTES = 1024*1024;
        imageRef.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //convert byte[] to bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                profileImage.setImageBitmap(bitmap);
            }
        });

    }


}