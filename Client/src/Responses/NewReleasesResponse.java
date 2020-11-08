package Responses;

import constants.Constant;
import utility.SongInfo;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

//Response of New Release request
public class NewReleasesResponse extends Response implements Serializable {

    public ArrayList newSongsList = new ArrayList<SongInfo>();

    public NewReleasesResponse(ArrayList newRelease) throws SQLException {
        respType = (Constant.NEW_RELEASES_LIST);

        //Store relevant details in an array list for display
        this.newSongsList = newRelease;
    }

    @Override
    public Constant getRespType() {
        return this.respType;
    }

}
