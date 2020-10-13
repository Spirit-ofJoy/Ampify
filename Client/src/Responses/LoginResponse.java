package Responses;

import java.io.Serializable;

//Response of login request
public class LoginResponse extends Response implements Serializable {

    private String userID;
    private String History;
    private String Liked;
    private String Disliked;
    private String Playlists;


    public LoginResponse(String id) {              //Login serialized response if User not found
        this.respType = "LOGIN_CHECK";
        this.userID = id;
    }

    public LoginResponse(String id, String a, String b, String c, String d){      //Loads back profile if user found
        this.respType = "LOGIN_CHECK";
        this.userID = id;
        this.History = a;
        this.Liked = b;
        this.Disliked = c;
        this.Playlists = d;
    }

    @Override
    public String getRespType() {
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

