package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network;

import org.json.JSONObject;

import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by ehye on 2015-09-14.
 */
public class RestRequestHelper {

    private static RestRequestHelper instance;

    private RestAdapter restAdapter;
    private RestRequest restRequest;

    private static final String url = "http://10.30.113.181:8081/smartdoorlock";


    public static RestRequestHelper newInstance(){
        if(instance == null){
            instance = new RestRequestHelper();
        }
        return instance;
    }

    public RestRequestHelper(){
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(url).build();
        restRequest = restAdapter.create(RestRequest.class);

    }

    public interface RestRequest{
        @FormUrlEncoded
        @POST("/signup")
        void signUp(@Field("id") String id,
                    @Field("password") String password,
                    @Field("name") String name,
                    @Field("phone") String phone,
                    Callback<Integer> signUpCallback);

        @FormUrlEncoded
        @POST("/login")
        void login(@Field("id") String id,
                   @Field("password") String password,
                   Callback<Integer> loginCallback
        );

        @FormUrlEncoded
        @POST("/usingstatus")
        void receiveUsingStatus(@Field("date")Date date,
                                Callback<JSONObject> usingStatusCallback
        );

        @FormUrlEncoded
        @POST("/roomstatus")
        void roomStatus(@Field("roomName")String roomName,
                        Callback<Integer> roomStatusCallback);

        @FormUrlEncoded
        @POST("/doorcontrol")
        void controlDoor(@Field("id") String id,
                         @Field("doorName") String doorName,
                         @Field("status") boolean status,
                         Callback<Integer> doorControllCallback
                         );

        @FormUrlEncoded
        @POST("/room")
        void roomList(@Field("id") String id,
                Callback<String> roomListCallback);


    }
    public void signUp(String id, String password, String name, String phone, Callback<Integer> signUpCallback){
        restRequest.signUp(id, password, name, phone, signUpCallback);
    }

    public void login(String id, String password,  Callback<Integer> loginCallback){
        restRequest.login(id, password, loginCallback);
    }

    public void receiveUsingStatue(Date date, Callback<JSONObject> usingStatusCallback){
        restRequest.receiveUsingStatus(date, usingStatusCallback);
    }

    public void roomList(String id,Callback<String> roomListCallback){
        restRequest.roomList(id, roomListCallback);
    }

    public void roomStatus(String roomName, Callback<Integer> roomStatusCallback){
        restRequest.roomStatus(roomName, roomStatusCallback);
    }

    public void controlDoor(String id, String doorName, boolean status, Callback<Integer> controlDoorCallback ){
        restRequest.controlDoor(id,doorName,status, controlDoorCallback);
    }
}