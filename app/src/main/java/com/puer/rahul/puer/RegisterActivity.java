package com.puer.rahul.puer;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText userEmail,userPassword,userConfirmPassword;
    private Button createAccountButton;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        userEmail = (EditText)findViewById(R.id.register_email);
        userPassword = (EditText)findViewById(R.id.register_password);
        userConfirmPassword = (EditText)findViewById(R.id.register_confirm_password);
        createAccountButton = (Button)findViewById(R.id.register_sign_up);
        loadingBar = new ProgressDialog(this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            sendUserToHomeActivity();
        }
    }

    private void sendUserToHomeActivity() {
        Intent homeIntent=new Intent(RegisterActivity.this,home_actiivty.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

    private void createNewAccount()
    {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String confirmpassword  = userConfirmPassword.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Empty field is not allowed",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Empty field is not allowed",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmpassword))
        {
            Toast.makeText(this,"Empty field is not allowed",Toast.LENGTH_SHORT).show();
        }
        else if(!(password.equals(confirmpassword)))
        {
            Toast.makeText(this,"Passwords donot match",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Creating New Account ... ");
            loadingBar.setMessage("Your Account is being created ... ");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        sendUserToSetupActivity();
                        Toast.makeText(RegisterActivity.this,"Authentication Successful",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        if(task.getException().getMessage() != null) {
                            String message = task.getException().getMessage();
                            Toast.makeText(RegisterActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "Unknown Error Occured", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                }
            });
        }
    }

    private void sendUserToSetupActivity() {

        Intent setup_activity_intent = new Intent(this,setupActivity.class);
        setup_activity_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setup_activity_intent);
        finish();
    }
}
