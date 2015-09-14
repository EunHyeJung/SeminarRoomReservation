package kr.ac.kookmin.cs.capstone2.seminarroomreservation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccessControlFragment extends Fragment {
    ListView SeminarList;
    ListViewAdapter listAdapter;

    public AccessControlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_aceess_control, container, false);

        SeminarList = (ListView)view.findViewById(R.id.SeminarList);
        listAdapter=new ListViewAdapter();

        SeminarList.setAdapter(listAdapter);

        //Add the item to listview
        listAdapter.add("Seminar Room A");
        listAdapter.add("Seminar Room B");
        listAdapter.add("Seminar Room C");
        listAdapter.add("Seminar Room D");
        listAdapter.add("Seminar Room E");
        listAdapter.add("Seminar Room F");
        listAdapter.add("Seminar Room G");

        // Inflate the layout for this fragment
        return view;
    }


}
