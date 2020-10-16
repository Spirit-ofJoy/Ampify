package main;

import java.util.TreeSet;

//Class to store user profile details
public class ActiveProfile {

    private String username;
    private String userID;

    //Relevant collection of ids are stored in TreeSets
    public TreeSet<String> History = new TreeSet<String>();
    public TreeSet<String> Liked = new TreeSet<String>();
    public TreeSet<String> Disliked = new TreeSet<String>();
    public TreeSet<String> Playlists = new TreeSet<String>();

    //Singleton Object
    private static ActiveProfile activeProfile = null;

    //Constructor call
    private ActiveProfile(String uname, String uid, String a, String b, String c, String d){
        userID = uid;
        username = uname;

        if(a!=null){
            for (String temp: a.split(".")){
                History.add(temp);
            }
        }
        if(b!=null){
            for (String temp: b.split(".")){
                Liked.add(temp);
            }
        }
        if(c!=null){
            for (String temp: c.split(".")){
                Disliked.add(temp);
            }
        }
        if(d!=null){
            for (String temp: d.split(".")){
                Playlists.add(temp);
            }
        }
    }

    //If profile already created, simply return reference
    public static ActiveProfile getProfile() {
        return activeProfile;
    }

    //Return profile after it's creation
    public static ActiveProfile getProfile(String u, String v, String w, String x, String y, String z) {
        activeProfile = new ActiveProfile(u, v, w, x, y, z);
        return activeProfile;
    }


    public String getUsername() {
        return username;
    }

    public String getUserID() {
        return userID;
    }
}
