package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Join.JoinActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;

/**
 * Created by ehye on 2015-09-10.
 */
public class CustomGridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Integer> inputValues;

    private  TextView textViewGridItem;

    public CustomGridAdapter(Context mContext, ArrayList<Integer> inputValues){
        this. mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.inputValues = inputValues;
        System.out.println("inputValues in CustomGridAdapter : "+inputValues);
    }

    @Override
    public int getCount() {
        return 120;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_gridview, parent, false);
            textViewGridItem = (TextView) convertView.findViewById(R.id.textView_gridItem);
            textViewGridItem.setText(""+position);
/*

            if(position == 7 || position== 12)
                textViewGridItem.setBackgroundColor(Color.parseColor("#90C84646"));
            if(position == 51 || position == 56 || position == 61)
                textViewGridItem.setBackgroundColor(Color.parseColor("#90E86E59"));

*/



            textViewGridItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ReservationFormActivity.class);        //  
                    mContext.startActivity(intent);
                }
            });


        }
        return convertView;
    }


    /* -------------------- End Of getView -------------------- */
}
