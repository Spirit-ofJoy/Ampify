package utility;

import java.util.ArrayList;
import java.util.TreeSet;

//Class to store user profile details
public class ActiveProfile {

    private String username;
    private String userID;

    //Relevant collection of ids are stored in TreeSets
    public ArrayList<String> History = new ArrayList<String>();
    public TreeSet<String> Liked = new TreeSet<String>();
    public ArrayList<String> Playlists = new ArrayList<String>();

    //Singleton Object
    private static ActiveProfile activeProfile = null;

    //Constructor call
    private ActiveProfile(String uname, String uid, String hist, String like, String playlist){
        userID = uid;
        username = uname;

        if(hist !=null){
            for (String temp: hist.split("-")){
                History.add(temp);
            }
        }
        if(like !=null){
            for (String temp: like.split("-")){
                Liked.add(temp);
            }
        }
        if(playlist !=null){
            for (String temp: playlist.split("-")){
                Playlists.add(temp);
            }
        }
    }

    //If profile already created, simply return reference
    public static ActiveProfile getProfile() {
        return activeProfile;
    }

    //Return profile after it's creation
    public static ActiveProfile getProfile(String uname, String uid, String hist, String like, String playlist) {
        activeProfile = new ActiveProfile(uname, uid, hist, like, playlist);
        return activeProfile;
    }


    public String getUsername() {
        return username;
    }

    public String getUserID() {
        return userID;
    }
}
