package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Authentication;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Join.TransmissionJoinInfo;

/**
 * Created by eunhye on 2015-11-22.
 */
public class TransmissionLoginInfo {
    private String id;
    private String password;
    private String instanceId;

    public TransmissionLoginInfo(String id, String password, String instanceId){
        this.id = id;
        this.password = password;
        this.instanceId = instanceId;
    }
}
