package com.puer.rahul.puer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PopUpActivity extends DialogFragment {

    private String postKey;
    private String saveCurrentTime,saveCurrentDate,postRandomName,saveCurrentTime2,post_UID,fullname,username,profileimage,current_UID;

    private Button edit,delete,report,share;
    private DatabaseReference clickPostRef,postsRef;
    private FirebaseAuth mAuth;


    public void setValue(String st)
    {
        this.postKey = st;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.activity_pop_up, null);
        clickPostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey);
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        current_UID = mAuth.getCurrentUser().getUid();
        post_UID = current_UID;
        // Get your views by using view.findViewById() here and do your listeners.

        edit = (Button)view.findViewById(R.id.popup_makechanges);
        delete = (Button)view.findViewById(R.id.popup_delete);
        report = (Button)view.findViewById(R.id.popup_report);
        share = (Button)view.findViewById(R.id.popup_share);



        clickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                if(!dataSnapshot.child("uid").getValue().equals(mAuth.getCurrentUser().getUid()))
                {
                    post_UID = dataSnapshot.child("uid").getValue().toString();
                    edit.setText("View Post");
                    delete.setText("Save Post");
                }}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_UID.equals(post_UID))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clickPostIntent = new Intent(getContext(),ClickPostActivity.class);
                clickPostIntent.putExtra("Post_Key",postKey);
                startActivity(clickPostIntent);
                getActivity().getFragmentManager().popBackStack();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePost(postKey);
            }
        });

        // Set the dialog layout
        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
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
                        Toast.makeText(getContext(),"Shared successfully",Toast.LENGTH_LONG).show();
                    }
                });

                sendUserToActivity2(home_actiivty.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void deletePost()
    {
        clickPostRef.removeValue();
        sendUserToActivity2(home_actiivty.class);
        toastMessage("Post deleted");

    }

    private void toastMessage(String s)
    {
        Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
    }

    private void sendUserToActivity2(Class<?> context)
    {
        Intent activityintent = new Intent(getContext(),context);
        activityintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(activityintent);
    }

}
