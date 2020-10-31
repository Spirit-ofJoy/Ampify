package ampify_player;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class downloadSRT extends Service<Void> {

    public String CURRENTLY_PLAYING;

    public downloadSRT(String CURRENTLY_PLAYING){
        this.CURRENTLY_PLAYING=CURRENTLY_PLAYING;
    }

    protected Task createTask() {
        return new Task<Void>() {
            protected Void call() throws Exception {
                Download_Encrypt_Decrypt.download_srt(CURRENTLY_PLAYING);//This method decrypt the song
                System.out.println("DOWNLOADING SRT BEGINS");
                return null;

            }
        };
    }
}