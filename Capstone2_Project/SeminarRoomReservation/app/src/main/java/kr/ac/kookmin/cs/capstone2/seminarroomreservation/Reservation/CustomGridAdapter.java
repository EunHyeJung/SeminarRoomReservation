package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.ReservationsInfo;

/**
 * Created by eunhye on 2015-10-13.
 */
public class CustomGridAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Integer> inputValues;

    private TextView textViewGridItem;
    private ReservationsInfo[] reservationsInfo;

    public CustomGridAdapter(Context mContext, ArrayList<Integer> inputValues) {
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.inputValues = inputValues;
    }

    public CustomGridAdapter(Context mContext, ArrayList<Integer> inputValues,
                             ReservationsInfo[] reservationsInfo) {
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.inputValues = inputValues;
        this.reservationsInfo = reservationsInfo;
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

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_gridview, parent, false);
            textViewGridItem = (TextView) convertView.findViewById(R.id.textView_gridItem);

            if (reservationsInfo != null) {
                for (int i = 0; i < reservationsInfo.length; i++) {
                    int startTime = reservationsInfo[i].getStartTime();
                    int endTme = reservationsInfo[i].getEndTime();
                    for (int j = startTime; j <= endTme; j += 5) {
                        if (j == position) {
                            if (reservationsInfo[i].getReservationStatus() == 1)
                                textViewGridItem.setBackgroundColor(Color.parseColor("#90C84646"));
                            else if (reservationsInfo[i].getReservationStatus() == 2)
                                textViewGridItem.setBackgroundColor(Color.parseColor("#90008EC8"));
                        }
                    }
                }
            }

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

