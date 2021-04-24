package com.acebrico.royalcarribeanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {
    //views
    ImageView img_royalcarribean;
    EditText et_email,et_fullname,et_subject;
    Button btn_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        //
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        btn_send = findViewById(R.id.btn_send);
        et_email = findViewById(R.id.et_email);
        et_fullname = findViewById(R.id.et_fullname);
        et_subject = findViewById(R.id.et_subject);

        //
        img_royalcarribean.setOnClickListener(this);
        btn_send.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == img_royalcarribean)
        {
            Intent intent = new Intent(ContactUsActivity.this,MainActivity.class);
            startActivity(intent);
        }else if(view == btn_send){

        }
    }
}