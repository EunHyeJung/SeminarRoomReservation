package kr.ac.kookmin.cs.capstone2.seminarroomreservation.User;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.TransmissionUserInfo;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model.RoomInfo;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model.UserInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class KeyFragment extends Fragment{
    Button btnSmartKeyOpen;
    Button btnSmartKeyClose;
    TextView textDate;
    TextView textStime;
    TextView textEtime;
    TextView textRoom;
    RestRequestHelper restRequestHelper;
    TransmissionUserInfo info;
    String roomName;
    int roomId;

    final int open = 1;
    final int close = 0;

    public KeyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_key, container, false);
        restRequestHelper = RestRequestHelper.newInstance(); //네트워크 초기화
        init(view); //이외의 부분 초기화

        btnSmartKeyOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restRequestHelper.controlDoor(UserInfo.getId(), roomId, open, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        int result = jsonObject.get("result").getAsInt();

                        switch (result) {
                            //문 관리 실패
                            case 0:
                                Toast.makeText(getContext(), "거절 되었습니다.", Toast.LENGTH_SHORT).show();
                                break;
                            //문 열기 가능
                            case 1:
                                Toast.makeText(getContext(), "문이 열렸습니다.", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        });

        btnSmartKeyClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restRequestHelper.controlDoor(UserInfo.getId(), roomId, close, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        int result = jsonObject.get("result").getAsInt();

                        switch (result) {
                            //문 관리 실패
                            case 0:
                                Toast.makeText(getContext(), "거절 되었습니다.", Toast.LENGTH_SHORT).show();
                                break;
                            //문 열기 가능
                            case 1:
                                Toast.makeText(getContext(), "문이 닫혔습니다.", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        });


        restRequestHelper.getSmartKey(info, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                JsonObject data = jsonObject.getAsJsonObject("responseData");
                int id = data.getAsJsonPrimitive("key").getAsInt();

                if (id == -1)//키가 아예 존재하지 않을 경우
                {
                    textDate.setText("There is no valid key");
                    btnSmartKeyOpen.setEnabled(false);
                } else// 키가 있을 경우
                {
                    String stime = data.getAsJsonPrimitive("startTime").getAsString();
                    String etime = data.getAsJsonPrimitive("endTime").getAsString();
                    roomId = data.getAsJsonPrimitive("roomId").getAsInt();
                    roomName = RoomInfo.getRoomName(data.getAsJsonPrimitive("roomId").getAsInt());
                    String date = data.getAsJsonPrimitive("date").getAsString();

                    textDate.setText(date);
                    textStime.setText(stime);
                    textEtime.setText(etime);
                    textRoom.setText(roomName.replace("\"", ""));

                    if (id == 1)//현재 시간이 예약 시간 안에 겹칠 때
                        btnSmartKeyOpen.setEnabled(true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                btnSmartKeyOpen.setClickable(false);
                Log.e("KF", error.toString());
            }
        });

        return view;
    }

    public void init(View view){
        info = new TransmissionUserInfo("null");

        btnSmartKeyOpen = (Button)view.findViewById(R.id.button_smartkey_open);
        btnSmartKeyClose = (Button)view.findViewById(R.id.button_smartkey_close);

        textDate = (TextView)view.findViewById(R.id.text_key_date);
        textStime = (TextView)view.findViewById(R.id.text_key_stime);
        textEtime = (TextView)view.findViewById(R.id.text_key_etime);
        textRoom = (TextView)view.findViewById(R.id.text_key_room);
        roomName = null;

        btnSmartKeyOpen.setEnabled(false); //키는 비활성화

    }

}