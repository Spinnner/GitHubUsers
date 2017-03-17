package ua.spinner.githubusers2.dagger2;

import javax.inject.Singleton;
import dagger.Component;
import ua.spinner.githubusers2.activities.UserActivity;
import ua.spinner.githubusers2.activities.UsersListActivity;

/**
 * Created by Spinner on 3/17/2017.
 */

@Singleton
@Component(modules = {NetworksModule.class})
public interface NetworkComponent {

    public void inject(UsersListActivity usersListActivity);

    public void inject(UserActivity userActivity);
}
