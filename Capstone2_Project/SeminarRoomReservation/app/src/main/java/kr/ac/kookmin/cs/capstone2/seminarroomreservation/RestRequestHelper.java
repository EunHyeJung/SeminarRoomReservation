package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

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
                .setEndpoint("http://192.168.1.101:8081/smartdoorlock").build();
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
    }
    public void signUp(String id, String password, String name, String phone, Callback<Integer> signUpCallback){
        restRequest.signUp(id, password, name, phone, signUpCallback);
    }


}
