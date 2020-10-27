package controllers;


import Requests.SignUpRequest;
import Responses.SignUpResponse;
import constants.Constant;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import main.ClientMain;

import java.io.IOException;


public class SignupControl {

    public TextField usernameTextfld;
    public PasswordField passwordFld;
    public FlowPane langPane;
    public FlowPane genrePane;
    public FlowPane artistPane;
    public Button signupProcessButton;
    public TextField msgDisplay;
    public Button backButton;
    public Button loginLoader;

    public CheckBox langEng;
    public CheckBox langHin;
    public CheckBox genPop;
    public CheckBox genRnB;
    public CheckBox genIndie;
    public CheckBox genRock;
    public CheckBox genEpop;
    public CheckBox genHipHop;
    public CheckBox genElec;
    public CheckBox artistA001;
    public CheckBox artistA002;
    public CheckBox artistA003;
    public CheckBox artistA004;
    public CheckBox artistA005;
    public CheckBox artistA006;
    public CheckBox artistA007;
    public CheckBox artistA008;
    public CheckBox artistA009;
    public CheckBox artistA011;
    public CheckBox artistA012;
    public CheckBox artistA013;
    public CheckBox artistA014;
    public CheckBox artistA015;
    public CheckBox artistA016;
    public CheckBox artistA017;

    //Initiate the signup process
    public void processSignUp() {

        String uname = usernameTextfld.getText();
        String pass = passwordFld.getText();
        String prefLang = "";
        String prefGenre = "";
        String prefArtist = "";

        //Language choices
        if(langEng.isSelected()){
            prefLang = prefLang.concat("English-");
        }
        if(langHin.isSelected()){
            prefLang = prefLang.concat("Hindi-");
        }

        //Genre choices
        if (genPop.isSelected()){
            prefGenre = prefGenre.concat("Pop-");
        }
        if (genRnB.isSelected()){
            prefGenre = prefGenre.concat("R_&_B-");
        }
        if (genIndie.isSelected()){
            prefGenre = prefGenre.concat("Indie-");
        }
        if (genRock.isSelected()){
            prefGenre = prefGenre.concat("Rock-");
        }
        if (genEpop.isSelected()){
            prefGenre = prefGenre.concat("Electro_Pop-");
        }
        if (genHipHop.isSelected()){
            prefGenre = prefGenre.concat("Hip_Hop-");
        }
        if (genElec.isSelected()){
            prefGenre = prefGenre.concat("Electronic-");
        }

        //Artist Choices
        if (artistA001.isSelected()){
            prefArtist = prefArtist.concat("A#001-");
        }
        if (artistA002.isSelected()){
            prefArtist = prefArtist.concat("A#002-");
        }
        if (artistA003.isSelected()){
            prefArtist = prefArtist.concat("A#003-");
        }
        if (artistA004.isSelected()){
            prefArtist = prefArtist.concat("A#004-");
        }
        if (artistA005.isSelected()){
            prefArtist = prefArtist.concat("A#005-");
        }
        if (artistA006.isSelected()){
            prefArtist = prefArtist.concat("A#006-");
        }
        if (artistA007.isSelected()){
            prefArtist = prefArtist.concat("A#007-");
        }
        if (artistA008.isSelected()){
            prefArtist = prefArtist.concat("A#008-");
        }
        if (artistA009.isSelected()){
            prefArtist = prefArtist.concat("A#009-");
        }
        if (artistA011.isSelected()){
            prefArtist = prefArtist.concat("A#011-");
        }
        if (artistA012.isSelected()){
            prefArtist = prefArtist.concat("A#012-");
        }
        if (artistA013.isSelected()){
            prefArtist = prefArtist.concat("A#013-");
        }
        if (artistA014.isSelected()){
            prefArtist = prefArtist.concat("A#014-");
        }
        if (artistA015.isSelected()){
            prefArtist = prefArtist.concat("A#015-");
        }
        if (artistA016.isSelected()){
            prefArtist = prefArtist.concat("A#016-");
        }
        if (artistA017.isSelected()){
            prefArtist = prefArtist.concat("A#017-");
        }

        String finalPrefGenre = prefGenre;
        String finalPrefLang = prefLang;
        String finalPrefArtist = prefArtist;

        Runnable signupProcess = new Runnable() {
            @Override
            public void run() {
                try {
                    ClientMain.clientOutputStream.writeObject(new SignUpRequest(uname, pass, finalPrefLang, finalPrefGenre, finalPrefArtist));
                    ClientMain.clientOutputStream.flush();                                          //Request sent

                    SignUpResponse incomingResponse;
                    incomingResponse = (SignUpResponse) ClientMain.clientInputStream.readObject();   //Response accepted

                    if(incomingResponse.getStatus().equals(String.valueOf(Constant.FAILURE))){
                        msgDisplay.setText("Unable to Sign Up. Please use another Username or try again later.");
                    }
                    else {
                        msgDisplay.setText("User created. Enter credentials at Login.");

                        //Update GUI for button to load Profile
                        Platform.runLater(()-> { loginLoader.setVisible(true); } );
                    }
                } catch(NullPointerException e) {
                    msgDisplay.setText("Server currently offline. Try again later.");
                } catch(IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread signupThread = new Thread(signupProcess);  //Request run and handled in new thread
        signupThread.start();

    }


    //Go to Login Page
    public void loadLoginPage() throws IOException {
        System.out.println("[CLIENT] Login Page invoked.");
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/resources/login.fxml"));
        Scene loginScene = new Scene(loginRoot, 475, 400);

        Stage window = (Stage) loginLoader.getScene().getWindow();

        window.setScene(loginScene);
        window.show();
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


}