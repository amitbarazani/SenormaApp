package com.acebrico.royalcarribeanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ChatAgentActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_royalcarribean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_agent);
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        img_royalcarribean.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == img_royalcarribean)
        {
            Intent intent = new Intent(ChatAgentActivity.this,MenuAgentActivity.class);
            startActivity(intent);
            finish();

        }
    }
}