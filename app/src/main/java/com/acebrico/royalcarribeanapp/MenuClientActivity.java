package com.acebrico.royalcarribeanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuClientActivity extends AppCompatActivity implements View.OnClickListener {
    //views
    Button btn_personalinfo;
    Button btn_signOut;
    //firebase
    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_client);
        //
        btn_personalinfo = findViewById(R.id.btn_personalinfo);
        btn_signOut = findViewById(R.id.btn_signout);

        //
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        getUserDetails();
        //
        btn_signOut.setText("Sign out "+user.fullName);
        btn_personalinfo.setOnClickListener(this);
        btn_signOut.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        if(view == btn_personalinfo)
        {
            startActivity(new Intent(MenuClientActivity.this,ProfileClientActivity.class));
        }else if(view == btn_signOut)
        {
            mAuth.signOut();
            sp.edit().clear().apply();
            startActivity(new Intent(MenuClientActivity.this,MainActivity.class));
        }
    }
}