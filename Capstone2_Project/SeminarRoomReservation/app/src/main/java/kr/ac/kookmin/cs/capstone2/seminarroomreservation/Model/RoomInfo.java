package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ehye on 2015-09-24.
 */
public class RoomInfo {

    // key : roomId
    public static HashMap<Integer , String> roomNames = new HashMap<Integer , String>();
    public static HashMap<String, Integer> roomIds = new HashMap<String, Integer>();
    public RoomInfo(){

    }
    public static void setRoomInfo(Integer key, String value){
        roomNames.put(key, value);
    }
    public static void setRoomIfo(String key, Integer value) { roomIds.put(key, value); }

    public static String getRoomName(Integer key){
        return roomNames.get(key);
    }
    public static int getRoomId(String key) { return roomIds.get(key); }


    public static int roomNamesSize(){
        return roomNames.size();
    }

    public static ArrayList<String> getRoomNames(){
        ArrayList<String> roomNames = new ArrayList<String>();
        Iterator<Integer> iterator = RoomInfo.roomNames.keySet().iterator();
        while (iterator.hasNext()) {
            int key = (Integer) iterator.next();
            roomNames.add(getRoomName(key));      // 참가자 고유 ID값을 얻음
        /*   System.out.print("key=" + key);
            System.out.println(" value=" + selectedUsers.get(key));  */
        }
        return roomNames;
    }
}
