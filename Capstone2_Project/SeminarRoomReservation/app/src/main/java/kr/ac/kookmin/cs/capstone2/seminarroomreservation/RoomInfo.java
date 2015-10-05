package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

/**
 * Created by ehye on 2015-09-24.
 */
public class RoomInfo {
    public static String roomNames[];

    public static void setRoomNames(int numOfRooms){
        roomNames = new String[numOfRooms];
    }


    public static String[] getRoomNames(){
        return roomNames;
    }

    // 서버와의 통신  확인후 삭제 할것
    public static void printRoomNames(){
        for(int i=0 ; i<roomNames.length ; i++){
            System.out.println("roomName : "+roomNames[i]);
        }
    }
}
