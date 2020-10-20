package Responses;

public class SignUpResponse extends Response{

    private String status;

    public SignUpResponse(String finalStatus) {
        this.respType = "SIGNUP_START";
        this.status = finalStatus;
    }

    @Override
    public String getRespType() {
        return this.respType;
    }

    public String getStatus() {
        return status;
    }
}
