package Responses;

import main.SongInfo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//Response of Top Hits request
public class TopHitsResponse extends Response implements Serializable {

    public ArrayList topHitsPlaylist = new ArrayList<SongInfo>();

    public TopHitsResponse(ResultSet r) throws SQLException {
        respType = "TOP_HITS_LIST";

        //Store relevant details in an array list for display
        SongInfo temp;
        while (r.next()){
            temp = new SongInfo(r.getString("SONG_ID"), r.getString("Name"),
                    r.getString("Genre"), r.getString("Language"), r.getString("Artist_Name"),
                    r.getString("Album_Name"), r.getString("Upload_time"));

            topHitsPlaylist.add(temp);
        }
    }

    @Override
    public String getRespType() {
        return this.respType;
    }

}
