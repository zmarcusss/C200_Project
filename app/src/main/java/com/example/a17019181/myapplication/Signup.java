package com.example.a17019181.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    public ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private EditText emailField;
    private EditText passwordField;
    private EditText userField;
    private EditText conditionField;
    private EditText contactField;

    private Button btn_create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextView alreadyMember = (TextView) findViewById(R.id.link_login);
        alreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this,LogInPage.class));
            }
        });

        btn_create = (Button) findViewById(R.id.btn_signup);
        emailField = (EditText) findViewById(R.id.registerEmail);
        passwordField = (EditText) findViewById(R.id.registerPassword);

        userField = (EditText) findViewById(R.id.registerUser);
        conditionField = (EditText) findViewById(R.id.registerCondition);
        contactField = (EditText) findViewById(R.id.registerContact);

        mAuth = FirebaseAuth.getInstance();

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(emailField.getText().toString(),passwordField.getText().toString());
            }
        });
    }

    private void createAccount(String email, String password) {
        Log.d("CreateAccount", "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in userField's information
                            Log.d("CreateAccount", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userInformation post = new userInformation(conditionField.getText().toString(),contactField.getText().toString(),userField.getText().toString());
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(user.getUid());
                            ref.setValue(post);
                            startActivity(new Intent(Signup.this,LogInPage.class));
                            Toast.makeText(Signup.this, "Account Created",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the userField.
                            Log.w("CreateAccount", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup.this, "Failed to create",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Creating...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        }else if(!email.matches("\\w+@\\w+\\.com")){
            emailField.setError("Enter a Valid Email.");
            valid = false;
        }

        else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        }else if(password.length()<6){
            passwordField.setError("Password must be at least 6 digit");
            valid = false;

        }
        else {
            passwordField.setError(null);
        }

        String user = userField.getText().toString();

        if(TextUtils.isEmpty(user)){
            userField.setError("Required.");
            valid = false;

        }

        String contact = contactField.getText().toString();

        if(TextUtils.isEmpty(contact)){
            contactField.setError("Required.");
            valid = false;


        }
        if(!contact.matches("^[689]\\d{7}$")){
            contactField.setError("Invalid Phone Number.");
            valid = false;

        }

        String condition = conditionField.getText().toString();

        if(TextUtils.isEmpty(condition)){
            conditionField.setError("Required.");
            valid = false;

        }


        return valid;
    }
}
