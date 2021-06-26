package com.acebrico.royalcarribeanapp;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 *  adapter for available rooms
 */
public class MessagesAdapter extends ArrayAdapter<Message>
{
    private ArrayList<Message> messages;
    Context mContext;
    private Bitmap imageCurrentUser;
    private Bitmap imageOtherUser;
    private String currentUserName;



    public MessagesAdapter(ArrayList<Message> data, Context context,Bitmap imageCurrentUser,Bitmap imageOtherUser,String currentUserName) {
        super(context, R.layout.item_person_chat, data);
        this.messages = data;
        this.mContext=context;
        this.imageCurrentUser=imageCurrentUser;
        this.imageOtherUser=imageOtherUser;
        this.currentUserName=currentUserName;
    }

    ViewGroup viewGroup;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //final String friendsUsername = getItem(position);

        final Message message = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        final View result;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_person_chat, parent, false);
            result=convertView;
        }

        TextView tv_senderName = (TextView)convertView.findViewById(R.id.tv_senderName);
        TextView tv_content = (TextView)convertView.findViewById(R.id.tv_content);
        TextView tv_timestamp = (TextView)convertView.findViewById(R.id.tv_timestamp);
        ImageView img_profilePic = (ImageView) convertView.findViewById(R.id.img_profilePic);

        tv_senderName.setText(message.user.name);

        if (this.currentUserName.equals(message.user.name)) {
            if(imageCurrentUser!= null)
                img_profilePic.setImageBitmap(imageCurrentUser);
        } else {
            if(imageOtherUser!= null)
                img_profilePic.setImageBitmap(imageOtherUser);
        }
        tv_content.setText(message.content);
        tv_timestamp.setText(message.timestamp);

        viewGroup = parent;

        // Return the completed view to render on screen
        return convertView;
    }







}

