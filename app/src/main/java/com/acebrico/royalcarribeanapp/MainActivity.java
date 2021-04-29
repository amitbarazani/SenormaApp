package com.acebrico.royalcarribeanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_loginAgents,btn_contactus,btn_loginCustomers;
    FirebaseAuth mAuth;
    TextView tv_options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null)
        {

            SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
            Log.d("TAG", "shared prefrences:"+sp.getAll());
            if(sp.getString("role","").equals("Client"))
            {
                startActivity(new Intent(MainActivity.this,MenuClientActivity.class));
            }else if(sp.getString("role","").equals("Agent")){
                startActivity(new Intent(MainActivity.this,MenuAgentActivity.class));
            }
            finish();
        }
        //
        btn_loginAgents = findViewById(R.id.btn_loginAgents);
        btn_loginCustomers = findViewById(R.id.btn_loginCustomers);
        btn_contactus = findViewById(R.id.btn_contactUs);
        tv_options = findViewById(R.id.tv_options);
        //
        btn_loginAgents.setOnClickListener(this);
        btn_contactus.setOnClickListener(this);
        btn_loginCustomers.setOnClickListener(this);
        tv_options.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btn_loginAgents)
        {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.putExtra("role","Agent");
            startActivity(intent);
        }else if(view == btn_loginCustomers)
        {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.putExtra("role","Client");
            startActivity(intent);
        }else if(view == btn_contactus)
        {
            Intent intent = new Intent(MainActivity.this,ContactUsActivity.class);
            startActivity(intent);
        }else if(view == tv_options)
        {
            Uri uri = Uri.parse("https://www.royalcaribbean.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}