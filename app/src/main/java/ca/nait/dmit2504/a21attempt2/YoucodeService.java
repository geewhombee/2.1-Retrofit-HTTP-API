package ca.nait.dmit2504.a21attempt2;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface YoucodeService {
    @GET("JitterServlet")   //  response body format:	[date] from [sender] *** [message]
    Call<String> listJitterServlet();

    @GET("JSONServlet")     //  response body format: 	sender\n\message\n\date
    Call<String> listJSONServlet();

    @FormUrlEncoded
    @POST("JitterServlet")
    Call<String> postJitter(@Field("LOGIN_NAME") String loginName, @Field("DATA") String data);
}
