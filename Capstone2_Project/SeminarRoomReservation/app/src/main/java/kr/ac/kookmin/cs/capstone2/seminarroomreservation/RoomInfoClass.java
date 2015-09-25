package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

/**
 * Created by ehye on 2015-09-24.
 */
public class RoomInfoClass {
    public static String roomNames[];

    public static void init(int numOfRooms){
        roomNames = new String[numOfRooms];
    }


    // 서버와의 통신  확인후 삭제 할것
    public static void printRoomNames(){
        for(int i=0 ; i<roomNames.length ; i++){
            System.out.println("roomName : "+roomNames[i]);
        }
    }
}
