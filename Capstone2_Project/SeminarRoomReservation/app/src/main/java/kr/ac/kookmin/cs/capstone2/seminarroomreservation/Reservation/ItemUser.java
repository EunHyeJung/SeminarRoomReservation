package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

/**
 * Created by eunhye on 2015-11-08.
 */
public class ItemUser {
    int id;
    String userId;
    String userName;
    boolean isChecked;

    public ItemUser(int id, String userId, String userName){
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.isChecked = false;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setUserName(String name){
        this.userName = name;
    }
    public void setChecked(boolean isChecked) { this.isChecked = isChecked; }

    public int getId(){
        return id;
    }
    public String getUserId(){
        return userId;
    }
    public String getUserName(){
        return userName;
    }
    public boolean getIsChecked() { return isChecked;}

    public void printUserInfo(){
        System.out.println("테스트/ id : "+id+", userId : "+userId+", userName : "+userName);
    }
}
