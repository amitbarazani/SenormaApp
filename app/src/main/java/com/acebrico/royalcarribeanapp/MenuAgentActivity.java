package com.acebrico.royalcarribeanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuAgentActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_signout,btn_chat,btn_profile;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_agent);
        btn_chat = findViewById(R.id.btn_chat);
        btn_signout = findViewById(R.id.btn_signout);
        btn_profile = findViewById(R.id.btn_personalinfo);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        getUserDetails();

        btn_signout.setText("Sign out "+user.fullName);
        btn_profile.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        btn_signout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btn_chat)
        {
            startActivity(new Intent(MenuAgentActivity.this,ChatAgentActivity.class));
        }else if(view == btn_signout)
        {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
            sp.edit().clear().apply();

            startActivity(new Intent(MenuAgentActivity.this,MainActivity.class));

        }else if(view == btn_profile)
        {
            startActivity(new Intent(MenuAgentActivity.this,ProfileAgentActivity.class));
        }
    }

    SharedPreferences sp;
    User user;
    public void getUserDetails()
    {
        user = new User();
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        if(currentUser != null)
        {
            user.fullName = sp.getString("fullName","");
            user.email = sp.getString("email","");
            user.Online = sp.getString("Online","");
            user.password = sp.getString("password","");
            user.role = sp.getString("role","");
            user.idNumber = sp.getString("idNumber","");

        }
    }

}