package Requests;

import java.io.Serializable;

public class LoginRequest extends Request implements Serializable {
    private String username;
    private String password;

    public LoginRequest(String uname, String  pass){
        this.reqType = "LOGIN_CHECK";
        this.username = uname;
        this.password = pass;
    }

    @Override
    public String getReqType() {
        return this.reqType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
