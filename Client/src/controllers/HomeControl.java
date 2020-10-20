package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class HomeControl  {

    public Button loginButton;
    public Button signUpButton;

    //Change to login scene
    public void loginLoad() throws IOException {
        System.out.println("[CLIENT] Login Page invoked.");
        loginButton.setText("Loading...");

        Parent loginRoot = FXMLLoader.load(getClass().getResource("/resources/login.fxml"));      //call login.fxml and load it to loginRoot
        Scene loginScene = new Scene(loginRoot, 475, 400);                                       //create scene

        Stage window = (Stage) loginButton.getScene().getWindow();                           //Gets original stage of scene

        window.setScene(loginScene);                                                         //Sets scene
        window.show();                                                                       //Shows scene
    }

    //Change to Sign Up scene
    public void signUpLoad() throws IOException {
        System.out.println("[CLIENT] Sign Up page invoked.");
        signUpButton.setText("Loading...");

        Parent signupRoot = FXMLLoader.load(getClass().getResource("/resources/signup.fxml"));      //call login.fxml and load it to loginRoot
        Scene signupScene = new Scene(signupRoot, 600, 575);                                       //create scene

        Stage window = (Stage) signUpButton.getScene().getWindow();                           //Gets original stage of scene

        window.setScene(signupScene);                                                         //Sets scene
        window.show();
    }

}
