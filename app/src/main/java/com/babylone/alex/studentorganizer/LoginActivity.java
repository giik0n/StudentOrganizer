package com.babylone.alex.studentorganizer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private TextView signup, reserPass;
    private FirebaseAuth mAuth;
    private EditText userEmail, userPass;
    private Button loginButton;
    private ProgressDialog loadBar;
    private ImageButton googleButton;

    private  static final int RC_SIGN_IN = 1;
    private static final String TAG = "LoginActivity";
    private String m_Text = "";

    private GoogleApiClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.textView_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// перехід на реєстрацію при натисканні
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });
        mAuth = FirebaseAuth.getInstance();
        loadBar = new ProgressDialog(this);
        userEmail = findViewById(R.id.editTextEmailLogin);
        userPass = findViewById(R.id.editTextPasswordLogin);
        userPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean mHandled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    loginButton.performClick();
                    mHandled = true;
                }
                return mHandled;
            }
        });
        loginButton = findViewById(R.id.buttonLogin);
        googleButton = findViewById(R.id.imageButtonGoogle);
        reserPass = findViewById(R.id.textViewForgot);

        reserPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// відновлення паролю

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Password restore");
                builder.setMessage("Enter your email");

                final EditText input = new EditText(LoginActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);

                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        if (!m_Text.isEmpty()){
                            mAuth.sendPasswordResetEmail(m_Text).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {// відправляє ссилку на зміну пароля на введену пошту
                                    Toast.makeText(LoginActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });// вхід с гугл

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// вхід
                loadBar.setTitle("Loggining In");
                loadBar.setMessage("Please wait");
                loadBar.show();
                String email = userEmail.getText().toString();
                String pass = userPass.getText().toString();

                if(email.isEmpty() || pass.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Fill ale the fields", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {// якщо логін і пароль вірні переходимо на домашній єкран
                            if (task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                finish();
                            }else{
                                String message = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                            }
                            loadBar.dismiss();
                        }
                    });
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)// параметри входу гугл(стандартна штука)_
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "Connection to Google failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signIn() {// вхід через аккаунт гугл на телефоні
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {// якщо можна війти через аккаунт гугл, тоді переходимо на головну сторінку
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

                Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Cant get auth result", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {// відправка данниз гугл для входу
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }

}
