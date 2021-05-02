package com.acebrico.royalcarribeanapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ReservationDetailsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Reservation reservation;


    public ReservationDetailsAdapter(Context context, Reservation reservation) {
        this.context = context;
        this.reservation = reservation;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        //String listTitle = (String) getGroup(i);
       return null; }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_reservation_details, null);
        }

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

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
