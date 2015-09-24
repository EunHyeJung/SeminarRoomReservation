package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

/**
 * Created by ehye on 2015-09-24.
 */
public class RoomInfoClass {
    private static String[] roomNames;
    private static int numOfRooms;

    public RoomInfoClass(int numOfRooms){
        this.numOfRooms = numOfRooms;
        roomNames = new String[numOfRooms];
    }


    public static void setRoomNames(String[] roomNamesData){
        for(int i=0 ; i<roomNamesData.length ; i++)
            roomNames[i] =  roomNamesData[i];
    }
    public static String[] getRoomNames(){
        return roomNames;
    }
    public static int getNumOfRooms(){
        return numOfRooms;
    }

}
