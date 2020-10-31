package ampify_player;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class DownloadSong extends Service<String> {

    public String CURRENTLY_PLAYING;
    public String save_as;

    public DownloadSong(String CURRENTLY_PLAYING, String save_as){
        this.CURRENTLY_PLAYING=CURRENTLY_PLAYING;
        this.save_as=save_as;
    }

    //public final String get_CURRENTLY_PLAYING() { return this.CURRENTLY_PLAYING; }
    //public final String get_save_as() { return this.save_as;}

    protected Task createTask() {
        return new Task<String>() {
            protected String call() throws Exception {

                Download_Encrypt_Decrypt.encrypt_and_download_song(CURRENTLY_PLAYING,save_as);//This method decrypt the song
                return null;

            }
        };
    }
}