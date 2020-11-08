package Responses;

import constants.Constant;
import utility.SongInfo;

import java.io.Serializable;
import java.util.ArrayList;

public class BrowseResponse extends Response implements Serializable {

    public ArrayList<SongInfo> popCollection;
    public ArrayList<SongInfo> rnbCollection;
    public ArrayList<SongInfo> indieCollection;
    public ArrayList<SongInfo> rockCollection;
    public ArrayList<SongInfo> epopCollection;
    public ArrayList<SongInfo> hiphopCollection;
    public ArrayList<SongInfo> elecCollection;


    public BrowseResponse(ArrayList<SongInfo> popList, ArrayList<SongInfo> rnbList, ArrayList<SongInfo> indieList, ArrayList<SongInfo> rockList, ArrayList<SongInfo> epopList, ArrayList<SongInfo> hiphopList, ArrayList<SongInfo> elecList){
        this.respType = (Constant.SONG_BROWSE);
        this.popCollection = popList;
        this.elecCollection = elecList;
        this.epopCollection = epopList;
        this.hiphopCollection = hiphopList;
        this.indieCollection = indieList;
        this.rockCollection = rockList;
        this.rnbCollection = rnbList;
    }

    @Override
    public Constant getRespType() {
        return respType;
    }
}
