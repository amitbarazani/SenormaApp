package com.acebrico.royalcarribeanapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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

public class ProfileAgentActivity extends AppCompatActivity implements View.OnClickListener {
    //views
    ProgressBar prg_profileAgent;
    ImageView img_royalcarribean;
    TextView tv_name,tv_email,tv_id;
    TableLayout tbl_reservations;
    //firebase
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase db;
    //variables
    ArrayList<Reservation> reservations;
    Boolean foundReservations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_agent);
        //
        prg_profileAgent = findViewById(R.id.prg_profileagent);
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
        foundReservations = false;
        db.getReference("Reservations/").orderByChild("IDAgent").equalTo(Integer.parseInt(user.idNumber)).addChildEventListener(reservationListener);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!foundReservations) {
                    db.getReference("Reservations/").orderByChild("IDAgent").equalTo(Integer.parseInt(user.idNumber)).removeEventListener(reservationListener);
                    Toast.makeText(ProfileAgentActivity.this, "didn't find any reservations...", Toast.LENGTH_SHORT).show();
                    prg_profileAgent.setVisibility(View.GONE);
                    tv_email.setVisibility(View.VISIBLE);
                    tv_id.setVisibility(View.VISIBLE);
                    tv_name.setVisibility(View.VISIBLE);
                    tbl_reservations.setVisibility(View.GONE);
                }
            }
        },4000);

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
                foundReservations = true;
                prg_profileAgent.setVisibility(View.GONE);
                tv_email.setVisibility(View.VISIBLE);
                tv_id.setVisibility(View.VISIBLE);
                tv_name.setVisibility(View.VISIBLE);
                tbl_reservations.setVisibility(View.VISIBLE);
                showReservation(snapshot);
            }else{
                Log.d("TAG", "test:"+snapshot);
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Log.d("TAG", "snapshot changed:"+snapshot.toString());
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Log.d("TAG", "snapshot removed:"+snapshot.toString());
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Log.d("TAG", "snapshot moved:"+snapshot.toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d("TAG", "snapshot cancelled:");
        }
    };

    Integer counter = 1;
    public void showReservation(DataSnapshot dataSnapshot)
    {
        if(dataSnapshot.exists())
        {
            final Reservation tempReservation =dataSnapshot.getValue(Reservation.class);
            TableRow reservationRow = new TableRow(ProfileAgentActivity.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
            reservationRow.setLayoutParams(lp);


            ImageView img_moreDetails = new ImageView(this);
            img_moreDetails.setImageDrawable(getDrawable(R.drawable.help));
            img_moreDetails.setAdjustViewBounds(true);
            img_moreDetails.setScaleType(ImageView.ScaleType.FIT_CENTER);
            img_moreDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createDetailsDialog(tempReservation);
                }
            });

            TextView tvRow_reservationNumber = new TextView(this);
            tvRow_reservationNumber.setText(tempReservation.ReservationNumber);
            tvRow_reservationNumber.setPadding(16,0,16,0);
            tvRow_reservationNumber.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

            TextView tvRow_status = new TextView(this);
            tvRow_status.setText(tempReservation.Status);
            tvRow_status.setPadding(16,16,16,16);

            TextView tvRow_client = new TextView(this);
            tvRow_client.setText(tempReservation.fullName);
            tvRow_client.setPadding(16,16,16,16);


            TextView tvRow_clientEmail = new TextView(this);
            tvRow_clientEmail.setText(tempReservation.ClientEmail);

            reservationRow.addView(img_moreDetails,new TableRow.LayoutParams(120,120));
            reservationRow.addView(tvRow_reservationNumber);
            reservationRow.addView(tvRow_status);
            reservationRow.addView(tvRow_client);
            reservationRow.addView(tvRow_clientEmail);
            reservationRow.setPadding(0,20,0,20);

            tbl_reservations.addView(reservationRow,counter);
            counter++;

        }else{
            Toast.makeText(ProfileAgentActivity.this, "there are no reservations...", Toast.LENGTH_SHORT).show();
        }
    }


    private void createDetailsDialog(Reservation reservation)
    {
        View view = new View(this);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_reservation_details, null);


        TextView tv_ship = (TextView) view.findViewById(R.id.tv_ship);
        TextView tv_roomType = (TextView) view.findViewById(R.id.tv_roomType);
        TextView tv_leavingFrom = (TextView) view.findViewById(R.id.tv_leavingFrom);
        TextView tv_visiting = (TextView) view.findViewById(R.id.tv_visiting);
        TextView tv_returningTo = (TextView) view.findViewById(R.id.tv_returningTo);
        TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
        tv_ship.setText(reservation.ship);
        tv_roomType.setText("ROOM TYPE:"+reservation.roomCategory);
        tv_leavingFrom.setText("LEAVING FROM:"+reservation.departsFrom+","+reservation.departsAt);
        tv_visiting.setText("VISITING:"+reservation.stopPlace+","+reservation.stopTime);
        tv_returningTo.setText("RETURNING TO:"+reservation.ArriveTo+","+reservation.ArriveAt);
        tv_price.setText(reservation.Price);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).create().show();

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
            Log.d("TAG", "user:"+user.toString());
        }
    }


    @Override
    public void onClick(View view) {
        if(view == img_royalcarribean)
        {
            Intent intent = new Intent(ProfileAgentActivity.this,MenuAgentActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        db.getReference("Reservations/").orderByChild("IDAgent").equalTo(user.idNumber).removeEventListener(reservationListener);
        super.onStop();
    }
}