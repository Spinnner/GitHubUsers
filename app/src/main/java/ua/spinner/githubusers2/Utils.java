package ua.spinner.githubusers2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Spinner on 3/11/2017.
 */

public class Utils {

    public static boolean isNetworkAvailable(Context context)
    {
        boolean status = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);

            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);

                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    status = true;
                } else {
                    status = false;
                }
            }

        } catch (Exception e) {
            Log.e(Constants.TAG_ERROR, e.getMessage());
            return false;
        }

        return status;
    }

    public static NetworkAPIError parseError(Converter<ResponseBody, NetworkAPIError> errorConverter, Response<?> response) {
        NetworkAPIError error;
        try {
            error = errorConverter.convert(response.errorBody());
        } catch (IOException e) {
            return new NetworkAPIError();
        }

        return error;
    }

    public static String replaceNull(String str) {
        return str == null ? "" : str;
    }

}
