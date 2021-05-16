package com.acebrico.royalcarribeanapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class ChatClientActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;


    ImageView img_royalcarribean,img_profilePic,img_backToPickScreen;
    RelativeLayout rl_chatScreen,rl_pickScreen;
    Button btn_sendMessage,btn_changePic;
    EditText et_message;
    ListView lv_pickChat,lv_chat;
    TextView tv_name,tv_talkingWith,tv_picFilename;
    //
    FirebaseStorage firebaseStorage;
    FirebaseAuth mAuth;
    FirebaseUser currentUserAuth;
    FirebaseDatabase db;
    StorageReference storageReference;

    //
    Bitmap imageCurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_client);
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        img_profilePic = findViewById(R.id.img_profilePic);
        img_backToPickScreen = findViewById(R.id.img_back);
        btn_sendMessage = findViewById(R.id.btn_sendMessage);
        btn_changePic = findViewById(R.id.btn_changePic);
        rl_chatScreen = findViewById(R.id.rl_chatScreen);
        rl_pickScreen = findViewById(R.id.rl_pickScreen);
        et_message = findViewById(R.id.et_message);
        lv_chat = findViewById(R.id.lv_chat);
        lv_pickChat = findViewById(R.id.lv_pickChat);
        tv_name = findViewById(R.id.tv_name);
        tv_talkingWith = findViewById(R.id.tv_talkingWith);
        tv_picFilename = findViewById(R.id.tv_picFilename);
        //
        mAuth = FirebaseAuth.getInstance();
        currentUserAuth = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        firebaseStorage =FirebaseStorage.getInstance();
        //
        getUserDetails();
        //
        tv_name.setText(currentUser.fullName);
        Log.d("TAG", "user id:"+ currentUser.idNumber);

        //
        progressPictureAndChats = new ProgressDialog(this);
        progressPictureAndChats.setTitle("Loading data...");
        progressPictureAndChats.show();
        loadPicture();
        //
        if(currentUserAuth == null)
        {
            Toast.makeText(this, "You are not connected to an account!", Toast.LENGTH_SHORT).show();
        }

        //
        btn_sendMessage.setOnClickListener(this);
        btn_changePic.setOnClickListener(this);
        img_profilePic.setOnClickListener(this);
        img_royalcarribean.setOnClickListener(this);
        img_backToPickScreen.setOnClickListener(this);


    }

    String currentUserPic;
    private void loadPicture()
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference storageRef = storage.getReferenceFromUrl("gs://senorma-64974.appspot.com").child("images/").child(currentUser.idNumber+".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    imageCurrentUser = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                    img_profilePic.setImageBitmap(imageCurrentUser);
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            currentUserPic = uri.toString();
                            loadPickChats();

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("TAG", "onFailure:"+exception);
                    loadPickChats();

                }
            });
        } catch (IOException e ) {}

    }

    ProgressDialog progressPictureAndChats;
    private ArrayList<User> users;

    private void loadPickChats()
    {
        users = new ArrayList<>();
        db.getReference("Clients/").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    for (DataSnapshot dataSnapshot: task.getResult().getChildren()) {
                        if(dataSnapshot.exists()) {
                            if(!dataSnapshot.getValue(User.class).fullName.equals(currentUser.fullName))
                            {
                                users.add(dataSnapshot.getValue(User.class));
                            }
                        }
                    }
                    db.getReference("Agents/").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for (DataSnapshot dataSnapshot: task.getResult().getChildren()) {
                                    if(dataSnapshot.exists()) {
                                        users.add(dataSnapshot.getValue(User.class));
                                    }
                                }
                                PickChatAdapter pickChatAdapter = new PickChatAdapter(users,ChatClientActivity.this);
                                lv_pickChat.setAdapter(pickChatAdapter);
                                lv_pickChat.setOnItemClickListener(ChatClientActivity.this);
                                progressPictureAndChats.dismiss();
                                //Toast.makeText(ChatClientActivity.this, "loaded", Toast.LENGTH_SHORT).show();

                            }else{
                                progressPictureAndChats.dismiss();
                                Toast.makeText(ChatClientActivity.this, "there was a problem loading you data...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    ProgressDialog progressSendMessage;
    @Override
    public void onClick(View view) {
        if(view == img_royalcarribean)
        {
            Intent intent = new Intent(ChatClientActivity.this,MenuClientActivity.class);
            startActivity(intent);
            finish();

        }else if(view == btn_sendMessage)
        {
            if(!et_message.getText().equals(""))
            {
                progressSendMessage
                        = new ProgressDialog(this);
                progressSendMessage.setTitle("Sending Message...");
                progressSendMessage.show();
                sendMessage(et_message.getText().toString());
                et_message.clearFocus();


            }
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
                    Intent.createChooser(intent, "Select Image from here..."),
                    PICK_IMAGE_REQUEST);
        }else if(view == img_backToPickScreen)
        {
            rl_pickScreen.setVisibility(View.VISIBLE);
            rl_chatScreen.setVisibility(View.GONE);
            lv_chat.setAdapter(null);
            messages = null;

        }
    }


    SharedPreferences sp;
    User currentUser;
    public void getUserDetails()
    {
        currentUser = new User();
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        if(currentUserAuth != null)
        {
            currentUser.fullName = sp.getString("fullName","");
            currentUser.email = sp.getString("email","");
            currentUser.Online = sp.getString("Online","");
            currentUser.password = sp.getString("password","");
            currentUser.role = sp.getString("role","");
            currentUser.idNumber = sp.getString("idNumber","");
            Log.d("TAG", "getUserDetails: "+ currentUser.toString());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);

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

            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            storageReference =firebaseStorage.getReference();
            StorageReference ref = storageReference.child("images/" + currentUser.idNumber+".jpg");

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(this, "chosen:"+users.get(i).fullName, Toast.LENGTH_SHORT).show();
        if(adapterView == lv_pickChat) {
            messageWith = users.get(i);
            rl_pickScreen.setVisibility(View.GONE);
            rl_chatScreen.setVisibility(View.VISIBLE);
            tv_talkingWith.setText(messageWith.fullName);
            loadImagesAndMessages();


        }else if(view == lv_chat)
        {

        }


    }


    public void sendMessage(String message)
    {
        databaseReference = null;
        Integer idInt1 = Integer.parseInt(currentUser.idNumber);
        Integer idInt2 = Integer.parseInt(messageWith.idNumber);
        if (idInt1 > idInt2)
            databaseReference = db.getReference("privateMessages/").child(messageWith.idNumber+currentUser.idNumber);
        else
            databaseReference = db.getReference("privateMessages/").child(currentUser.idNumber+messageWith.idNumber);
        String messageKey = databaseReference.push().getKey();

        Sender user = new Sender(currentUserPic,currentUserAuth.getUid(),currentUser.fullName);
        String timeStamp = new SimpleDateFormat("MM/dd/yyyy, HH:mm:ss aa").format(new Date());
        Message messageToSend = new Message(message,"No",timeStamp,user);


        databaseReference.child(messageKey).updateChildren(messageToSend.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    loadImagesAndMessages();
                }else{
                    Toast.makeText(ChatClientActivity.this, "there was a problem sending your message...", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "error sending message: "+task.getException());
                }
                et_message.setText("");
                progressSendMessage.dismiss();
            }
        });
    }

    ArrayList<Message> messages;
    ProgressDialog progressLoadingMessages;
    DatabaseReference databaseReference;
    User messageWith;
    public void loadMessages(final Bitmap imageOtherUser, final Bitmap imageCurrentUser) {

        messages =new ArrayList<>();
        databaseReference = null;
        Integer idInt1 = Integer.parseInt(currentUser.idNumber);
        Integer idInt2 = Integer.parseInt(messageWith.idNumber);
        if (idInt1 > idInt2)
            databaseReference = db.getReference("privateMessages/").child(messageWith.idNumber+currentUser.idNumber);
        else
            databaseReference = db.getReference("privateMessages/").child(currentUser.idNumber+messageWith.idNumber);
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    for (DataSnapshot dataSnapshot: task.getResult().getChildren()) {
                        if(dataSnapshot.exists())
                        {
                            messages.add(dataSnapshot.getValue(Message.class));
                        }
                    }
                    lv_chat.setAdapter(new MessagesAdapter(messages,ChatClientActivity.this,imageCurrentUser,imageOtherUser,currentUser.fullName));
                    progressLoadingMessages.dismiss();
                }
            }
        });

    }




    private void loadImagesAndMessages()
    {
        progressLoadingMessages  = new ProgressDialog(this);
        progressLoadingMessages.setTitle("Loading messages...");
        progressLoadingMessages.show();
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://senorma-64974.appspot.com").child("images/").child(messageWith.idNumber+".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap imageOtherUser = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    loadMessages(imageOtherUser,imageCurrentUser);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("TAG", "onFailure:"+exception);
                    loadMessages(null,imageCurrentUser);

                }
            });
        } catch (IOException e ) {}
    }

}