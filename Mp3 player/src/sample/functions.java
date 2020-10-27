package sample;

public class functions {

    /**
     * @param song_name must only be used when givng the song name in a URL otherwise don't
     * @return song name but changes '#' with '%23' as only then it can be used in a URL
     */
    public static String use_song_name(String song_name){
        System.out.println(song_name);
        return song_name.replace("#","%23");                          // we cant use '#' in URL directly
        //so we have to use %23 in place of #
    }

    /**
     * @param query_url
     * @param song_name (format is not needed to specify here)
     * @return Complete Usable URL
     */
    public static String useURL(String query_url, String song_name){                    //give the song name with format
        String URL = query_url+song_name+".mp3";                       //Setting the URL of the Song
        return URL;
    }

}
