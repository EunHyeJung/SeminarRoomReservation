package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class CustomExitDialog extends Dialog{

    Context context;
    Button buttonOk;
    Button buttonCancel;
    private View.OnClickListener clickListener;

    public CustomExitDialog(Context context, View.OnClickListener clickListener) {
        super(context);
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dialog);

        findViewByIds();

        if(clickListener != null){
            buttonCancel.setOnClickListener(clickListener);
            buttonOk.setOnClickListener(clickListener);
        }
    }

    public void findViewByIds(){
        buttonOk = (Button) findViewById(R.id.button_ok);
        buttonCancel = (Button) findViewById(R.id.button_cancel);

    }


}
