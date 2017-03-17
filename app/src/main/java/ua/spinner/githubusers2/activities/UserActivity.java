package ua.spinner.githubusers2.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import ua.spinner.githubusers2.Constants;
import ua.spinner.githubusers2.CustomApplication;
import ua.spinner.githubusers2.R;
import ua.spinner.githubusers2.interfaces.RetrofitUserInterface;
import ua.spinner.githubusers2.pojo.UserMoreInfo;

/**
 * Created by Spinner on 3/14/2017.
 */

public class UserActivity extends AppCompatActivity {

    @BindView(R.id.ivAvatar) CircleImageView ivAvatar;
    @BindView(R.id.prBarLoading) ProgressBar progressBar;
    @BindView(R.id.tvLogin) TextView tvLogin;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvType) TextView tvType;
    @BindView(R.id.tvBlog) TextView tvBlog;
    @BindView(R.id.tvLocation) TextView tvLocation;
    @BindView(R.id.tvEmail) TextView tvEmail;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.tvFollowing) TextView tvFollowing;

    @Inject Retrofit retrofit;

    private String login = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ButterKnife.bind(this);
        ((CustomApplication)getApplication()).getNetworkComponent().inject(this);

        login = getIntent().getStringExtra(Constants.LOGIN);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(login);

        getUser(login);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getUser(String login) {
        RetrofitUserInterface service = retrofit.create(RetrofitUserInterface.class);

        Call<UserMoreInfo> call = service.getUser(login);

        call.enqueue(new Callback<UserMoreInfo>() {
            @Override
            public void onResponse(Call<UserMoreInfo> call, Response<UserMoreInfo> response) {
                try {
                    //Log.e("MESSAGES2: ", response.toString());
                    progressBar.setVisibility(View.GONE);

                    UserMoreInfo user = response.body();

                    String login = user.getLogin();
                    String name = user.getName();
                    String type = user.getType();
                    String blog = user.getBlog();
                    String avatar = user.getAvatarUrl();
                    String location = user.getLocation();
                    String email = user.getEmail();
                    String followers = user.getFollowers().toString();
                    String following = user.getFollowing().toString();

                    Picasso.with(getApplicationContext()).load(avatar).placeholder(R.drawable.profile_photo).into(ivAvatar);
                    tvLogin.setText("Login: " + login);
                    tvName.setText("Name: " + name);
                    tvType.setText("Type: " + type);
                    tvBlog.setText("Blog: " + blog);
                    tvLocation.setText("Location: " + location);
                    tvEmail.setText("Email: " + email);
                    tvFollowers.setText("Followers: " + followers);
                    tvFollowing.setText("Following: " + following);

                } catch (Exception e) {
                    Log.e("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();
                Log.e("onFailure", t.toString());
            }

        });
    }
}
