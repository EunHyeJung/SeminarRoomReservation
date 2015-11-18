package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

/**
 * 예약 정보 : 세미나실 사용 시작 시간, 사용 종료 시간
 */
public class ReservationsInfo {
    int startTime;
    int endTime;
    int reservationStatus;
    int reservationId;

    public void setReservationsInfo(int reservationId, int reservationStatus, int startTime, int endTime){
        this.reservationId = reservationId;
        this.reservationStatus = reservationStatus;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getStartTime(){
        return startTime;
    }
    public int getEndTime(){
        return endTime;
    }
    public int getReservationStatus(){
        return reservationStatus;
    }
    public int getReservationId() { return reservationId; }
}
