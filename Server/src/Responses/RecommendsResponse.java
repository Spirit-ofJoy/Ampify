package Responses;

import constants.Constant;
import utility.SongInfo;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

public class RecommendsResponse extends Response implements Serializable {

    public ArrayList recommendations = new ArrayList<SongInfo>();

    public RecommendsResponse(ArrayList<SongInfo> recommends) throws SQLException {
        this.respType = (Constant.PERSONAL_RECOMMENDS);

        this.recommendations = recommends;
    }

    @Override
    public Constant getRespType() {
        return respType;
    }
}
