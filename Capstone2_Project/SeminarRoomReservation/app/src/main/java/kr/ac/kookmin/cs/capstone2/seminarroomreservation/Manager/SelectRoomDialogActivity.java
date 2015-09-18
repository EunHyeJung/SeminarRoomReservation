package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;

public class SelectRoomDialogActivity extends AppCompatActivity {
    RadioGroup roomRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_room_dialog);

        roomRadioGroup=(RadioGroup)findViewById(R.id.roomRadioGroup);
    }
}
