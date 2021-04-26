package com.acebrico.royalcarribeanapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProfileClientActivity extends AppCompatActivity implements View.OnClickListener {
    //views
    ProgressBar prg_profileClient;
    ImageView img_royalcarribean;
    TextView tv_name,tv_email,tv_id;
    TableLayout tbl_reservations;
    //firebase
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase db;
    //variables
    ArrayList<Reservation> reservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_client);
        //
        prg_profileClient = findViewById(R.id.prg_profileclient);
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        tbl_reservations = findViewById(R.id.tbl_reservations);
        tv_email = findViewById(R.id.tv_email);
        tv_name = findViewById(R.id.tv_name);
        tv_id = findViewById(R.id.tv_id);
        //
        tv_email.setVisibility(View.GONE);
        tv_name.setVisibility(View.GONE);
        tv_id.setVisibility(View.GONE);
        tbl_reservations.setVisibility(View.GONE);
        //
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        getUserDetails();
        //
        reservations = new ArrayList<>();
        db.getReference("Reservations/").orderByChild("idClient").equalTo(user.idNumber).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                prg_profileClient.setVisibility(View.GONE);
                tv_email.setVisibility(View.VISIBLE);
                tv_id.setVisibility(View.VISIBLE);
                tv_name.setVisibility(View.VISIBLE);
                tbl_reservations.setVisibility(View.GONE);
                if(task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    showReservations(dataSnapshot);
                }else{
                    Toast.makeText(ProfileClientActivity.this, "there was a problem loading your reservations...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //
        tv_id.setText("ID number:"+user.idNumber);
        tv_email.setText("Email:"+user.email);
        tv_name.setText("Name:"+user.fullName);
        //
        img_royalcarribean.setOnClickListener(this);

    }
    public void showReservations(DataSnapshot dataSnapshot)
    {
        if(dataSnapshot.exists())
        {
            Log.d("TAG", "showReservations: "+dataSnapshot.getValue());
        }else{
            Toast.makeText(ProfileClientActivity.this, "there are no reservations...", Toast.LENGTH_SHORT).show();
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

        }
    }


    @Override
    public void onClick(View view) {
        if(view == img_royalcarribean)
        {
            Intent intent = new Intent(ProfileClientActivity.this,MenuClientActivity.class);
            startActivity(intent);
        }
    }
}