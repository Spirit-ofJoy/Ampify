package Requests;

import constants.Constant;

import java.io.Serializable;
import java.util.ArrayList;

public class HistoryInfoRequest extends Request implements Serializable {

    public ArrayList<String> historySongID = new ArrayList<String>();

    public HistoryInfoRequest(ArrayList<String> history){
        this.reqType = String.valueOf(Constant.HISTORY_INFO);
        this.historySongID = history;
    }

    @Override
    public String getReqType() {
        return this.reqType;
    }
}
