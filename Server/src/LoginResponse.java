import java.io.Serializable;

public class LoginResponse extends Response implements Serializable {

    private String userID;

    public LoginResponse(String id){
        this.respType = "LOGIN_CHECK";
        this.userID = id;
    }

    @Override
    public String getRespType() {
        return this.respType;
    }

    public String getUserID() {
        return userID;
    }
}
