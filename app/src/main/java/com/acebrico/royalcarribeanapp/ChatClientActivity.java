package com.acebrico.royalcarribeanapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class ChatClientActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_royalcarribean,img_profilePic;
    RelativeLayout rl_chatScreen,rl_pickScreen;
    Button btn_sendMessage,btn_changePic;
    EditText et_message;
    ListView lv_messages,lv_chat;
    TextView tv_name,tv_talkingWith,tv_picFilename;
    //
    FirebaseStorage firebaseStorage;
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
        firebaseStorage =FirebaseStorage.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //
        getUserDetails();
        //
        tv_name.setText(user.fullName);
        Log.d("TAG", "user id:"+user.idNumber);
        StorageReference storageRef = storage.getReferenceFromUrl("gs://senorma-64974.appspot.com").child("images/").child(user.idNumber+".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    img_profilePic.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("TAG", "onFailure:"+exception);
                }
            });
        } catch (IOException e ) {}


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
            Log.d("TAG", "getUserDetails: "+user.toString());
        }
    }

}