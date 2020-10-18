package Responses;

import Responses.Response;
import main.SongInfo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RecommendsResponse extends Response implements Serializable {

    public ArrayList recommendations = new ArrayList<SongInfo>();

    public RecommendsResponse(ResultSet r) throws SQLException {
        this.respType = "RECOMMENDS_GIVEN";

        SongInfo temp;
        while (r.next()){
            temp = new SongInfo(r.getString("SONG_ID"), r.getString("Name"),
                    r.getString("Genre"), r.getString("Language"), r.getString("Artist_Name"),
                    r.getString("Album_Name"), r.getString("Upload_time"));

            recommendations.add(temp);
        }
    }

    @Override
    public String getRespType() {
        return respType;
    }
}
