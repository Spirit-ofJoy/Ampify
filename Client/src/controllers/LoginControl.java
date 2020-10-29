package controllers;

import Requests.LoginRequest;
import Responses.LoginResponse;
import constants.Constant;
import javafx.application.Platform;
import utility.ActiveProfile;
import main.ClientMain;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginControl {

    public TextField usernameTextfld;
    public PasswordField passwordFld;
    public Button loginProcessButton;
    public TextField msgDisplay;
    public Button backButton;
    public Button profileLoader;

    //Checking login credentials by sending request to server
    public void processLogin() {

        String uname = usernameTextfld.getText();          //Takes username
        String pass = passwordFld.getText();               //Takes password
        loginProcessButton.setText("Loading...");

        //Runnable over new thread to connect to Server through the connection streams and get login info
        Runnable loginProcess = new Runnable() {
            @Override
            public void run() {
                try {
                    ClientMain.clientOutputStream.writeObject(new LoginRequest(uname, pass));
                    ClientMain.clientOutputStream.flush();                                          //Request sent

                    LoginResponse incomingResponse;
                    incomingResponse = (LoginResponse) ClientMain.clientInputStream.readObject();   //Response accepted

                    if(incomingResponse.getUserID().equals(String.valueOf(Constant.USER_NOT_FOUND))){
                        msgDisplay.setText("User not Found. Please check credentials or Sign-Up if don't have an account.");
                    }
                    else {
                        msgDisplay.setText("User found. Loading Profile now.");

                        //Making a profile for user found
                        ActiveProfile arbitary = ActiveProfile.getProfile(uname, incomingResponse.getUserID(),
                                incomingResponse.getHistory(), incomingResponse.getLiked(), incomingResponse.getPlaylists());

                        //Update GUI for button to load Profile
                        Platform.runLater(()-> { profileLoader.setVisible(true); } );
                    }
                } catch(NullPointerException e) {
                    msgDisplay.setText("Server currently offline. Try again later.");
                } catch(IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread loginThread = new Thread(loginProcess);  //Request run and handled in new thread
        loginThread.start();

    }

    //Go back to home
    public void backToHome() throws IOException {
        System.out.println("[CLIENT] Home Page invoked.");
        Parent homeRoot = FXMLLoader.load(getClass().getResource("/resources/home.fxml"));
        Scene homeScene = new Scene(homeRoot, 400, 375);
        Stage homeStage = (Stage) backButton.getScene().getWindow();
        homeStage.setScene(homeScene);
        homeStage.show();
    }

    //Go to Profile
    public void loadProfilePage() throws IOException {
        System.out.println("[CLIENT] Profile Page invoked.");

        Parent profileRoot = FXMLLoader.load(getClass().getResource("/resources/profile.fxml"));
        Scene profileScene = new Scene(profileRoot);
        Stage profileStage = (Stage) profileLoader.getScene().getWindow();
        profileStage.setScene(profileScene);
        profileStage.show();
    }

}