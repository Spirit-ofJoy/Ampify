package Requests;

import java.io.Serializable;

//Requests to give personalized recommendations
public class RecommendsRequest extends Request implements Serializable {

    private String userID;
    int i=0;

    public RecommendsRequest(String a) {
        this.reqType = "PERSONAL_RECOMMENDS";
        this.userID = a;
    }

    @Override
    public String getReqType() {
        return reqType;
    }

    public String getUserID() {
        return userID;
    }
}
