package Requests;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

//Requests to give personalized recommendations
public class RecommendsRequest extends Request implements Serializable {

    private String userID;
    //public ArrayList history = new ArrayList<String>();
    //public ArrayList liked = new ArrayList<String>();
    int i=0;

    public RecommendsRequest(String a) {
        this.reqType = "RECOMMENDS_GIVEN";
        this.userID = a;
/*
        //Keeps track of last 3 watched or liked
        for(String temp : x){
            if(i==3){ break; }
            history.add(temp);
            i++;
        }

        i=0;
        for(String temp : y){
            if(i==3){ break; }
            liked.add(temp);
            i++;
        }

 */
    }

    @Override
    public String getReqType() {
        return reqType;
    }

    public String getUserID() {
        return userID;
    }
}
