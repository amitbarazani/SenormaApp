package com.acebrico.royalcarribeanapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
            final Reservation tempReservation =dataSnapshot.getValue(Reservation.class);
            //Log.d("TAG", "temp reservation:"+tempReservation.toString());
            TableRow reservationRow = new TableRow(ProfileClientActivity.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
            reservationRow.setLayoutParams(lp);
           // reservationRow.setHorizontalScrollBarEnabled(false);
           // reservationRow.setVerticalScrollBarEnabled(true);


            /*
            ExpandableListView expandableListView = new ExpandableListView(this);
            expandableListView.setAdapter(new ReservationDetailsAdapter(this,tempReservation));
             */


            /*
            Button btn_expand = new Button(this);
            btn_expand.setPadding(0,0,16,0);
            btn_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View expandDetails = null;
                    expandDetails = loadDetails(expandDetails,tempReservation);

                    TableRow reservationDetailsRow = new TableRow(ProfileClientActivity.this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    reservationDetailsRow.setLayoutParams(lp);
                    reservationDetailsRow.addView(expandDetails);
                    tbl_reservations.addView(reservationDetailsRow,counter);
                    //tbl_reservations.removeView(reservationDetailsRow);
                    toggle_contents(view,reservationDetailsRow);
                }
            });
            
             */


            ImageView img_moreDetails = new ImageView(this);
            img_moreDetails.setImageDrawable(getDrawable(R.drawable.help));
            //img_moreDetails.setLayoutParams(new TableRow.LayoutParams(32,32));
            //img_moreDetails.setLayoutParams(new );
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

            TextView tvRow_agent = new TextView(this);
            tvRow_agent.setText(tempReservation.Agent);
            tvRow_agent.setPadding(16,16,16,16);


            TextView tvRow_agentEmail = new TextView(this);
            tvRow_agentEmail.setText(tempReservation.AgentEmail);


           // reservationRow.addView(expandableListView);
           // reservationRow.addView(btn_expand);

            reservationRow.addView(img_moreDetails,new TableRow.LayoutParams(120,120));
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

    /*

    public View loadDetails(View view,Reservation tempReservation) {
        //String listTitle = (String) getGroup(i);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_reservation_details, null);
        }

        TextView tv_ship = (TextView) view.findViewById(R.id.tv_ship);
        TextView tv_roomType = (TextView) view.findViewById(R.id.tv_roomType);
        TextView tv_leavingFrom = (TextView) view.findViewById(R.id.tv_leavingFrom);
        TextView tv_visiting = (TextView) view.findViewById(R.id.tv_visiting);
        TextView tv_returningTo = (TextView) view.findViewById(R.id.tv_returningTo);
        TextView tv_price = (TextView) view.findViewById(R.id.tv_price);

        tv_ship.setText(tempReservation.ship);
        tv_roomType.setText("ROOM TYPE:"+tempReservation.roomCategory);
        tv_leavingFrom.setText("LEAVING FROM:"+tempReservation.departsFrom+","+tempReservation.departsAt);
        tv_visiting.setText("VISITING:"+tempReservation.stopPlace+","+tempReservation.stopTime);
        tv_returningTo.setText("RETURNING TO:"+tempReservation.ArriveTo+","+tempReservation.ArriveAt);
        tv_price.setText(tempReservation.Price);

        return view;
    }




    public static void slide_down(Context ctx, View v) {

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }
    public static void slide_up(Context ctx, View v) {

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public void toggle_contents(View expandButton,View expandLayout){

        if(expandLayout.isShown()){
            slide_up(this,expandButton);
            expandLayout.setVisibility(View.GONE);
        }
        else{
            expandLayout.setVisibility(View.VISIBLE);
            slide_down(this, expandButton);
        }
    }



     */


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

    @Override
    protected void onStop() {
        db.getReference("Reservations/").orderByChild("idClient").equalTo(user.idNumber).removeEventListener(reservationListener);
        super.onStop();
    }
}