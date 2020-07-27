package com.onuroapplications.sequence;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.onuroapplications.memorytrainer.R;

import java.util.ArrayList;

public class SequenceAdapter extends ArrayAdapter<String> {

    private ArrayList<String> arrList;
    private final LayoutInflater inflater;
    private final int resource;

    private final String TAG = "SequenceAdapter";

    public SequenceAdapter(Context context, int resource, ArrayList<String> dataArrayList) {
        super(context, resource);
        this.arrList = dataArrayList;
        this.inflater = LayoutInflater.from(context);
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return arrList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.appTextView);
        textView.setText(arrList.get(position));
        return view;
    }
}
