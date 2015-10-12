package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

import java.util.HashMap;

/**
 * Created by ehye on 2015-09-24.
 */
public class RoomInfo {
    public static HashMap<Integer , String> roomNames = new HashMap<Integer , String>();

    public RoomInfo(){

    }

    public static void setRoomInfo(Integer key, String value){
        roomNames.put(key, value);
    }

    public static String getRoomName(Integer key){
        return roomNames.get(key);
    }



}
