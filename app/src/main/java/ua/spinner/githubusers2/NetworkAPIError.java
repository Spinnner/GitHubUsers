package ua.spinner.githubusers2;

/**
 * Created by Spinner on 3/20/2017.
 */

public class NetworkAPIError {

    private String message;
    private String documentation_url;

    public NetworkAPIError() {

    }

    public String message() {
        return message;
    }

    public String getDocumentation_url(){
        return documentation_url;
    }
}
