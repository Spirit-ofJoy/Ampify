package Requests;

import java.io.Serializable;

public class SongSearchRequest extends Request implements Serializable {
    private String searchType;
    private String searchKey;

    public SongSearchRequest(String type, String key) {
        this.reqType = "SONG_SEARCH";
        this.searchType = type;
        this.searchKey = key;
    }

    @Override
    public String getReqType() {
        return reqType;
    }

    public String getSearchType() {
        return searchType;
    }

    public String getSearchKey() {
        return searchKey;
    }
}
