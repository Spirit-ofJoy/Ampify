package AmpifyRadio;

public class functionsConstants {

    /*
     * network settings
     */
    public static final String protocol_used = "http://";
    public static final String host = "localhost";
    public static final String music_server_port = "8800";
    public static final String radio_server_port = "8000";
    public static final String context = "/ampify";
    public static final String query_starts = "?";
    public static final String query_request = "song=";
    public static final String format = "";

    /*
     * @link http://localhost:8800/ampify?song=
     */
    public static String query_url_song_server = protocol_used + host + ":" + music_server_port + context + query_starts + query_request;

    /**
     *
     */
    public static String query_url_radio_server = protocol_used + host + ":" + radio_server_port + context + query_starts + "radio=";


    /**
     * @param song_name must only be used when givng the song name in a URL otherwise don't
     * @return song name but changes '#' with '%23' as only then it can be used in a URL
     */
    public static String use_song_name(String song_name){
        System.out.println(song_name);
        return song_name.replace("#","%23");// we cant use '#' in URL directly
        //so we have to use %23 in place of #
    }

    /**
     * @param query_url give @query_url as the input
     * @param song_name (format is not needed to specify here)
     * @return Complete Usable URL
     */
    public static String useURL(String query_url, String song_name){                    //give the song name with format
        return query_url+song_name;                       //Setting the URL of the Song
    }

}
