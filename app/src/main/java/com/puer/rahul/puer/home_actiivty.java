package com.puer.rahul.puer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;

public class home_actiivty extends AppCompatActivity {


    private boolean textPostBool;

    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private RecyclerView postList;
    private Toolbar mToolbar;

    private ImageButton addNewPost;

    private CircleImageView navProfileImage;
    private ImageView allStarImage;
    private TextView nav_profile_name,allStarText;
    private SwipeRefreshLayout pullToRefresh;

    private String current_UID,user_status;
    private String postsUID;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef,PostsRef,StarsRef;
    private  DatabaseReference viewClickPostRef,CorrectPostRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_actiivty);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        StarsRef = FirebaseDatabase.getInstance().getReference().child("Stars");


        if(mAuth.getCurrentUser()!=null)
        {
            current_UID = mAuth.getCurrentUser().getUid();
        }

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        addNewPost = (ImageButton)findViewById(R.id.add_new_post);

        addNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(home_actiivty.this, drawerLayout, R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);


        postList = (RecyclerView) findViewById(R.id.all_users_post);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        navProfileImage = (CircleImageView)navView.findViewById(R.id.nav_profile_image);
        nav_profile_name = (TextView)navView.findViewById(R.id.nav_user_full_name);
        allStarImage = (ImageView)navView.findViewById(R.id.allstar);
        allStarText = (TextView)navView.findViewById(R.id.allstarcontrib);

    if(mAuth.getCurrentUser()!=null){
        usersRef.child(current_UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    if (dataSnapshot.hasChild("fullname")) {
                        String full_name = dataSnapshot.child("fullname").getValue().toString();
                        nav_profile_name.setText(full_name);
                        user_status = dataSnapshot.child("status").getValue().toString();
                    }
                    if (dataSnapshot.hasChild("profileimage")) {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile_white).into(navProfileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        StarsRef.child(current_UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    allStarImage.setVisibility(View.VISIBLE);
                    allStarText.setVisibility(View.VISIBLE);
                }
                else
                {
                    if(!user_status.equals("default_status"))
                    {
                    allStarImage.setImageResource(R.drawable.heart);
                    allStarText.setText(user_status);}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });

    /*pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //DisplayAllUsersPosts();
        }
    });*/
    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();
        if(currentUser==null)
        {
            sendUserToLoginActivity();
        }
        else
        {
            checkUserExistence();
        }
        DisplayAllUsersPosts();
    }

    private void DisplayAllUsersPosts()
    {
        FirebaseRecyclerOptions<Posts> options=new FirebaseRecyclerOptions.Builder<Posts>().setQuery(PostsRef,Posts.class).build();
        FirebaseRecyclerAdapter<Posts, PostsViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(options)
        {
            @Override protected void onBindViewHolder(@NonNull final PostsViewHolder holder, int position, @NonNull final Posts model)
            {
                    final String postKey = getRef(position).getKey();
                    holder.date.setText(model.getDate());
                    holder.time.setText(model.getTime());

                    CorrectPostRef = FirebaseDatabase.getInstance().getReference().child("Users").child(model.uid);
                    CorrectPostRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                holder.username.setText(dataSnapshot.child("fullname").getValue().toString());
                                if(dataSnapshot.child("profileimage").getValue().toString()!=null || dataSnapshot.child("profileimage").getValue().toString().equals("default"))
                            {
                                if((!dataSnapshot.child("profileimage").getValue().toString().equals("default")))
                                {
                                    Picasso.get().load(dataSnapshot.child("profileimage").getValue().toString()).placeholder(R.drawable.profile).into(holder.user_post_image);
                                }
                                if(model.getStatus().equals("share"))
                                {
                                    holder.post_status.setText("has shared a post");
                                }
                            }}

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    if((model.getDescription().equals("DefaultNull")) || model.getDescription().equals("default"))
                    {
                        holder.description.setVisibility(View.GONE);
                    }
                    else {
                        holder.description.setText(model.getDescription());
                    }
                    if(!(model.getpostimage().equals("textAlone"))) {
                        Picasso.get().load(model.getpostimage()).into(holder.postImage);
                    }
                    else
                    {
                        holder.postImage.setVisibility(View.GONE);
                    }

                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopUpActivity popUpActivity = new PopUpActivity();
                            popUpActivity.setValue(postKey);
                            popUpActivity.show(getSupportFragmentManager(),"popup");
                        }
                    });

            }
            @NonNull
            @Override
            public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.all_posts_layout,parent,false);
                PostsViewHolder viewHolder=new PostsViewHolder(view);
                return viewHolder;

            }


        };

        firebaseRecyclerAdapter.setHasStableIds(true);
        postList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

    public void showPopup(View v) {

            PopupMenu popup = new PopupMenu(this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.popup_menu, popup.getMenu());
            Object menuHelper;
            Class[] argTypes;
            try {
                Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                fMenuHelper.setAccessible(true);
                menuHelper = fMenuHelper.get(popup);
                argTypes = new Class[]{boolean.class};
                menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
            } catch (Exception e) {

            }

        /*@SuppressLint("RestrictedAPI") MenuPopupHelper menuHelper = new MenuPopupHelper(getApplicationContext(), (MenuBuilder) popup.getMenu(), v);
        menuHelper.setForceShowIcon(true);
        menuHelper.setGravity(Gravity.END);
        menuHelper.show();*/

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId())
                    {
                        case R.id.post_image_menu:
                        {
                            textPostBool = false;
                            sendUserToActivityWithBoolKey(postActivity.class,textPostBool);
                            break;
                        }
                        case R.id.post_text_menu:
                        {
                            textPostBool = true;
                            sendUserToActivityWithBoolKey(postActivity.class,textPostBool);
                            break;
                        }
                    }
                    return false;
                }
            });

            popup.show();
    }


    public class PostsViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;
        private TextView username,date,time,description,post_status;
        private CircleImageView user_post_image;
        private ImageView postImage;
        private ImageButton popup;
        PostsViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
            username=itemView.findViewById(R.id.post_userName);
            date=itemView.findViewById(R.id.post_date);
            time=itemView.findViewById(R.id.post_time);
            description=itemView.findViewById(R.id.alp_post_description);
            postImage=itemView.findViewById(R.id.alp_post_image);
            user_post_image=itemView.findViewById(R.id.post_profile_image);
            post_status = itemView.findViewById(R.id.alp_text);
        }

    }

    private void sendUserToActivityWithBoolKey(Class<?> context,Boolean textPostBool)
    {
        Intent clickPostIntent = new Intent(this,context);
        clickPostIntent.putExtra("BoolKey",textPostBool);
        startActivity(clickPostIntent);
    }


    private void checkUserExistence() {

        final String current_user_id = mAuth.getCurrentUser().getUid();
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!(dataSnapshot.hasChild(current_user_id)))
                {
                    sendUserToSetupActivity();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendUserToSetupActivity() {
        Intent setupintent = new Intent(home_actiivty.this,setupActivity.class);
        setupintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupintent);
        finish();
    }

    private void sendUserToActivity2(Class<?> context)
    {
        Intent setupintent = new Intent(home_actiivty.this,context);
        setupintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupintent);
        finish();
    }

    private void sendUserToActivity(Class<?> context)
    {
        Intent activityintent = new Intent(this, context);
        startActivity(activityintent);
    }



    private void sendUserToLoginActivity() {
        Intent loginintent = new Intent(home_actiivty.this,loginActivity.class);
        loginintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginintent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_profile:
                sendUserToActivity(ProfileActivity.class);
                break;
            case R.id.nav_new_post:
                sendUserToActivityWithBoolKey(postActivity.class,false);
                break;
            case R.id.nav_home:
                Toast.makeText(this,"home",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_friends:
                Toast.makeText(this,"frnds",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_find_friends:
                sendUserToActivity(FindFriends.class);
                break;
            case R.id.nav_messages:
                Toast.makeText(this,"messages",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                sendUserToActivity(SettingsActivity.class);
                break;
            case R.id.nav_logout:
                allowUserToLogOut();
                break;
        }
    }

    private void allowUserToLogOut() {

        Toast.makeText(this,"logged out",Toast.LENGTH_SHORT).show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuth.signOut();
                sendUserToLoginActivity();
            }
        },600);

    }

    private void toastMessage(String s)
    {
        Toast.makeText(home_actiivty.this,s,Toast.LENGTH_LONG).show();
    }
}
