package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.content.Context;
<<<<<<< HEAD
=======
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
>>>>>>> 84933e22fc6fe968b226446fa058f3c3e6a20ee4
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
=======
import android.widget.ArrayAdapter;
>>>>>>> 84933e22fc6fe968b226446fa058f3c3e6a20ee4
import android.widget.BaseAdapter;
import android.widget.TextView;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;

/**
 * Created by ehye on 2015-09-10.
 */
public class CustomGridAdapter extends BaseAdapter {

<<<<<<< HEAD
    Context mContext;
    private String[] itemList;


    public CustomGridAdapter(Context context, String[] itemList){
        super();
        mContext = context;
        itemList = itemList;
=======
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private final String[] inputValues;


    public CustomGridAdapter(Context mContext, String[] inputValues){
       this. mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.inputValues = inputValues;
>>>>>>> 84933e22fc6fe968b226446fa058f3c3e6a20ee4
    }

    @Override
    public int getCount() {
<<<<<<< HEAD
        if(itemList == null)
            return 0;
        return itemList.length;
=======
        return inputValues.length;
>>>>>>> 84933e22fc6fe968b226446fa058f3c3e6a20ee4
    }

    @Override
    public Object getItem(int position) {
<<<<<<< HEAD
        return itemList;
=======
        return null;
>>>>>>> 84933e22fc6fe968b226446fa058f3c3e6a20ee4
    }

    @Override
    public long getItemId(int position) {
<<<<<<< HEAD
        return position;
=======
        return 0;
>>>>>>> 84933e22fc6fe968b226446fa058f3c3e6a20ee4
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
<<<<<<< HEAD
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_gridview, parent, false);
        }

        TextView textViewGridItem = (TextView) convertView.findViewById(R.id.textView_gridItem);
        textViewGridItem.setText(itemList[position]);

=======
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
>>>>>>> 84933e22fc6fe968b226446fa058f3c3e6a20ee4
        return convertView;
    }
}
