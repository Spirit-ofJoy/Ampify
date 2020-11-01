package radioServer;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static radioServer.Song_Queue.create_songs_String;
import static radioServer.radioFile.create_radio_file;

public class radioUIController {
    public Button host_radio;
    public Button addtoqueue;
    public ListView queue_songs;
    public TextField name;
    public Button save;

    private static final String CONTEXT = "/ampify";
    private static final int PORT = 8000;
    public Button auto_fill;
    public ListView all_songs;
    Song_Queue queue = new Song_Queue();

    public void addtoqueue(ActionEvent actionEvent) {
        queue_songs.setItems((ObservableList<String>) Song_Queue.queue);
    }

    public void save(ActionEvent actionEvent) {
        queue_songs.setItems((ObservableList<String>) Song_Queue.queue);
    }

    public void host_radio(ActionEvent actionEvent) throws IOException, UnsupportedAudioFileException {

        // Create a new StreamingHttpServer with HttpRequestFilter and HttpRequestHandler
        radioHttpserver httpServer = new radioHttpserver(PORT, CONTEXT,
                new HttpRequestFilter(), new radioHttpRequestHandler());

        // Start the server
        httpServer.start();
        create_radio_file(create_songs_String());
        System.out.println("Server is started and listening on port " + PORT);
    }

    public void name_of_radio(ActionEvent actionEvent) {

    }

    public void auto_fill(ActionEvent actionEvent) {
        queue.autofill();
        queue_songs.setItems((ObservableList<String>) Song_Queue.queue);
    }
}
