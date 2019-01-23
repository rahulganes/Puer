package com.puer.rahul.puer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import androidx.appcompat.widget.Toolbar;
import id.zelory.compressor.Compressor;

public class postActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton selectImagePost;
    private EditText postDescription;
    private Button updatePostButton;

    private static int Gallery_pick = 1;
    private Uri ImageUri;
    private String Description;
    private String saveCurrentTime,saveCurrentDate,postRandomName,currentUID,fileName;
    private  Uri uriDownload;

    private StorageReference postImageReference;
    private DatabaseReference usersRef,postsRef;
    private FirebaseAuth mAuth;

    private File imageFile;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Boolean boolKey = getIntent().getExtras().getBoolean("BoolKey");



        postImageReference = FirebaseStorage.getInstance().getReference();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();

        selectImagePost = (ImageButton)findViewById(R.id.select_post_image);
        updatePostButton = (Button)findViewById(R.id.update_post_button);
        postDescription = (EditText)findViewById(R.id.post_description);

        loadingBar = new ProgressDialog(this);

        mToolbar = (Toolbar)findViewById(R.id.update_post_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Post");

        selectImagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        updatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validPostInfo();
            }
        });

        if(boolKey)
        {
            selectImagePost.setVisibility(View.INVISIBLE);
        }
    }

    private void validPostInfo() {

        Description = postDescription.getText().toString();


        if(TextUtils.isEmpty(Description)&&(ImageUri == null))
        {
            Toast.makeText(postActivity.this, "Nothing to post", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Add New Post");
            loadingBar.setMessage("Your Post is being updated...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);
            storingImageToFirebaseStorage();
        }
    }

    private void storingImageToFirebaseStorage(){

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat currentTime2 = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordTime.getTime());
        postRandomName = saveCurrentDate+saveCurrentTime;
        saveCurrentTime = currentTime2.format(calFordTime.getTime());
        if(ImageUri!=null){
        //failed effort to compress image
        /*compressedImage = new Compressor(this)
                .setMaxWidth(640)
                .setMaxHeight(480)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .compressToFile(actualImage);*/
        fileName = ImageUri.getLastPathSegment()+postRandomName+".jpg";

        final StorageReference file = postImageReference.child("postimages").child(fileName);

        file.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();

                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                   final String uri_rg = uri.toString();
                        postsRef.child(postRandomName+currentUID).child("type").setValue("Image");
                        postsRef.child(postRandomName+currentUID).child("postimage").setValue(uri_rg);
                   usersRef.child(currentUID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {

                                    if (dataSnapshot.hasChild("profileimage")) {
                                        String image = dataSnapshot.child("profileimage").getValue().toString();
                                        postsRef.child(postRandomName+currentUID).child("profileimage").setValue(image);

                                    }
                                    else
                                    {
                                        postsRef.child(postRandomName+currentUID).child("profileimage").setValue("default");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });


                if(task.isSuccessful())
                {
                    Toast.makeText(postActivity.this, "Image Upload successful", Toast.LENGTH_SHORT).show();
                    SavingPostInformationToDatabase();

                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(postActivity.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                }

            }
        });}
        else
            {
            usersRef.child(currentUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {

                        if (dataSnapshot.hasChild("profileimage")) {
                            String image = dataSnapshot.child("profileimage").getValue().toString();
                            postsRef.child(postRandomName+currentUID).child("profileimage").setValue(image);

                        }
                        else
                        {
                            postsRef.child(postRandomName+currentUID).child("profileimage").setValue("default");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
                usersRef.child(currentUID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {

                            String userFullName = dataSnapshot.child("fullname").getValue().toString();
                            String userProfileName = dataSnapshot.child("username").getValue().toString();
                            String profileimage = dataSnapshot.child("profileimage").getValue().toString();


                            HashMap postMap = new HashMap();
                            postMap.put("uid",currentUID);
                            postMap.put("date",saveCurrentDate);
                            postMap.put("time",saveCurrentTime);
                            if(TextUtils.isEmpty(Description))
                            {
                                postMap.put("description","default");
                            }
                            else {
                                postMap.put("description", Description);
                            }
                            if(ImageUri==null)
                            {

                                postMap.put("postimage","textAlone");
                            }
                            else
                            {
                                String imageFile = ImageUri.toString();
                                postMap.put("postimage",imageFile);
                            }
                            postMap.put("status", "original");
                            postMap.put("sharedfrom","none");
                            postMap.put("profileimage",profileimage);
                            postMap.put("username",userProfileName);
                            postMap.put("fullname", userFullName);
                            postMap.put("key",currentUID+postRandomName);
                            postMap.put("status","original");

                            postsRef.child(postRandomName+currentUID).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful())
                                    {
                                        sendUserToActivity2(home_actiivty.class);
                                        Toast.makeText(postActivity.this, "updated successfully.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else
                                    {
                                        Toast.makeText(postActivity.this, "Error Occured , Post updation failed.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            postsRef.child(postRandomName+currentUID).child("postimage").setValue("textAlone");
            postsRef.child(postRandomName+currentUID).child("type").setValue("text");
        }

    }

    private void SavingPostInformationToDatabase()
    {
        usersRef.child(currentUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {

                    String userFullName = dataSnapshot.child("fullname").getValue().toString();
                    String userProfileName = dataSnapshot.child("username").getValue().toString();
                    String profileimage = dataSnapshot.child("profileimage").getValue().toString();
                    String imageFile = ImageUri.toString();

                    HashMap postMap = new HashMap();
                    postMap.put("uid",currentUID);
                    postMap.put("date",saveCurrentDate);
                    postMap.put("time",saveCurrentTime);
                    if(TextUtils.isEmpty(Description))
                    {
                        postMap.put("description","default");
                    }
                    else {
                        postMap.put("description", Description);
                    }
                    if(ImageUri==null)
                    {
                        postMap.put("postimage","textAlone");
                    }
                    else
                    {
                        postMap.put("postimage",imageFile);
                    }
                    postMap.put("profileimage",profileimage);
                    postMap.put("username",userProfileName);
                    postMap.put("fullname", userFullName);
                    postMap.put("key",currentUID+postRandomName);
                    postMap.put("status", "original");
                    postMap.put("sharedfrom","none");

                    postsRef.child(postRandomName+currentUID).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful())
                            {
                                sendUserToActivity2(home_actiivty.class);
                                Toast.makeText(postActivity.this, "updated successfully.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                Toast.makeText(postActivity.this, "Error Occured , Post updation failed.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void openGallery()
    {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_pick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    { super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_pick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            selectImagePost.setImageURI(ImageUri);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            sendUserToActivity2(home_actiivty.class);
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendUserToActivity(Class<?> context)
    {
        Intent activityintent = new Intent(this, context);
        startActivity(activityintent);
        finish();
    }

    private void sendUserToActivity2(Class<?> context)
    {
        Intent activityintent = new Intent(this, context);
        activityintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(activityintent);
        finish();
    }
}
