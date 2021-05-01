package com.acebrico.royalcarribeanapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

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
        db.getReference("Reservations/").orderByChild("idClient").equalTo(user.idNumber).addChildEventListener(reservationListener);
        //^close the listener

        //
        tv_id.setText("ID number:"+user.idNumber);
        tv_email.setText("Email:"+user.email);
        tv_name.setText("Name:"+user.fullName);
        //
        img_royalcarribean.setOnClickListener(this);

    }

    private ChildEventListener reservationListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if(snapshot.exists())
            {
                prg_profileClient.setVisibility(View.GONE);
                tv_email.setVisibility(View.VISIBLE);
                tv_id.setVisibility(View.VISIBLE);
                tv_name.setVisibility(View.VISIBLE);
                tbl_reservations.setVisibility(View.VISIBLE);
                showReservation(snapshot);
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    Integer counter = 1;
    public void showReservation(DataSnapshot dataSnapshot)
    {
        if(dataSnapshot.exists())
        {
            //Log.d("TAG", "showReservations: "+dataSnapshot.getValue());
            Reservation tempReservation =dataSnapshot.getValue(Reservation.class);
            //Log.d("TAG", "temp reservation:"+tempReservation.toString());
            TableRow reservationRow = new TableRow(ProfileClientActivity.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            reservationRow.setLayoutParams(lp);



            TextView tvRow_reservationNumber = new TextView(this);
            tvRow_reservationNumber.setText(tempReservation.ReservationNumber);
            tvRow_reservationNumber.setPadding(16,0,16,0);
            tvRow_reservationNumber.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

            TextView tvRow_status = new TextView(this);
            tvRow_status.setText(tempReservation.Status);
            tvRow_status.setPadding(16,16,16,16);

            TextView tvRow_agent = new TextView(this);
            tvRow_agent.setText(tempReservation.Agent);
            tvRow_agent.setPadding(16,16,16,16);


            TextView tvRow_agentEmail = new TextView(this);
            tvRow_agentEmail.setText(tempReservation.AgentEmail);


            reservationRow.addView(tvRow_reservationNumber);
            reservationRow.addView(tvRow_status);
            reservationRow.addView(tvRow_agent);
            reservationRow.addView(tvRow_agentEmail);
            //reservationRow.setDividerDrawable(new ColorDrawable(Color.BLACK));
            //reservationRow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            reservationRow.setPadding(0,20,0,20);

            tbl_reservations.addView(reservationRow,counter);
            counter++;

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