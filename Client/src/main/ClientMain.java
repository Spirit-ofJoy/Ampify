package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class ClientMain extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{                             //loads Home page in JavaFX GUI thread
        Parent root = FXMLLoader.load(getClass().getResource("/resources/home.fxml"));
        primaryStage.setTitle("Ampify");

        primaryStage.setScene(new Scene(root, 400, 375));                        //Scene setting for home
        primaryStage.show();
    }

    private static final int ServerPORT = 5436;
    private static Socket mySocket;
    public static ObjectOutputStream clientOutputStream;
    public static ObjectInputStream clientInputStream;

    static {                                                 //Static block implements before Main and establishes Main connection
        try {
            mySocket = new Socket("127.0.0.1", ServerPORT);
            System.out.println("[CLIENT] Server connection established.");
            clientOutputStream = new ObjectOutputStream(mySocket.getOutputStream());
            clientInputStream = new ObjectInputStream(mySocket.getInputStream());
        } catch (ConnectException e){
            System.out.println("[CLIENT] Server offline currently. Try again later...");
        } catch (SocketException e) {
            System.out.println("[CLIENT] Server connection Lost.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);                                 //JavaFX GUI launched
    }
}
