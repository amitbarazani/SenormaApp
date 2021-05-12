package com.acebrico.royalcarribeanapp;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *  adapter for available rooms
 */
public class PickChatAdapter extends ArrayAdapter<User>
{
    private ArrayList<User> users;
    Context mContext;



    public PickChatAdapter(ArrayList<User> data, Context context) {
        super(context, R.layout.item_person_chat, data);
        this.users = data;
        this.mContext=context;



    }

    ViewGroup viewGroup;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //final String friendsUsername = getItem(position);

        final User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        final View result;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_person_chat, parent, false);
            result=convertView;
        }

        TextView tv_name = (TextView)convertView.findViewById(R.id.tv_name);
        ImageView img_isOnline = (ImageView) convertView.findViewById(R.id.img_isOnline);
        tv_name.setText(user.fullName);
        if(user.Online.equals("Yes"))
        {
            img_isOnline.setImageResource(R.drawable.green_circle);
        }else{
            img_isOnline.setImageResource(R.drawable.red_circle);
        }
        viewGroup = parent;

        // Return the completed view to render on screen
        return convertView;
    }






}

