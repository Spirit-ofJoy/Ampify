package utility;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private String groupName;
    private String groupId;
    private ArrayList<String> groupMembers;
    public Playlist groupPlaylist;

    public Group(String grpName, String grpId, ArrayList<String> grpMembers, Playlist grpPlaylist) {
        this.groupName = grpName;
        this.groupId = grpId;
        this.groupMembers = grpMembers;
        this.groupPlaylist = grpPlaylist;
    }

    public String getGroupId() {
        return groupId;
    }

    public ArrayList<String> getGroupMembers() {
        return groupMembers;
    }

    public String getGroupName() {
        return groupName;
    }
}
