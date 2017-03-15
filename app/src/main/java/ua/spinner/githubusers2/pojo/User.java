package ua.spinner.githubusers2.pojo;

/**
 * Created by Spinner on 3/14/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("login")
    @Expose
    private String login;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("avatar_url")
    @Expose
    private String avatar;

    @SerializedName("type")
    @Expose
    private String type;

    public User (int id, String avatar, String login, String type){
        this.id = id;
        this.avatar = avatar;
        this.login = login;
        this.type = type;
    }

    public String getLogin() {
        return login;
    }

    public Integer getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getType() {
        return type;
    }
}