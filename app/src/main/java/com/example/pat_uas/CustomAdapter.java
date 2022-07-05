package com.example.pat_uas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context c;
    String myData[],otherData[];
    ArrayList result;
    LayoutInflater inflater;

    public CustomAdapter(Context c, String[] myData, String[] otherData) {
        this.c = c;
        this.myData = myData;
        this.otherData = otherData;
        inflater = LayoutInflater.from(c);
    }

    public CustomAdapter(Context c, String[] myData) {
        this.c = c;
        this.myData = myData;
        inflater = LayoutInflater.from(c);
    }

    public CustomAdapter(Context c, String[] otherData, LayoutInflater inflater) {
        this.c = c;
        this.otherData = otherData;
        this.inflater = inflater;
        inflater = LayoutInflater.from(c);
    }

    public CustomAdapter(Context c, ArrayList result, LayoutInflater inflater) {
        this.c = c;
        this.result = result;
        this.inflater = inflater;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return myData.length;

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.my_lvelement, null);

        TextView user1 = (TextView)  convertView.findViewById(R.id.tv1);
        //TextView user2 = (TextView)  convertView.findViewById(R.id.tv2);
//         TextView user3 = (TextView)  convertView.findViewById(R.id.tv3);
        user1.setText(myData[position]);
        //user2.setText(otherData[position]);
//         user3.setText(myData[position]);

        return convertView;

    }
}
