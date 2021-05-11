package com.acebrico.royalcarribeanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ChatClientActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_royalcarribean,img_profilePic;
    RelativeLayout rl_chatScreen,rl_pickScreen;
    Button btn_sendMessage,btn_changePic;
    EditText et_message;
    ListView lv_messages,lv_chat;
    TextView tv_name,tv_talkingWith,tv_picFilename;
    //
    
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase db;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_client);
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        img_profilePic = findViewById(R.id.img_profilePic);
        btn_sendMessage = findViewById(R.id.btn_sendMessage);
        btn_changePic = findViewById(R.id.btn_changePic);
        rl_chatScreen = findViewById(R.id.rl_chatScreen);
        rl_pickScreen = findViewById(R.id.rl_pickScreen);
        et_message = findViewById(R.id.et_message);
        lv_chat = findViewById(R.id.lv_chat);
        lv_messages = findViewById(R.id.lv_messages);
        tv_name = findViewById(R.id.tv_name);
        tv_talkingWith = findViewById(R.id.tv_talkingWith);
        tv_picFilename = findViewById(R.id.tv_picFilename);
        //
        mAuth = FirebaseAuth.getInstance();
        currentUser= mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        if(currentUser == null)
        {
            Toast.makeText(this, "You are not connected to an account!", Toast.LENGTH_SHORT).show();
        }

        //
        btn_sendMessage.setOnClickListener(this);
        btn_changePic.setOnClickListener(this);
        img_profilePic.setOnClickListener(this);
        img_royalcarribean.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == img_royalcarribean)
        {
            Intent intent = new Intent(ChatClientActivity.this,MenuClientActivity.class);
            startActivity(intent);
            finish();

        }else if(view == btn_sendMessage)
        {
            
        }else if(view == img_profilePic)
        {
            if(btn_changePic.isShown() && tv_picFilename.isShown())
            {
                btn_changePic.setVisibility(View.GONE);
                tv_picFilename.setVisibility(View.GONE);
            }else{
                btn_changePic.setVisibility(View.VISIBLE);
                tv_picFilename.setVisibility(View.VISIBLE);
            }
        }else if(view == btn_changePic){

        }
    }
}