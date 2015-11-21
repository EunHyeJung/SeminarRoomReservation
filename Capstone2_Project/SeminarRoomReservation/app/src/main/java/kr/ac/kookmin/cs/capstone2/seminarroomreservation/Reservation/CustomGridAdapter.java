package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model.ReservationsInfo;
import static kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model.DefinedValues.*;

/**
 * Created by - on 2015-10-13.
 */
public class CustomGridAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Integer> inputValues;

    private TextView textViewGridItem;
    private ReservationsInfo[] reservationsInfo;

    HashMap<Integer, Integer> temp;

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

        temp = new HashMap<Integer, Integer>();
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
                        if (j == position) {        // reservationStatus 값 define으로 바꿀 것
                            if (reservationsInfo[i].getReservationStatus() == RESERVATION_STANDBY){
                                textViewGridItem.setText(mContext.getString(R.string.status_standby));
                                textViewGridItem.setBackgroundColor(Color.parseColor("#90C84646"));
                                textViewGridItem.setHint(String.valueOf(reservationsInfo[i].getReservationId()));
                               temp.put(position, reservationsInfo[i].getReservationId());
                            }
                            else if (reservationsInfo[i].getReservationStatus() == RESERVED) {
                                textViewGridItem.setText(mContext.getString(R.string.status_reserved));
                                textViewGridItem.setBackgroundColor(Color.parseColor("#90008EC8"));
                                textViewGridItem.setHint(String.valueOf(reservationsInfo[i].getReservationId()));
                                temp.put(position, reservationsInfo[i].getReservationId());
                            }
                        }
                    }
                }
            }

        }
        return convertView;
    }


        /* -------------------- End Of getView -------------------- */
}

