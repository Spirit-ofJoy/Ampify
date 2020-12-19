package radioServer;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static radioServer.radioFile.create_radio_file;
import static radioServer.Song_Queue.create_songs_String;

public class RadioMain {

    public static final String CONTEXT = "/ampify";
    public static final int PORT = 8000;

    public static void startRADIO() throws IOException, UnsupportedAudioFileException {
        // Create a new StreamingHttpServer with HttpRequestFilter and HttpRequestHandler
        radioHttpserver httpServer = new radioHttpserver(PORT, CONTEXT,
                new HttpRequestFilter(), new radioHttpRequestHandler());

        // Start the server
        httpServer.start();
        create_radio_file(create_songs_String(), "Overwritable Radio");
        System.out.println("Server is started and listening on port " + PORT);

        /*
         **UPDATE THE DESTINATION_FOLDER in RequestFilter before plying songs
         */

         /*
             STREAM       http://localhost:8000/ampify?radio=radioName
             Radio List   http://localhost:8000/ampify?radio=list_Of_Available_Radios
         */

    }

    /*
    public static void main(String[] args) throws Exception {
        startRADIO();
    }
     */
}