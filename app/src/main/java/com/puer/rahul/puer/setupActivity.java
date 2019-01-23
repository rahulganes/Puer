package com.puer.rahul.puer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class setupActivity extends AppCompatActivity {

    private EditText userName,fullName,countryName;
    private Button saveButton;
    private CircleImageView profileImage;
    private ProgressDialog loadingBar;
    private TextView change_dp;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private StorageReference userProfileImageRef;



    private String downloadImage;

    String currentUID;
    final static int Gallery_pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUID = FirebaseAuth.getInstance().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUID);
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("profile images");
        loadingBar = new ProgressDialog(this);

        userName = (EditText)findViewById(R.id.setup_user_name);
        fullName = (EditText)findViewById(R.id.setup_full_name);
        countryName = (EditText)findViewById(R.id.setup_country_name);
        saveButton = (Button) findViewById(R.id.setup_save_btn);
        change_dp = (TextView)findViewById(R.id.change_DP_setup);

        profileImage = (CircleImageView)findViewById(R.id.setup_profile_image);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccountSetupInformation();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_pick);
            }
        });

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("profileimage")) {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.add_profile_picture).into(profileImage);
                        change_dp.setText("change profile picture");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                { Toast.makeText(setupActivity.this, "Image is being updated...", Toast.LENGTH_SHORT).show();
                    Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();
                    StorageReference storageReference = task.getResult().getMetadata().getReference();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override public void onSuccess(Uri uri)
                        { final String downloadUrl = uri.toString();

                             downloadImage = downloadUrl;
                            usersRef.child("profileimage").setValue(downloadUrl) .addOnCompleteListener(new OnCompleteListener<Void>()
                            { @Override public void onComplete(@NonNull Task<Void> task)
                            { if (task.isSuccessful())
                            {

                                //Intent selfIntent = new Intent(setupActivity.this, setupActivity.class);
                                //startActivity(selfIntent);
                                Toast.makeText(setupActivity.this, "Image updation successful...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            } else
                            { String message = task.getException().getMessage();
                                Toast.makeText(setupActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            } } });
                        } });
                } } });
            } else
            { Toast.makeText(setupActivity.this, "Error: Unable to crop Image.", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }
    private void sendUserToHomeActivity() {
        Intent homeIntent=new Intent(setupActivity.this,home_actiivty.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }


    private void saveAccountSetupInformation() {
        String username = userName.getText().toString();
        String fullname = fullName.getText().toString();
        String country  = countryName.getText().toString();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(fullname) || TextUtils.isEmpty(country))
        {
            Toast.makeText(this,"Empty field is not allowed",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Saving Information ... ");
            loadingBar.setMessage("Your Account is being updated ... ");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);

            HashMap userMap = new HashMap();
            userMap.put("username", username);
            userMap.put("fullname",fullname);
            userMap.put("country",country);
            userMap.put("status","default_status");
            userMap.put("gender","default_gender");
            userMap.put("dob","");
            if(TextUtils.isEmpty(downloadImage))
            {
                userMap.put("profileimage","default");
            }
            usersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(setupActivity.this, "Info update successful", Toast.LENGTH_LONG).show();
                        sendUserToHomeActivity();
                    }
                    else
                    {
                        if(task.getException().getMessage()!=null)
                        {
                            String message = task.getException().getMessage();
                            Toast.makeText(setupActivity.this, "Error : "+message, Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(setupActivity.this, "Unknown Error occured", Toast.LENGTH_LONG).show();
                        }
                    }
                    loadingBar.dismiss();
                }
            });
        }
    }
}
