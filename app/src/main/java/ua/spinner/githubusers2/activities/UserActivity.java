package ua.spinner.githubusers2.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.lang.annotation.Annotation;
import javax.inject.Inject;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import ua.spinner.githubusers2.Constants;
import ua.spinner.githubusers2.CustomApplication;
import ua.spinner.githubusers2.NetworkAPIError;
import ua.spinner.githubusers2.R;
import ua.spinner.githubusers2.Utils;
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

    @BindString(R.string.name) String titleName;
    @BindString(R.string.login) String titleLogin;
    @BindString(R.string.type) String titleType;
    @BindString(R.string.blog) String titleBlog;
    @BindString(R.string.location) String titleLocation;
    @BindString(R.string.email) String titleEmail;
    @BindString(R.string.followers) String titleFollowers;
    @BindString(R.string.following) String titleFollowing;

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
        final Converter<ResponseBody, NetworkAPIError> errorConverter
                = retrofit.responseBodyConverter(NetworkAPIError.class, new Annotation[0]);

        Call<UserMoreInfo> call = service.getUser(login);

        call.enqueue(new Callback<UserMoreInfo>() {
            @Override
            public void onResponse(Call<UserMoreInfo> call, Response<UserMoreInfo> response) {
                progressBar.setVisibility(View.GONE);

                if(response.isSuccessful()) {
                    UserMoreInfo user = response.body();

                    String login = Utils.replaceNull(user.getLogin());
                    String name = Utils.replaceNull(user.getName());
                    String type = Utils.replaceNull(user.getType());
                    String blog = Utils.replaceNull(user.getBlog());
                    String avatar = Utils.replaceNull(user.getAvatarUrl());
                    String location = Utils.replaceNull(user.getLocation());
                    String email = Utils.replaceNull(user.getEmail());
                    String followers = Utils.replaceNull(user.getFollowers().toString());
                    String following = Utils.replaceNull(user.getFollowing().toString());

                    Picasso.with(getApplicationContext()).load(avatar).placeholder(R.drawable.profile_photo).into(ivAvatar);
                    tvLogin.setText(titleLogin.concat(" ").concat(login));
                    tvName.setText(titleName.concat(" ").concat(name));
                    tvType.setText(titleType.concat(" ").concat(type));
                    tvBlog.setText(titleBlog.concat(" ").concat(blog));
                    tvLocation.setText(titleLocation.concat(" ").concat(location));
                    tvEmail.setText(titleEmail.concat(" ").concat(email));
                    tvFollowers.setText(titleFollowers.concat(" ").concat(followers));
                    tvFollowing.setText(titleFollowing.concat(" ").concat(following));
                }
                else{
                    NetworkAPIError error = Utils.parseError(errorConverter, response);
                    Log.e(Constants.TAG_ERROR, error.message());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();
                Log.e(Constants.TAG_ON_FAILURE, t.getMessage());
            }

        });
    }
}
