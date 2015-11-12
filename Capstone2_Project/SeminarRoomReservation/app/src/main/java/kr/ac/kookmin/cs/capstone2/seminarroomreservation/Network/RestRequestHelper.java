package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network;

import com.google.gson.JsonObject;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.TransmissionData;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.TransmissionResInfo;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.TransmissionReservation;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.TransmissionUserInfo;
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


    private static final String url = "http://10.30.113.215:8081/smartdoorlock";

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


        @POST("/bookingrequest")
        void makeReservation(@Body TransmissionResInfo transmissionResInfo,
                             Callback<Integer> makeReservationCallback);




        @FormUrlEncoded
        @POST("/login")
        void login(@Field("id") String id,
                   @Field("password") String password,
                   @Field("instanceId") String instanceId,
                   Callback<JsonObject> loginCallback
        );


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

     /*   @FormUrlEncoded
        @POST("/requestlist")
        void requestList(@Field("date") String date ,
                         Callback<JsonObject> requestListCallback);*/

        @POST("/requestlist")
        void requestList(@Field("info") TransmissionUserInfo info,
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
                      Callback<JsonObject> userlistCallback);

        //////////////////////////////////////////
        @FormUrlEncoded
        @POST("/userlist")
        void getUserList(@Field("id") int id,
                         @Field("name") String userName,
                         @Field("userId") String userId,
                         Callback<JsonObject> userListCallback
        );

        @POST("/cancelmybooking")
        void cancelBooking(@Body TransmissionReservation info,
                           Callback<JsonObject> cancelBookingCallback);

        @FormUrlEncoded
        @POST("/Reservationcheck")
        void checkReservation(@Field("userId") int userId,
                              @Field("userName") String userName,
                              @Field("date") String date,
                              @Field("startTime") String startTime,
                              @Field("endTime") String endTime,
                              @Field("context") String context,
                              @Field("participants") String participants,
                              Callback<JsonObject> checkReservationCallback
        );

    }

    // 회원가입시 호출
    public void signUp(String id, String password, String name, String phone, Callback<Integer> signUpCallback){
        restRequest.signUp(id, password, name, phone, signUpCallback);
    }

    // 로그인 시 호출
    public void login(String id, String password, String instanceId,  Callback<JsonObject> loginCallback){
        restRequest.login(id, password, instanceId, loginCallback);
    }

    // 예약하기
    public void makeReservation(TransmissionResInfo transmissionResInfo, Callback<Integer> makeReservationCallback){
        restRequest.makeReservation(transmissionResInfo, makeReservationCallback);
    }


    // 예약 현황 호출 시, 서버로 날짜를 전송 후 날짜에 해당하는 예약 내역 정보를 받아옴
    public void receiveUsingStatue(TransmissionData transmissionData, Callback<JsonObject> usingStatusCallback){
        restRequest.receiveUsingStatus(transmissionData, usingStatusCallback);
    }

    public void roomList(String id,Callback<String> roomListCallback){
        restRequest.roomList(id, roomListCallback);
    }

    public void roomStatus(int id, String roomName, Callback<JsonObject> roomStatusCallback){
        restRequest.roomStatus(id, roomName, roomStatusCallback);
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

    public void getUserList(String date, Callback<JsonObject> userlistCallback){
        restRequest.dayWatch(date, userlistCallback);
    }
    public void cancelBooking(TransmissionReservation info, Callback<JsonObject> cancelBookingCallback){
        restRequest.cancelBooking(info, cancelBookingCallback);
    }
    /////////////////////////////
    public void getUserList(int id, String userName, String userId, Callback<JsonObject> userListCallback){
        restRequest.getUserList(id, userName, userId, userListCallback);
    }
    public void checkReservation(int userId, String userName, String date, String startTime, String endTime, String context, String participants,  Callback<JsonObject> checkReservationCallback){
        restRequest.checkReservation(userId, userName, date, startTime, endTime, context, participants, checkReservationCallback);
    }
}