package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import java.util.ArrayList;

/**
 * Created by eunhye on 2015-11-11.
 */

public class TransmissionResInfo {

    public int roomId;
    public int userId;
    public String date;
    public String startTime;
    public String endTime;
    public String context;
    public ArrayList<Participants> participants;

    public TransmissionResInfo(int roomId, int userId, String date,
                               String startTime, String endTime, String context, ArrayList<Integer> tempParticipants){
        this.userId = userId;
        this.roomId = roomId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.context = context;
        this.participants = new ArrayList<Participants>();
        for(int i=0 ; i<tempParticipants.size() ; i++){
            this.participants.add(new Participants(tempParticipants.get(i)));
        }

    }

    public class Participants{
        public int id;
        public Participants(int ids){
            id = ids;
        }

    }

}
