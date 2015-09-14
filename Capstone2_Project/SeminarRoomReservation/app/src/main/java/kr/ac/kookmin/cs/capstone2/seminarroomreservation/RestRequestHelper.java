package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

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


    public static RestRequestHelper newInstance(String addr){
        if(instance == null){
            instance = new RestRequestHelper(addr);
        }
        return instance;
    }

    public RestRequestHelper(String addr){
        restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://192.168.1.101:8081/"+addr).build();
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
        @POST("/doorcontrol")
        void controlDoor(@Field("id") String id,
                         @Field("doorName") String doorName,
                         @Field("status") boolean status);
    }
    public void signUp(String id, String password, String name, String phone, Callback<Integer> signUpCallback){
        restRequest.signUp(id, password, name, phone, signUpCallback);
    }

    public void login(String id, String password,  Callback<Integer> loginCallback){
        restRequest.login(id, password, loginCallback);
    }

    public void controlDoor(String id, String doorName, boolean status ){
        restRequest.controlDoor(id,doorName,status);
    }
}
