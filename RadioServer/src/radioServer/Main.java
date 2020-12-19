package radioServer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static radioServer.RadioMain.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("radio.fxml"));
        primaryStage.setTitle("Stream on Ampify Radio");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
        try {
            // Create a new StreamingHttpServer with HttpRequestFilter and HttpRequestHandler
            radioHttpserver httpServer = new radioHttpserver(PORT, CONTEXT,
                    new HttpRequestFilter(), new radioHttpRequestHandler());
            // Start the server
            httpServer.start();
            System.out.println("Server is started and listening on port " + PORT);
        } catch (Exception e) {

        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
