package Responses;

import constants.Constant;

public class SignUpResponse extends Response{

    private String status;

    public SignUpResponse(String finalStatus) {
        this.respType = (Constant.SIGNUP_START);
        this.status = finalStatus;
    }

    @Override
    public Constant getRespType() {
        return this.respType;
    }

    public String getStatus() {
        return status;
    }
}
