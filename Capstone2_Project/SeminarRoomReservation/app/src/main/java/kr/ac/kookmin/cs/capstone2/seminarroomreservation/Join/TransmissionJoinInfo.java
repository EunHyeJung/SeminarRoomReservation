package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Join;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Encryption.EncryptionClass;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.TransmissionResInfo;

/**
 * Created by eunhye on 2015-11-22.
 */
public class TransmissionJoinInfo {
    private String id;
    private String password;
    private String name;
    private String phone;

    public TransmissionJoinInfo(String id, String password, String name, String phone){
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

}
