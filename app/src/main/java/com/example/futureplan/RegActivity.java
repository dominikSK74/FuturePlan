package com.example.futureplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegActivity extends AppCompatActivity {
    private Button buttonLogowanie;
    private Button btnReg;
    private EditText editTextEmail, editTextName, editTextPassword, editTextRepassword;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        btnReg = findViewById(R.id.btnReg);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepassword = findViewById(R.id.editTextPassword2);

        fStore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(RegActivity.this);

        buttonLogowanie = findViewById(R.id.button);
        buttonLogowanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String name = editTextName.getText().toString();
                String pass = editTextPassword.getText().toString();
                String repass = editTextRepassword.getText().toString();
                if(email.equals("") || pass.equals("") || repass.equals("")){
                    Toast.makeText(RegActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }else if (pass.length() < 7) {
                    Toast.makeText(RegActivity.this, "Password to short. Password must have 8 characters length", Toast.LENGTH_SHORT).show();
                }else if (!pass.equals(repass)) {
                    Toast.makeText(RegActivity.this, "Passwords not equal", Toast.LENGTH_SHORT).show();
                }else {
                    mLoadingBar.setTitle("Registration");
                    mLoadingBar.setMessage("Please wait, while check your credentials");
                    mLoadingBar.setCanceledOnTouchOutside(false);
                    mLoadingBar.show();

                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegActivity.this,"Succesfull registration", Toast.LENGTH_SHORT);
                                mLoadingBar.dismiss();
                                userID = mAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("nickname",name);
                                user.put("email",email);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("TAG","onSuccess: user profile is created for " + userID);
                                    }
                                });

                                Intent intent = new Intent(RegActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegActivity.this,task.getException().toString(),Toast.LENGTH_SHORT);
                            }
                    }

                    });
                }

            }
        });
    }
}
