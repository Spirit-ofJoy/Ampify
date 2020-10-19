package Responses;

import main.SongInfo;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

public class RecommendsResponse extends Response implements Serializable {

    public ArrayList recommendations = new ArrayList<SongInfo>();

    public RecommendsResponse(ArrayList<SongInfo> recommends) throws SQLException {
        this.respType = "RECOMMENDS_GIVEN";

        this.recommendations = recommends;
    }

    @Override
    public String getRespType() {
        return respType;
    }
}
