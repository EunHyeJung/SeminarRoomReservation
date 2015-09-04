package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AdminPageActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_ShowManageBook;
    Button btn_showManageRoom;
    Button btn_showDoorLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        //레이아웃과 매핑
        btn_ShowManageBook=(Button)findViewById(R.id.button_ShowManageBook);
        btn_showManageRoom=(Button)findViewById(R.id.button_ShowManageRoom);
        btn_showDoorLog=(Button)findViewById(R.id.button_ShowDoorLog);

        //버튼 클릭 이벤트
        btn_ShowManageBook.setOnClickListener(this);
        btn_showManageRoom.setOnClickListener(this);
        btn_showDoorLog.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_page, menu);
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

    @Override
    public void onClick(View v) {

    }
}
