package com.puer.rahul.puer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProgressDialog loadingBar;

    private static int Gallery_pick=1;

    private Boolean statusEditable=true;

    private EditText userStatus, userGender, userCountry, userDOB;
    private Button updateProfileButton;
    private EditText username,userProfName;
    private CircleImageView userProfImage;

    private FirebaseAuth mAuth;
    private DatabaseReference SettingsUserRef,usersRef,starReference;
    private StorageReference userProfileImageRef;

    private String currentUID,downloadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        SettingsUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUID);
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("profile images");
        starReference = FirebaseDatabase.getInstance().getReference().child("Stars");

        loadingBar = new ProgressDialog(this);
        mToolbar = (Toolbar)findViewById(R.id.settings_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = (EditText) findViewById(R.id.settings_username);
        userProfName = (EditText) findViewById(R.id.settings_fullname);
        userStatus = (EditText)findViewById(R.id.settings_status);
        userCountry = (EditText)findViewById(R.id.settings_country);
        userDOB = (EditText)findViewById(R.id.settings_dob);
        userGender = (EditText)findViewById(R.id.settings_gender);
        userProfImage = (CircleImageView)findViewById(R.id.settings_profileimage);
        updateProfileButton = (Button)findViewById(R.id.settings_update_profile_button);


        SettingsUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {

                    String myProfileImage = dataSnapshot.child("profileimage").getValue().toString();
                    String username_s = dataSnapshot.child("username").getValue().toString();
                    final String status_s = dataSnapshot.child("status").getValue().toString();
                    String profilename_s = dataSnapshot.child("fullname").getValue().toString();
                    String country_s = dataSnapshot.child("country").getValue().toString();
                    String gender_s = dataSnapshot.child("gender").getValue().toString();
                    String dob_s = dataSnapshot.child("dob").getValue().toString();

                    if(!myProfileImage.equals("default"))
                    {
                        Picasso.get().load(myProfileImage).placeholder(R.drawable.add_profile_picture).into(userProfImage);
                    }

                    username.setText(username_s);
                    userProfName.setText(profilename_s);
                    starReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(currentUID))
                            {
                                userStatus.setText("All-Star Contributor");
                                userStatus.setEnabled(false);
                            }
                            else
                            {
                                userStatus.setText(status_s);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    userCountry.setText(country_s);
                    userGender.setText(gender_s);
                    userDOB.setText(dob_s);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAccountInfo();
                sendUserToActivity(home_actiivty.class);
            }
        });

        userProfImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    { super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Gallery_pick && resultCode == RESULT_OK && data != null)
        { Uri imageUri = data.getData();
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON) .setAspectRatio(1,1) .start(this);
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        { CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {   loadingBar.setTitle("Image Update");
                loadingBar.setMessage("Image is being updated...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);
                Uri resultUri = result.getUri();
                StorageReference filePath = userProfileImageRef.child(currentUID + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                { @Override public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                { if(task.isSuccessful())
                {
                    toastMessage("image is being updated.");
                    Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();


                    result.addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override public void onSuccess(Uri uri)
                        {
                            Uri downloadUri = uri;
                            downloadImage = downloadUri.toString();
                            SettingsUserRef.child("profileimage").setValue(downloadImage) .addOnCompleteListener(new OnCompleteListener<Void>()
                            { @Override public void onComplete(@NonNull Task<Void> task)
                            { if (task.isSuccessful())
                            {

                                //Intent selfIntent = new Intent(setupActivity.this, setupActivity.class);
                                //startActivity(selfIntent);
                                Picasso.get().load(downloadImage).placeholder(R.drawable.add_profile_picture).into(userProfImage);
                                toastMessage("Image updation successful");
                                loadingBar.dismiss();
                            } else
                            { String message = task.getException().getMessage();
                                toastMessage("Error : "+message);
                                loadingBar.dismiss();
                            } } });
                        } });
                } } });
            } else
            { toastMessage("Unable to crop image");
                loadingBar.dismiss();
            }
        }
    }

    private void updateProfileImage() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_pick);

    }

    private void validateAccountInfo() {

        String username_s = username.getText().toString();
        String userfullname_s=userProfName.getText().toString();
        String userstatus_s=userStatus.getText().toString();
        String country_s=userCountry.getText().toString();
        String gender_s=userGender.getText().toString();
        String dob_s = userDOB.getText().toString();

        if(checkEmpty(username_s))
        {
            toastMessage("Username can't be left empty.");
        }
        else if(checkEmpty(userfullname_s))
        {
            toastMessage("full name can't be empty");
        }
        else if(checkEmpty(userstatus_s))
        {
            toastMessage("OneWordDescription can't be empty");
        }
        else if(checkEmpty(country_s))
        {
            toastMessage("country can't be empty");
        }
        else if(checkEmpty(gender_s))
        {
            toastMessage("gender can't be empty");
        }
        else if(checkEmpty(dob_s))
        {
            toastMessage("DOB can't be empty");
        }
        else
        {
            UpdatePostInfo(username_s,userfullname_s,userstatus_s,country_s,gender_s,dob_s);
        }
        
    }

    private void UpdatePostInfo(String username_s, String userfullname_s, String userstatus_s, String country_s, String gender_s, String dob_s) {

        loadingBar.setTitle("Saving Information ... ");
        loadingBar.setMessage("Your Account is being updated ... ");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);

        HashMap userMap = new HashMap();
        userMap.put("username",username_s);
        userMap.put("fullname",userfullname_s);
        userMap.put("status",userstatus_s);
        userMap.put("country",country_s);
        userMap.put("gender",gender_s);
        userMap.put("dob",dob_s);

        SettingsUserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful())
                {
                toastMessage("Profile Update Success");
                }

            }
        });
        loadingBar.dismiss();

    }

    private void sendUserToActivity2(Class<?> context)
    {
        Intent setupintent = new Intent(this,context);
        setupintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupintent);
        finish();
    }

    private void sendUserToActivity(Class<?> context)
    {
        Intent activityintent = new Intent(this, context);
        startActivity(activityintent);
    }

    private void toastMessage(String s)
    {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    private boolean checkEmpty(String s)
    {
        if(TextUtils.isEmpty(s))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
