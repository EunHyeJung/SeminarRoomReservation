package kr.ac.kookmin.cs.capstone2.seminarroomreservation.User;

import android.app.Activity;
import android.content.Context;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.SharedPreferenceClass;

/**
 * Created by eunhye on 2015-10-05.
 */
public class UserInfo {
    private static int id;
    private static String userId;
    private static String password;


    public static void setUserInfo(int _id){
        id = _id;
        SharedPreferenceClass.putValue("id", id);
    }

    public static void setUserInfo(String _userId, String _password) {
        userId = _userId;
        password = _password;
        SharedPreferenceClass.putValue("id", userId);          // 사용자 정보 저장
        SharedPreferenceClass.putValue("password", password);
    }


    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

}
