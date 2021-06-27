package com.acebrico.royalcarribeanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.*;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.*;
import javax.activation.*;

import org.apache.commons.validator.routines.EmailValidator;

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

            if(! (et_fullname.getText().toString().equals("") ||  et_email.getText().toString().equals("") || et_subject.getText().toString().equals(""))) {
                if (EmailValidator.getInstance().isValid(et_email.getText().toString())) {
                    String to = "senoramasenorama@gmail.com";
                    String from = et_email.getText().toString();
                    String subject = et_subject.getText().toString();
                    String name = et_fullname.getText().toString();

                    Intent intent=new Intent(Intent.ACTION_SEND);
                    String[] recipients={to};
                    intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                    intent.putExtra(Intent.EXTRA_SUBJECT,"contact form from "+name);
                    intent.putExtra(Intent.EXTRA_TEXT,subject + "\n" + "sent from:"+from);
                    intent.setType("text/html");
                    startActivity(Intent.createChooser(intent, "Send mail"));

            }else{
                    Toast.makeText(this, "email not valid...", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "one or more of the fields is empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    
}