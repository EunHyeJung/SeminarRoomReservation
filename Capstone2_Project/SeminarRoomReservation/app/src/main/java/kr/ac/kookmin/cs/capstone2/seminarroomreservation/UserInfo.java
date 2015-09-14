package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

/**
 * Created by ehye on 2015-09-14.
 */
public class UserInfo {
    private String id;
    private String password;
    private String name;
    private String phone;

    public UserInfo(String id, String password, String name, String phone){
        this.id  = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public void setId(String id){
        this.id= id;
    }
    public String getId(){
        return id;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassworkd(){
       return password;
    }
       public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getPhone(){
        return phone;
    }

}
