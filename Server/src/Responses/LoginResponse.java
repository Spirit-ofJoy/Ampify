package Responses;

import constants.Constant;

import java.io.Serializable;

//Response of login request
public class LoginResponse extends Response implements Serializable {

    private String userID;
    private String History;
    private String Liked;
    private String Disliked;
    private String Playlists;


    public LoginResponse(String id) {              //Login serialized response if User not found
        this.respType = (Constant.LOGIN_CHECK);
        this.userID = id;
    }

    public LoginResponse(String id, String history, String liked, String disliked, String playlists){      //Login serialized response loads back profile if user found
        this.respType = (Constant.LOGIN_CHECK);
        this.userID = id;
        this.History = history;
        this.Liked = liked;
        this.Disliked = disliked;
        this.Playlists = playlists;
    }

    @Override
    public Constant getRespType() {
        return this.respType;
    }

    public String getUserID() {
        return userID;
    }

    public String getHistory() {
        return History;
    }

    public String getLiked() {
        return Liked;
    }

    public String getDisliked() {
        return Disliked;
    }

    public String getPlaylists() {
        return Playlists;
    }
}
