package com.babylone.alex.studentorganizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    Button buttonSignUp;
    EditText userEmail, userPass, userConfPass;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        userEmail = findViewById(R.id.editTextEmailSignUp);
        userPass = findViewById(R.id.editTextPasswordSignUp);
        userConfPass = findViewById(R.id.editTextConfirmPasswordSignUp);

        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });
    }

    private void createNewAccount() {// перевіряє заповнені поля та записує їх в бд по нат исканню кнопки
        String email = userEmail.getText().toString();
        String password = userPass.getText().toString();
        String confPass = userConfPass.getText().toString();

        if(email.isEmpty() || password.isEmpty()|| confPass.isEmpty()){
            Toast.makeText(this, R.string.fillInAllTheFields, Toast.LENGTH_SHORT).show();
        }else if(!password.equals(confPass)){
            Toast.makeText(this, R.string.passwordsAreNotSame, Toast.LENGTH_SHORT).show();
        }else if(password.length()<8){
            Toast.makeText(this, "Password too short must be 8 at least", Toast.LENGTH_SHORT).show();
        }else if(!email.contains("@") | !email.contains(".")){
            Toast.makeText(this, "write Your Email Correctly", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("Please wait");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {// функція створення нвого користувача
                    if (task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this, "youAreAuthenticatedSuccessfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }else{
                        String message = task.getException().getMessage();
                        Toast.makeText(SignUpActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                    loadingBar.dismiss();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
