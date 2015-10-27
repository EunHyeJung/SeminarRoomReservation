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

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager.AccessHistoryFragment;
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
        datePicker.init(UsingStatusFragment.year,UsingStatusFragment.month, UsingStatusFragment.day,

                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        UsingStatusFragment.year = year;
                        UsingStatusFragment.month = monthOfYear;
                        UsingStatusFragment.day = dayOfMonth;
                        UsingStatusFragment.date = year+"-"+String.format("%02d",(monthOfYear+1)) +"-"+String.format("%02d", dayOfMonth);
                        AccessHistoryFragment.date = UsingStatusFragment.date;
                        dismiss();
                    }
                });
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

}