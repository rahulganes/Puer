package com.puer.rahul.puer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private TextView userStatus, userGender, userCountry, userDOB,username,userProfName;
    private CircleImageView userProfImage;
    private ImageView trophy;

    private Boolean statusEditable;
    
    private DatabaseReference profileReference,starReference;
    private FirebaseAuth mAuth;

    private String currentUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();

        profileReference = FirebaseDatabase.getInstance().getReference().child("Users");
        starReference = FirebaseDatabase.getInstance().getReference().child("Stars");

        statusEditable=true;

        username = (TextView) findViewById(R.id.my_username);
        userProfName = (TextView) findViewById(R.id.my_profilename);
        userStatus = (TextView)findViewById(R.id.my_status);
        userCountry = (TextView)findViewById(R.id.my_country);
        userDOB = (TextView)findViewById(R.id.my_dob);
        userGender = (TextView)findViewById(R.id.my_gender);
        userProfImage = (CircleImageView)findViewById(R.id.my_profile_pic);
        trophy = (ImageView)findViewById(R.id.my_trophy);

        starReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentUID))
                {
                    trophy.setImageResource(R.drawable.star_green);
                    statusEditable=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profileReference.child(currentUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username.setText(getValueFromDataSnap("username",dataSnapshot));
                userDOB.setText(getValueFromDataSnap("dob",dataSnapshot));
                userGender.setText(getValueFromDataSnap("gender",dataSnapshot));
                userCountry.setText(getValueFromDataSnap("country",dataSnapshot));
                userProfName.setText(getValueFromDataSnap("fullname",dataSnapshot));
                if(statusEditable) {
                    userStatus.setText(getValueFromDataSnap("status", dataSnapshot));
                }
                else
                {
                    userStatus.setText("All-Star\nContributor");
                }
                if(!getValueFromDataSnap("profileimage",dataSnapshot).equals("default")) {
                    Picasso.get().load(getValueFromDataSnap("profileimage", dataSnapshot)).placeholder(R.drawable.profile_white).into(userProfImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private String getValueFromDataSnap(String s,DataSnapshot dataSnapshot)
    {
        String res = dataSnapshot.child(s).getValue().toString();
        return res;
    }
}
