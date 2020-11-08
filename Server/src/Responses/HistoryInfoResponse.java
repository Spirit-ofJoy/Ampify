package Responses;

import constants.Constant;

import java.io.Serializable;
import java.util.ArrayList;

public class HistoryInfoResponse extends Response implements Serializable {

    public ArrayList<String> historySongNames = new ArrayList<String>();

    public HistoryInfoResponse(ArrayList<String> queryList) {
        this.respType = (Constant.HISTORY_INFO);
        this.historySongNames = queryList;
    }

    @Override
    public Constant getRespType() {
        return this.respType;
    }
}
