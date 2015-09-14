package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ControlDialogActivity extends AppCompatActivity {
    Button OpenBtn;
    Button CloseBtn;
    TextView DoorStatusText;

    String RoomName="";

    RestRequestHelper requestHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_dialog);

        requestHelper=RestRequestHelper.newInstance("doorcontrol");

        //클릭한 세미나방 이름 가져오기
        Intent intent=getIntent();
        RoomName=intent.getExtras().getString("Room");

        //매핑하기
        OpenBtn=(Button)findViewById(R.id.btn_DoorOpen);
        CloseBtn=(Button)findViewById(R.id.btn_DoorClose);
        DoorStatusText=(TextView)findViewById(R.id.text_DoorStatus);

        OpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestHelper.controlDoor("admin",RoomName,true);
                DoorStatusText.setText("Door Status : Open");
            }
        });

        CloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestHelper.controlDoor("admin",RoomName,false);
                DoorStatusText.setText("Door Status : Close");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_control_dialog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
