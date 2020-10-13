

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

        Parent loginRoot = FXMLLoader.load(getClass().getResource("login.fxml"));      //call login class
        Scene loginScene = new Scene(loginRoot);                                             //create scene

        Stage window = (Stage) loginButton.getScene().getWindow();                           //Gets original stage of scene

        window.setScene(loginScene);                                                         //Sets scene
        window.show();
    }

    //Change to Sign Up scene
    public void signUpLoad() {
        System.out.println("[CLIENT] Sign Up page invoked.");
        signUpButton.setText("Loading...");
    }

}
