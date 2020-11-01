package radioServer;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static radioServer.radioFile.create_radio_file;
import static radioServer.Song_Queue.create_songs_String;

public class RadioMain {

    private static final String CONTEXT = "/ampify";
    private static final int PORT = 8000;

    public static void startRADIO() throws IOException, UnsupportedAudioFileException {
        // Create a new StreamingHttpServer with HttpRequestFilter and HttpRequestHandler
        radioHttpserver httpServer = new radioHttpserver(PORT, CONTEXT,
                new HttpRequestFilter(), new radioHttpRequestHandler());

        // Start the server
        httpServer.start();
        create_radio_file(create_songs_String());
        System.out.println("Server is started and listening on port " + PORT);

        /*
         **UPDATE THE DESTINATION_FOLDER in RequestFilter before plying songs
         */

         /*
             STREAM       http://localhost:8000/ampify?radio=radio1
         */

    }

    public static void main(String[] args) throws Exception {
        startRADIO();
    }
}