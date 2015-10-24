package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by eunhye on 2015-10-13.
 */
public class TransmissionData {
    // Transmission
    public String date;
    public ArrayList<RoomIds> roomIds;
    public TransmissionData(String date, int[] roomIds) {
        this.date = date;
        this.roomIds = new ArrayList<RoomIds>();
        for(int i=0 ; i<roomIds.length ; i++){
            this.roomIds.add(new RoomIds(roomIds[i]));
        }
      //  this.roomIds.add(new RoomIds(roomIds));
    }

    public class RoomIds{
        public int roomId;
        public RoomIds(int roomIds){
            roomId = roomIds;
        }

    }
}
