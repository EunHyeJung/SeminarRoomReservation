package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network;

import com.squareup.okhttp.Call;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by ehye on 2015-09-08.
 */
public class RestRequestHelper {

    private static RestRequestHelper instance;

    private RestAdapter restAdapter;
    private RestRequest restRequest;


    public static RestRequestHelper newInstance(){
        if(instance == null){
            instance = new RestRequestHelper();
        }
        return instance;
    }

    public RestRequestHelper(){
        restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://192.168.23.109:8081/smartdoorlock").build();
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
        @POST("/doorcontrol")
        void controlDoor(@Field("id") String id,
                         @Field("doorName") String doorName,
                         @Field("status") boolean status,
                         Callback<Integer> controlDoorCallback);
    }
    public void signUp(String id, String password, String name, String phone, Callback<Integer> signUpCallback){
        restRequest.signUp(id, password, name, phone, signUpCallback);
    }

    public void controlDoor(String id, String doorName, boolean status, Callback<Integer> controlDoorCallback){
        restRequest.controlDoor(id,doorName,status, controlDoorCallback);
    }
}
