package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;

/**
 * Created by ehye on 2015-09-10.
 */
public class CustomGridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private final String[] inputValues;

    public CustomGridAdapter(Context mContext, String[] inputValues){
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_gridview, parent, false);
            TextView textViewGridItem = (TextView) convertView.findViewById(R.id.textView_gridItem);
            //textViewGridItem.setText("p : "+position);
            if(position%6==0 ){
                textViewGridItem.setText(inputValues[position]);
                textViewGridItem.setTextSize(15);
                textViewGridItem.setWidth(200);
                textViewGridItem.setBackgroundColor(Color.parseColor("#7fa2d5"));

            }
            if(inputValues[position].equals("booked")){
                textViewGridItem.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }
        return convertView;
    }
}
