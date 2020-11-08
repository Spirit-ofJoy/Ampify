package Requests;

import constants.Constant;

import java.io.Serializable;

public class LoginRequest extends Request implements Serializable {
    private String username;
    private String password;

    public LoginRequest(String uname, String  pass){
        this.reqType = (Constant.LOGIN_CHECK);
        this.username = uname;
        this.password = pass;
    }

    @Override
    public Constant getReqType() {
        return this.reqType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
