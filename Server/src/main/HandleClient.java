package main;

import Requests.*;
import Responses.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

                //executes if Request type is of login
                if( incomingRequest.getReqType().equals("LOGIN_CHECK") ){
                    LoginRequest loginRequest = (LoginRequest) incomingRequest;
                    ResultSet queryResult = DatabaseConnection.checkLogin(loginRequest.getUsername(), loginRequest.getPassword());

                    //Login request processed and result set returned

                    if (!queryResult.isBeforeFirst() ){
                        //If User not registered
                        objectOutputStream.writeObject(new LoginResponse("USER_NOT_FOUND"));
                        objectOutputStream.flush();
                    }
                    else {
                        //If user found, then send back profile details
                        queryResult.next();

                        objectOutputStream.writeObject(new LoginResponse(queryResult.getString("USERID"),
                                queryResult.getString("History"), queryResult.getString("Liked"),
                                queryResult.getString("Disliked"), queryResult.getString("Playlists") ));
                        objectOutputStream.flush();
                    }
                }
                //executes if Request is made for the top songs based on viewership
                else if(incomingRequest.getReqType().equals("TOP_HITS_LIST")) {
                    ResultSet mostViewed = DatabaseConnection.getMostViewed();

                    //Send back the most viewed songs
                    objectOutputStream.writeObject(new TopHitsResponse(mostViewed));
                    objectOutputStream.flush();
                }
                //executes to load and provide personalized recommendations
                else if(incomingRequest.getReqType().equals("RECOMMENDS_GIVEN")) {
                    RecommendsRequest recommendsRequest = (RecommendsRequest) incomingRequest;
                    ResultSet personalRecommends = DatabaseConnection.getRecommends(recommendsRequest.getUserID(), recommendsRequest.liked);

                    //Send back personalized selection
                    objectOutputStream.writeObject(new RecommendsResponse(personalRecommends));
                    objectOutputStream.flush();
                }

            } catch (EOFException e) {
                System.out.println("[SERVER] User disconnected or offline.");
                break;
            } catch (SocketException e){
                System.out.println("[SERVER] User connection lost");
                break;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("[SERVER] Error in retrieval of data from database.");
            }
        }

    }
}
