package kr.ac.kookmin.cs.capstone2.seminarroomreservation.User;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.JsonObject;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.UserInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class KeyFragment extends Fragment implements View.OnClickListener {
    ImageButton btnSmartKey;
    TextView textStatus;
    RestRequestHelper restRequestHelper;
    JsonObject info;
    public KeyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_key, container, false);
        restRequestHelper = RestRequestHelper.newInstance(); //네트워크 초기화
        init(view); //이외의 부분 초기화

        btnSmartKey.setOnClickListener(this);

        return view;
    }

    public void init(View view){
        btnSmartKey = (ImageButton)view.findViewById(R.id.button_smartkey);
        textStatus = (TextView)view.findViewById(R.id.text_smart_text);

        info = new JsonObject();
        info.addProperty("Id", UserInfo.getUserId());

    }

    @Override
    public void onClick(View v) {

    }
}
