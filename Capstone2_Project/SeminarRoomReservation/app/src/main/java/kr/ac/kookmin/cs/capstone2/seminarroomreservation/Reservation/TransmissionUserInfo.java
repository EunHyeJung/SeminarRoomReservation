package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import com.google.gson.JsonObject;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.UserInfo;

/**
 * Created by song on 2015-10-26.
 */
public class TransmissionUserInfo {
    public int Id; //사용자 id
    public int mode; //사용자 mode
    public String date; // 날짜

    //생성자
    public TransmissionUserInfo(int id, String date){
        mode = UserInfo.getUserMode();
        this.Id = id;
        this.date = date;
    }
}
