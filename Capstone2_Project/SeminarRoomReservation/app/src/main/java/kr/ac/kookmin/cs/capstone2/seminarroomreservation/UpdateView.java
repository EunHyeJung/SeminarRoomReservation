package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by eunhye on 2015-11-21.
 */
public  class UpdateView {

    public static void setTextView(TextView textView, String text) {
        textView.setText(text);
    }

    public static void setUsableTextView(TextView textView, boolean usable) {
        textView.setFocusableInTouchMode(usable);
        textView.setFocusable(usable);
        textView.setClickable(usable);
    }

    public static void setUsableEditText(EditText editText, boolean usable) {
        editText.setFocusableInTouchMode(usable);
        editText.setFocusable(usable);
        editText.setClickable(usable);
    }

    public static void makeToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

}
