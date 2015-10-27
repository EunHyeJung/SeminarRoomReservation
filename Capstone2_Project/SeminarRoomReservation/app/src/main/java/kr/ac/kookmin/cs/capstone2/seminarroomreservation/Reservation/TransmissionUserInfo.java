package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import com.google.gson.JsonObject;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.UserInfo;

/**
 * Created by song on 2015-10-26.
 */
public class TransmissionUserInfo {
    public int Id;
    public String date;

    public TransmissionUserInfo(int id){
        this.Id = id;
        this.date = "ALL";
    }
}
