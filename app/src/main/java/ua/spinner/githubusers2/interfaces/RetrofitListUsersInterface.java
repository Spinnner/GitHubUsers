package ua.spinner.githubusers2.interfaces;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import ua.spinner.githubusers2.pojo.User;

/**
 * Created by Spinner on 3/14/2017.
 */

public interface RetrofitListUsersInterface {

    /*
   * Retrofit get annotation with our URL
   * And our method that will return us details of student.
  */
    @Headers("User-Agent: GitHubUsers2")
    @GET("/users")
    Call<List<User>> getUsers(@Query("since") int id, @Query("per_page") int per_Page);

}
