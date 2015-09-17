package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.content.Context;
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

    Context mContext;
    private String[] itemList;


    public CustomGridAdapter(Context context, String[] itemList){
        super();
        mContext = context;
        itemList = itemList;
    }

    @Override
    public int getCount() {
        if(itemList == null)
            return 0;
        return itemList.length;
    }

    @Override
    public Object getItem(int position) {
        return itemList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_gridview, parent, false);
        }

        TextView textViewGridItem = (TextView) convertView.findViewById(R.id.textView_gridItem);
        textViewGridItem.setText(itemList[position]);

        return convertView;
    }
}
