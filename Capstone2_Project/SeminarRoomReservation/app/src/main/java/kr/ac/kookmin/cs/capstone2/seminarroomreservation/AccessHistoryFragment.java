package kr.ac.kookmin.cs.capstone2.seminarroomreservation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccessHistoryFragment extends Fragment {

    ListView SeminarLogView;
    ArrayAdapter<String> LogViewAdapter;

    public AccessHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_access_history, container, false);

        LogViewAdapter=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1);

        SeminarLogView=(ListView)view.findViewById(R.id.SeminarLog);
        SeminarLogView.setAdapter(LogViewAdapter);

        LogViewAdapter.add("Admin, Room 609, 03:00 AM");
        LogViewAdapter.add("Admin, Room 611, 05:00 AM");

        return view;
    }


}
