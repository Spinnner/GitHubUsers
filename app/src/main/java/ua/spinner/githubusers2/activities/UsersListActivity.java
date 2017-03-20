package ua.spinner.githubusers2.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
import ua.spinner.githubusers2.interfaces.RetrofitListUsersInterface;
import ua.spinner.githubusers2.Utils;
import ua.spinner.githubusers2.adapters.RecyclerViewUsersAdapter;
import ua.spinner.githubusers2.pojo.User;

public class UsersListActivity extends AppCompatActivity {

    @BindView(R.id.swipeLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerViewUsers) RecyclerView rvUsers;
    @BindView(R.id.buttonConnect) Button btnConnect;

    @Inject Retrofit retrofit;
    private Converter<ResponseBody, NetworkAPIError> errorConverter;
    private RetrofitListUsersInterface service;
    private RecyclerViewUsersAdapter rvAdapter;

    private List<User> listUsers = new ArrayList<User>();
    private LinearLayoutManager llm;
    private int lastUserID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        ButterKnife.bind(this);
        ((CustomApplication)getApplication()).getNetworkComponent().inject(this);

        initViews();
        initRetrofit();
        checkInternet();
        setOnScrollListener();
    }

    private void initViews(){
        llm = new LinearLayoutManager(this);
        rvUsers.setLayoutManager(llm);
        rvAdapter = new RecyclerViewUsersAdapter(this, listUsers);
        rvUsers.setAdapter(rvAdapter);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setEnabled(false);
    }

    private void checkInternet(){
        if(!Utils.isNetworkAvailable(this)){
            btnConnect.setVisibility(View.VISIBLE);
            Toast.makeText(this, R.string.network_offline, Toast.LENGTH_LONG).show();
        }
        else{
            btnConnect.setVisibility(View.GONE);
            getUsers();
        }
    }

    @OnClick(R.id.buttonConnect)
    public void onConnectClicked(){
        checkInternet();
    }


    private void setOnScrollListener(){
        rvUsers.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItem = llm.findLastVisibleItemPosition();
                if(lastVisibleItem == listUsers.size() - 1 && dy > 0 && !swipeRefreshLayout.isRefreshing()){
                    getUsers();
                }
            }
        });
    }

    private void initRetrofit(){
        service = retrofit.create(RetrofitListUsersInterface.class);
        errorConverter = retrofit.responseBodyConverter(NetworkAPIError.class, new Annotation[0]);
    }

    private void getUsers() {
        swipeRefreshLayout.setRefreshing(true);

        Call<List<User>> call = service.getUsers(lastUserID, Constants.ITEMS_PER_PAGE);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){

                    for (User user: response.body()) {
                        lastUserID = user.getId();
                        listUsers.add(user);
                    }

                    rvAdapter.notifyItemRangeInserted(listUsers.size(), response.body().size());
                    swipeRefreshLayout.setRefreshing(false);
                }
                else{
                    NetworkAPIError error = Utils.parseError(errorConverter, response);
                    Log.e(Constants.TAG_ERROR, error.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                call.cancel();
                Log.e(Constants.TAG_ON_FAILURE, t.getMessage());
            }

        });
    }
}