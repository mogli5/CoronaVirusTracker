package com.mogli.coronavirustracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class LocationAdapter extends ArrayAdapter<Location> {

    public LocationAdapter(@NonNull Context context, List<Location>locations) {
        super(context,0,locations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View locationView = convertView;
        if(locationView == null){
            locationView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        final Location location = getItem(position);

        String cState = location.getcState();
        String cCountry = location.getcCountry();
        int cICases = location.getcNumInfected();
        int cDCases = location.getcNumDeaths();
        int cRCases = location.getcNumRecovered();

        TextView locationTextView = (TextView)locationView.findViewById(R.id.list_location);
        TextView infectedTextView = (TextView)locationView.findViewById(R.id.listView_infected);
        TextView dTextView = (TextView)locationView.findViewById(R.id.listView_d);
        TextView recoveredTextView = (TextView)locationView.findViewById(R.id.listView_recovered);

        if(cState.length() == 0){
            locationTextView.setText(cCountry);
        }else{
            locationTextView.setText(cState + ", " + cCountry);
        }

        infectedTextView.setText(": " + Integer.toString(cICases));
        dTextView.setText(": " + Integer.toString(cDCases));
        recoveredTextView.setText(": " + Integer.toString(cRCases));

        return locationView;
    }
}
