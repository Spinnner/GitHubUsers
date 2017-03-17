package ua.spinner.githubusers2;

import android.app.Application;
import ua.spinner.githubusers2.dagger2.DaggerNetworkComponent;
import ua.spinner.githubusers2.dagger2.NetworkComponent;
import ua.spinner.githubusers2.dagger2.NetworksModule;


/**
 * Created by Spinner on 3/17/2017.
 */

public class CustomApplication extends Application {

    private NetworkComponent networkComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        networkComponent = DaggerNetworkComponent.builder()
                .networksModule(new NetworksModule(Constants.URL_ROOT))
                .build();
    }

    public NetworkComponent getNetworkComponent(){
        return networkComponent;
    }

}
