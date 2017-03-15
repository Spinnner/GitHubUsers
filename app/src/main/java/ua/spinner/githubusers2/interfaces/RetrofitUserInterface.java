package ua.spinner.githubusers2.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ua.spinner.githubusers2.pojo.UserMoreInfo;

/**
 * Created by Spinner on 3/14/2017.
 */

public interface RetrofitUserInterface {

    @GET("/users/{user}")
    Call<UserMoreInfo> getUser(@Path("user") String user);
}
