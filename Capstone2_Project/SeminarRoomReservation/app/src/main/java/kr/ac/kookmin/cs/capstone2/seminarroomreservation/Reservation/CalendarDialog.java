package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;

import java.util.Date;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;

/**
 * Created by ehye on 2015-09-21.
 */
public class CalendarDialog extends Dialog implements DialogInterface.OnClickListener {


    public CalendarDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_calendar);


        DatePicker datePicker = (DatePicker) findViewById(R.id.datepicker);
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                       UsingStatusFragment.date = year+" / "+(monthOfYear+1)+" / "+dayOfMonth;
                        dismiss();
                    }
                });
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

}
