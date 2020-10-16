package controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.ActiveProfile;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileControl implements Initializable {

    ActiveProfile currProfile = ActiveProfile.getProfile();
    public Label userGreeting ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userGreeting.setText("Welcome, " + currProfile.getUsername());
    }


    //public ProfileControl(){
    //    userGreeting.setText("Welcome, "+currProfile.getUsername());
    //}



}
