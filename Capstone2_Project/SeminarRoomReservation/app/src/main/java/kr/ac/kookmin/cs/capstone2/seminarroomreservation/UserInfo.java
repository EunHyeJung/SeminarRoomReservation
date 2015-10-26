package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

import android.app.Activity;
import android.content.Context;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.SharedPreferenceClass;

/**
 * Created by eunhye on 2015-10-05.
 */
public class UserInfo {
    private static int id;
    private static int userMode;
    private static String userId;
    private static String password;


    public static void setUserInfo(int _id, int _userMode){
        id = _id;
        userMode = _userMode;
        SharedPreferenceClass.putValue("_id", id);
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

    public static String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public static int getUserMode() { return userMode; }

}
