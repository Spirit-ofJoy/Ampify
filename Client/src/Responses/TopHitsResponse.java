package Responses;

import constants.Constant;
import utility.SongInfo;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

//Response of Top Hits request
public class TopHitsResponse extends Response implements Serializable {

    public ArrayList topHitsPlaylist = new ArrayList<SongInfo>();

    public TopHitsResponse(ArrayList topHits) throws SQLException {
        respType = (Constant.TOP_HITS_LIST);

        //Store relevant details in an array list for display
        this.topHitsPlaylist = topHits;
    }

    @Override
    public Constant getRespType() {
        return this.respType;
    }

}
