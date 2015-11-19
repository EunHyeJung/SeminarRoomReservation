package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

/**
 * Created by hongjuhae on 2015. 10. 25..
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;

public class UserListAdapter extends ArrayAdapter<ItemUser> {

    Context context;
    int resource, textViewResourceId;
    ArrayList<ItemUser> items, tempItems, suggestions;
    HashMap<Integer, Boolean> mCheck;

    AccidentListener accidentListener;


    public UserListAdapter(Context context, int resource, int textViewResourceId, ArrayList<ItemUser> items) {
        super(context, 0, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;

        this.items = new ArrayList<ItemUser>();
        this.items = items;

        tempItems = new ArrayList<ItemUser>(items);
        suggestions = new ArrayList<ItemUser>();

        mCheck = new HashMap<Integer, Boolean>();
        for (int i = 0; i < getCount(); i++) {
            mCheck.put(i, false);
        }
    }

    @Override
    public ItemUser getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View  getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_user, parent, false);
        }

        final ItemUser itemUser = items.get(position);
        if (itemUser != null) {
            TextView textViewUserName = (TextView) convertView.findViewById(R.id.list_item_textView_userName);
            textViewUserName.setText(itemUser.getUserName());

            textViewUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ReservationFormActivity.selectedUsers.put(itemUser.getId(), itemUser.getUserName());
                    accidentListener.upLoadTextView(itemUser.getUserId());
                   // textViewParticipants.append(itemUser.getUserName() + "(" + itemUser.getUserId() + ") ");
                    Toast.makeText(context, itemUser.getUserName() + " 추가", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return convertView;
    }


    public void uiProcessing(){

    }



    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((ItemUser) resultValue).getUserName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (ItemUser itemUser : tempItems) {
                    if (itemUser.getUserName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        suggestions.add(itemUser);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<ItemUser> filterList = (ArrayList<ItemUser>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (ItemUser itemUser : filterList) {
                    add(itemUser);
                    notifyDataSetChanged();
                }
            }
        }
    };

    public interface AccidentListener {
        void upLoadTextView(String addedMemeberId);
    }


}
