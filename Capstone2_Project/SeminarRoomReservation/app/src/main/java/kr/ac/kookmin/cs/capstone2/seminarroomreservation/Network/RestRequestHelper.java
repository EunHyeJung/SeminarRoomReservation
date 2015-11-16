package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network;

import com.google.gson.JsonObject;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.TransmissionData;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.TransmissionReservation;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.TransmissionUserInfo;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.UserInfo;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
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


    private static final String url = "http://10.30.113.107:8081/smartdoorlock";

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

        //jh
        @FormUrlEncoded
        @POST("/bookingrequest")
        void makeReservation(@Field("date") String date,
                             @Field("start_time") String start_time,
                             @Field("end_time") String end_time,
                             @Field("room_id") String room_id,
                             @Field("context") String context,
                             Callback<Integer> makeReservationCallback);



        @FormUrlEncoded
        @POST("/login")
        void login(@Field("id") String id,
                   @Field("password") String password,
                   @Field("instanceId") String instanceId,
                   Callback<JsonObject> loginCallback
        );

    //    @FormUrlEncoded
        @POST("/usingstatus")
        void receiveUsingStatus(@Body TransmissionData transmissionData,
                                Callback<JsonObject> usingStatusCallback
        );

        @FormUrlEncoded
        @POST("/roomstatus")
        void roomStatus(@Field("id") int id,
                        @Field("roomName")String roomName,
                        Callback<JsonObject> roomStatusCallback);

        @FormUrlEncoded
        @POST("/doorcontrol")
        void controlDoor(@Field("id") int id,
                         @Field("roomName") String roomName,
                         @Field("command") boolean status,
                         Callback<JsonObject> doorControllCallback
        );

        @FormUrlEncoded
        @POST("/room")
        void roomList(@Field("id") String id,
                      Callback<String> roomListCallback);

        @FormUrlEncoded
        @POST("/roomhistory")
        void dayWatch(@Field("date") String date,
                      @Field("room") String roomName,
                      Callback<JsonObject> dayWatchCallback
                      );


        @POST("/requestlist")
        void requestList(@Body TransmissionUserInfo info,
                         Callback<JsonObject> requestListCallback);

        //@FormUrlEncoded
        @POST("/mybooking")
        void myBooking(@Body TransmissionUserInfo info,
                       Callback<JsonObject> mybookingListCallback);

        @FormUrlEncoded
        @POST("/bookingfilter")
        void bookingFilter(@Field("id") int id,
                           @Field("command") int command,
                           Callback<JsonObject> bookingFilterCallback);

        @POST("/smartkey")
        void getSmartKey(@Body TransmissionUserInfo info,
                         Callback<JsonObject> getSmartKeyCallback);


        @FormUrlEncoded
        @POST("/userlist")
        void dayWatch(@Field("date") String date,
                      Callback<JsonObject> userlistCallback
        );

        @POST("/cancelmybooking")
        void cancelBooking(@Body TransmissionReservation info,
                            Callback<JsonObject> cancelBookingCallback);


    }
    public void signUp(String id, String password, String name, String phone, Callback<Integer> signUpCallback){
        restRequest.signUp(id, password, name, phone, signUpCallback);
    }

    // 로그인 시 호출
    public void login(String id, String password, String instanceId,  Callback<JsonObject> loginCallback){
        restRequest.login(id, password, instanceId, loginCallback);
    }

    public void makeReservation(String date, String start_time, String end_time ,String room_id,String context, Callback<Integer> makeReservationCallback){
        restRequest.makeReservation(date, start_time, end_time, room_id, context, makeReservationCallback);
    }


    public void receiveUsingStatue(TransmissionData transmissionData, Callback<JsonObject> usingStatusCallback){
        restRequest.receiveUsingStatus(transmissionData, usingStatusCallback);
    }


    public void controlDoor(int id, String doorName, boolean status, Callback<JsonObject> controlDoorCallback ){
        restRequest.controlDoor(id, doorName, status, controlDoorCallback);
    }

    public void getHistory(String date, String roomName, Callback<JsonObject> dayWatchCallback){
        restRequest.dayWatch(date, roomName, dayWatchCallback);
    }

    //관리자 권한인지 아닌지를 이 함수에서 받고, mybooking과 함께 처리
    //date, id
/*    public void requestList(String date, Callback<JsonObject> requestCallback){
        restRequest.requestList(date, requestCallback);
    }*/

    public void requestList(TransmissionUserInfo info,Callback<JsonObject> requestCallback){
        restRequest.requestList(info, requestCallback);
    }

    public void bookingFilter(int id, int command, Callback<JsonObject> bookingFilterCallback){
        restRequest.bookingFilter(id, command, bookingFilterCallback);
    }

    public void myBooking(TransmissionUserInfo info, Callback<JsonObject> mybookingListCallback){
        restRequest.myBooking(info, mybookingListCallback);
    }

    public void getSmartKey(TransmissionUserInfo info, Callback<JsonObject> getSmartKeyCallback){
        restRequest.getSmartKey(info, getSmartKeyCallback);
    }


    public void getUserList(String date, Callback<JsonObject> userlistCallback) {
        restRequest.dayWatch(date, userlistCallback);
    }

    public void cancelBooking(TransmissionReservation info, Callback<JsonObject> cancelBookingCallback){
        restRequest.cancelBooking(info, cancelBookingCallback);

    }
}
