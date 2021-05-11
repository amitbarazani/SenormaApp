package com.acebrico.royalcarribeanapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


public class ChatClientActivity extends AppCompatActivity implements View.OnClickListener {
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;


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
    StorageReference storageReference;

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
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(
                            intent,
                            "Select Image from here..."),
                    PICK_IMAGE_REQUEST);
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


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img_profilePic.setImageBitmap(bitmap);
                uploadImage();
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }


    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            storageReference =firebaseStorage.getReference();
            StorageReference ref = storageReference.child("images/" + user.idNumber+".jpg");

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(ChatClientActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(ChatClientActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                                }
                            });
        }
    }

}