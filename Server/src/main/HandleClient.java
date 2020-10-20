package main;

import Requests.*;
import Responses.*;
import DatabaseConnection.*;
import constants.Constant;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;


//Class to handle and interact with incoming clients
public class HandleClient implements Runnable {

    private Socket clientSocket;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;

    public HandleClient(Socket socket) {            //Constructor to channel and assign proper streams for each client
        this.clientSocket = socket;

        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                Request incomingRequest = (Request) objectInputStream.readObject();

                //executes if Request is for Signing Up
                if(incomingRequest.getReqType().equals("SIGNUP_START")) {
                    SignUpRequest signUpRequest = (SignUpRequest) incomingRequest;
                    SignUpResponse signUpResponse = NewSignUp.createAccount(signUpRequest.getUsername(), signUpRequest.getPassword(),
                            signUpRequest.getPreferenceLanguage(), signUpRequest.getPreferenceGenre(), signUpRequest.getPreferenceArtists());

                    //Sign Up request processed and eligible response sent
                    objectOutputStream.writeObject(signUpResponse);
                    objectOutputStream.flush();

                }
                //executes if Request type is of login
                else if( incomingRequest.getReqType().equals("LOGIN_CHECK")) {
                    LoginRequest loginRequest = (LoginRequest) incomingRequest;
                    LoginResponse loginResponse = NewLogin.checkLogin(loginRequest.getUsername(), loginRequest.getPassword());

                    //Login request processed and response returned
                    objectOutputStream.writeObject(loginResponse);
                    objectOutputStream.flush();
                }
                //executes if Request is made for the top songs based on viewership
                else if(incomingRequest.getReqType().equals("TOP_HITS_LIST")) {
                    TopHitsResponse mostViewed = LoadProfile.getMostViewed();

                    //Send back the most viewed songs
                    objectOutputStream.writeObject(mostViewed);
                    objectOutputStream.flush();
                }
                //executes to load and provide personalized recommendations
                else if(incomingRequest.getReqType().equals("PERSONAL_RECOMMENDS")) {
                    RecommendsRequest recommendsRequest = (RecommendsRequest) incomingRequest;
                    RecommendsResponse personalRecommends = LoadProfile.getRecommends(recommendsRequest.getUserID());

                    //Send back personalized selection
                    objectOutputStream.writeObject(personalRecommends);
                    objectOutputStream.flush();
                }

            } catch (EOFException e) {
                System.out.println("[SERVER] User disconnected or offline.");
                break;
            } catch (SocketException e){
                System.out.println("[SERVER] User connection lost");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }

    }
}
