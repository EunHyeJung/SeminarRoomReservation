package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;

/**
 * Created by ehye on 2015-09-15.
 */
public class CustomListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private final String[] inputValues;


    public CustomListAdapter(Context mContext, String[] inputValues){
        this. mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.inputValues = inputValues;

    }

    @Override
    public int getCount() {
       return inputValues.length;
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
        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.item_listview, parent, false);
            setContent(convertView, position);
        }

        return convertView;
    }

    public void setContent(View convertView, final int position){
        TextView listItem =  (TextView) convertView.findViewById(R.id.textView_listItem);
        listItem.setText(inputValues[position]);
    }
}
