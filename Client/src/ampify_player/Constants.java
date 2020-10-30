package ampify_player;

// these are CONSTANT Values or Paths that are used by the mp3 player throughout
public class Constants{
    /**
     * Folder and file management settings
     */
    public static final String song_buffer_location = "Ampify_Files/My_Downloaded_Songs/.buffer/.buffer.ampify";
    public static final String DESTINATION_DOWNLOAD_FOLDER = "Ampify_Files/My_Downloaded_Songs/";
    public static final String BUFFER_FOLDER = "Ampify_Files/My_Downloaded_Songs/.buffer/";
    public static final String FILE_EXTENSION = ".amp";
    public static final String ASSETS_LOCATION = "assets/";

    /**
     * network settings
     */
    public static final String protocol_used = "http://";
    public static final String host = "localhost";
    public static final String port = "8800";
    public static final String context = "/ampify";
    public static final String query_starts = "?";
    public static final String query_request = "song=";

    /**
     * @link http://localhost:8800/ampify?song=
     */
    public static String query_url = protocol_used + host + ":" + port + context + query_starts + query_request;
    public static int BufferLeeWay = 3000;//only used in @SongBufferTimeEvents  Package


}
