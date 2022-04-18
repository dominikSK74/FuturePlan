package com.example.futureplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogActivity extends AppCompatActivity {
    private Button buttonRejestracja;
    private Button btnLog;
    private EditText editTextEmail, editTextPassword;

    private FirebaseAuth mAuth;
    ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(LogActivity.this);
        buttonRejestracja = findViewById(R.id.buttonRejestracja);

        buttonRejestracja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogActivity.this, RegActivity.class));
            }
        });

        btnLog = findViewById(R.id.btnLog);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String pass = editTextPassword.getText().toString();

                if(email.isEmpty() || !email.contains("@")){
                    Toast.makeText(LogActivity.this, "Email not valid", Toast.LENGTH_SHORT).show();
                }else if(pass.isEmpty() || pass.length() < 7){
                    Toast.makeText(LogActivity.this, "Password must be 7 characters", Toast.LENGTH_SHORT).show();
                }else{
                    mLoadingBar.setTitle("Login");
                    mLoadingBar.setMessage("Wait while checking your credentials");
                    mLoadingBar.setCanceledOnTouchOutside(false);
                    mLoadingBar.show();

                    mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                mLoadingBar.dismiss();
                                Intent intent = new Intent(LogActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }else{
                                mLoadingBar.dismiss();
                                Toast.makeText(LogActivity.this,"Invalid data",Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
            }
        });
    }
}