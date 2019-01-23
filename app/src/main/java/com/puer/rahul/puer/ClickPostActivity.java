package com.puer.rahul.puer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ClickPostActivity extends AppCompatActivity {

    private ImageView editPostImage;
    private Button makeChangeButton,deletePostButton;
    private TextView editPostDescription;
    private DatabaseReference clickPostRef;

    private String saveCurrentTime,saveCurrentDate,postRandomName,saveCurrentTime2,fullname,username,profileimage,fileName,current_UID,Description,uri_rg;
    private boolean editable = true;

    private String image,description,postsUID;
    private Uri ImageUri;

    private StorageReference postImageReference;
    private DatabaseReference usersRef,postsRef;

    private String postKey,currentUID;
    private Posts posts;

    private ProgressDialog loadingBar;
    private Toolbar mToolbar;

    private static int Gallery_pick=1;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);

        postKey = getIntent().getStringExtra("Post_Key");
        clickPostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey);
        mAuth = FirebaseAuth.getInstance();
        postImageReference = FirebaseStorage.getInstance().getReference();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        loadingBar = new ProgressDialog(this);

        mToolbar = (Toolbar)findViewById(R.id.click_post_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Post");


        editPostImage = findViewById(R.id.editPostImage);
        editPostDescription = findViewById(R.id.edit_post_description);
        makeChangeButton = findViewById(R.id.edit_post_button);
        deletePostButton = findViewById(R.id.delete_post_button);

        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        currentUID = mAuth.getCurrentUser().getUid();

        clickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                description = dataSnapshot.child("description").getValue().toString();
                image = dataSnapshot.child("postimage").getValue().toString();
                fullname = dataSnapshot.child("fullname").getValue().toString();
                username = dataSnapshot.child("username").getValue().toString();
                postsUID = dataSnapshot.child("uid").getValue().toString();
                uri_rg = image;


                if(!mAuth.getCurrentUser().getUid().equals(postsUID))
                {
                    makeChangeButton.setText("Share Button");
                    deletePostButton.setText("Save Post");
                    editPostDescription.setFocusable(false);
                    editPostImage.setEnabled(false);
                    editable = false;
                }

                if(!(description.equals("default")))
                {editPostDescription.setText(description);}
                if(!image.equals("textAlone")) {
                    Picasso.get().load(image).into(editPostImage);
                }
                else
                {
                    editPostImage.setVisibility(View.INVISIBLE);
                }

                deletePostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editable)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivity.this);
                            builder.setTitle("Delete Post");
                            builder.setMessage("Do you want to make this Post?");
                            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deletePost();
                                }
                            });
                            Dialog dialog = builder.create();
                            dialog.show();
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);

                        }
                        else
                        {
                            Toast.makeText(ClickPostActivity.this,"save",Toast.LENGTH_LONG).show();
                        }
                    }
                });}
                makeChangeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editable)
                        {
                         editCurrentPost(description);
                        }
                        else
                        {
                            sharePost(postKey);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*if(editable)
        {
            editPostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //openGallery();
                }
            });
            makeChangeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //validPostInfo();
                }
            });
        }

        if(!editable)
        {
            makeChangeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }*/
    }

    private void editCurrentPost(String Description)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivity.this);
        builder.setTitle("Edit Post");
        builder.setMessage("Do you want to make these changes?");

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!(TextUtils.isEmpty(editPostDescription.getText().toString()))) {
                    clickPostRef.child("description").setValue(editPostDescription.getText().toString());
                }
                else
                {
                    clickPostRef.child("description").setValue("default");
                }
                sendUserToActivity2(home_actiivty.class);
                toastMessage("Post Updated Successfully");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
    }

    private void deletePost()
    {
        clickPostRef.removeValue();
        sendUserToActivity2(home_actiivty.class);
        toastMessage("Post deleted");

    }

    private void toastMessage(String s)
    {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    private void sendUserToActivity2(Class<?> context)
    {
        Intent activityintent = new Intent(this, context);
        activityintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(activityintent);
        finish();
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

    private void sharePost(String key)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts").child(key);
        DatabaseReference currentUserDetails = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());


        currentUserDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullname = dataSnapshot.child("fullname").getValue().toString();
                username = dataSnapshot.child("username").getValue().toString();
                profileimage = dataSnapshot.child("profileimage").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat currentTime2 = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calFordTime.getTime());
        saveCurrentTime2 = currentTime2.format(calFordTime.getTime());
        postRandomName = saveCurrentDate+saveCurrentTime2;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String sharedfrom = dataSnapshot.child("uid").getValue().toString();
                String postimage = dataSnapshot.child("postimage").getValue().toString();
                String description = dataSnapshot.child("description").getValue().toString();

                HashMap postMap = new HashMap();
                postMap.put("uid",current_UID);
                postMap.put("postimage",postimage);
                postMap.put("description", description);
                postMap.put("date",saveCurrentDate);
                postMap.put("time",saveCurrentTime);
                postMap.put("key",current_UID+postRandomName);
                postMap.put("type",dataSnapshot.child("type").getValue().toString());
                postMap.put("status","share");
                postMap.put("username",username);
                postMap.put("fullname",fullname);
                postMap.put("profileimage",profileimage);
                postMap.put("source",sharedfrom);

                postsRef.child(postRandomName+current_UID).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        toastMessage("shared successfully");
                    }
                });
                sendUserToActivity2(home_actiivty.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    /*private void openGallery()
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
            editPostImage.setImageURI(ImageUri);

        }
    }

    private void validPostInfo() {

        Description = editPostDescription.getText().toString();

        Toast.makeText(ClickPostActivity.this, "valid post", Toast.LENGTH_SHORT).show();


        if(TextUtils.isEmpty(Description)&&(ImageUri == null))
        {
            Toast.makeText(ClickPostActivity.this, "Nothing to post", Toast.LENGTH_SHORT).show();
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
            /*fileName = ImageUri.getLastPathSegment()+postRandomName+".jpg";

            final StorageReference file = postImageReference.child("postimages").child(fileName);

            file.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();

                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uri_rg = uri.toString();
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
                        Toast.makeText(ClickPostActivity.this, "Image Upload successful", Toast.LENGTH_SHORT).show();
                        SavingPostInformationToDatabase();

                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(ClickPostActivity.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                    }

                }
            });}
        else
        {
            SavingPostInformationToDatabase();
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

                    HashMap postMap = new HashMap();
                    postMap.put("uid",currentUID);
                    postMap.put("date",saveCurrentDate);
                    postMap.put("time",saveCurrentTime);
                    if(TextUtils.isEmpty(Description) || Description.equals("default"))
                    {
                        postMap.put("description","default");
                    }
                    else {
                        postMap.put("description", Description);
                    }
                    if(ImageUri==null&&uri_rg.equals("textAlone"))
                    {
                        postMap.put("postimage","textAlone");
                    }
                    else
                    {
                        postMap.put("postimage",uri_rg);
                    }
                    postMap.put("username",userProfileName);
                    postMap.put("fullname", userFullName);
                    postMap.put("key",currentUID+postRandomName);
                    if(clickPostRef.child("uid").equals(currentUID))
                    {

                        postMap.put("status", "edit");
                        postMap.put("sharedfrom","none");

                    }
                    else
                    {
                        postMap.put("status", "share");
                        postMap.put("sharedfrom",postsUID);
                    }

                    postsRef.child(postRandomName+currentUID).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful())
                            {
                                sendUserToActivity2(home_actiivty.class);
                                Toast.makeText(ClickPostActivity.this, "updated successfully.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                Toast.makeText(ClickPostActivity.this, "Error Occured , Post updation failed.", Toast.LENGTH_SHORT).show();
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
    }*/

}
