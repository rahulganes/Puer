package com.puer.rahul.puer;

import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.content.Intent;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.widget.Toast.LENGTH_LONG;

public class loginActivity extends AppCompatActivity {

    private EditText userEmail, userPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    private ImageView googleSignInButton;
    private static int RC_SIGN_IN = 1;
    private static final String TAG = "LoginActivity";

    private GoogleApiClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        TextView registerNewAccount = (TextView) findViewById(R.id.register_account_link);
        registerNewAccount.setPaintFlags(registerNewAccount.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        userEmail = (EditText)findViewById(R.id.login_email);
        userPassword = (EditText)findViewById(R.id.login_password);
        Button loginButton = (Button) findViewById(R.id.login_sign_up);
        loadingBar = new ProgressDialog(loginActivity.this);

        googleSignInButton = (ImageView)findViewById(R.id.google_sign_in);

        registerNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegisterActivity();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowUserToLogin();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(loginActivity.this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(loginActivity.this,"Connection failed",Toast.LENGTH_SHORT).show();
            }
        })
    .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            loadingBar.setTitle("Logging in");
            loadingBar.setMessage("Your Account is being loaded ... ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                Toast.makeText(this,"Please wait.Your Account is being loaded.",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Sorry.Your Google Account can't be Authenticated.",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            sendUserToHomeActivity();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            String message = task.getException().toString();
                            Toast.makeText(loginActivity.this,"Error :"+message,Toast.LENGTH_SHORT).show();
                            sendUserToActivity(loginActivity.class);
                        }
                        loadingBar.dismiss();
                    }
                });
    }

    private void sendUserToActivity(Class<?> context)
    {
        Intent activityintent = new Intent(this, context);
        activityintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(activityintent);
        finish();
    }

    private void allowUserToLogin() {

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        try{
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Fields cannot be left empty",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Fields cannot be left empty",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Logging in");
            loadingBar.setMessage("Your Account is being loaded ... ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {
                        Toast.makeText(loginActivity.this,"Login successful",Toast.LENGTH_SHORT).show();
                        sendUserToHomeActivity();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        if(task.getException().getMessage() != null) {
                            String message = task.getException().getMessage();
                            Toast.makeText(loginActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                        else
                        {
                            Toast.makeText(loginActivity.this, "Unknown Error Occured", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }

                    }
                }
            });
        }}
        catch (Exception o)
        {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser!=null)
            {
                sendUserToHomeActivity();
            }
        }
        catch (Exception e)
        {

        }

    }

    private void sendUserToHomeActivity() {
        Intent homeIntent=new Intent(loginActivity.this,home_actiivty.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

    private void sendUserToRegisterActivity() {

        Intent registerIntent=new Intent(loginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);
    }
}
