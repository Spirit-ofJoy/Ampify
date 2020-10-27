package main;

import Requests.*;
import Responses.*;
import DatabaseConnection.*;
import constants.Constant;
import utility.Playlist;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
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

                //executes if Request is for Signing Up
                if(incomingRequest.getReqType().equals(String.valueOf(Constant.SIGNUP_START))) {
                    SignUpRequest signUpRequest = (SignUpRequest) incomingRequest;
                    SignUpResponse signUpResponse = NewSignUp.createAccount(signUpRequest.getUsername(), signUpRequest.getPassword(),
                            signUpRequest.getPreferenceLanguage(), signUpRequest.getPreferenceGenre(), signUpRequest.getPreferenceArtists());

                    //Sign Up request processed and eligible response sent
                    objectOutputStream.writeObject(signUpResponse);
                    objectOutputStream.flush();

                }
                //executes if Request type is of login
                else if( incomingRequest.getReqType().equals(String.valueOf(Constant.LOGIN_CHECK))) {
                    LoginRequest loginRequest = (LoginRequest) incomingRequest;
                    LoginResponse loginResponse = NewLogin.checkLogin(loginRequest.getUsername(), loginRequest.getPassword());

                    //Login request processed and response returned
                    objectOutputStream.writeObject(loginResponse);
                    objectOutputStream.flush();
                }
                //executes if Request is made for the top songs based on viewership
                else if(incomingRequest.getReqType().equals(String.valueOf(Constant.TOP_HITS_LIST))) {
                    TopHitsResponse mostViewed = LoadProfile.getMostViewed();

                    //Send back the most viewed songs
                    objectOutputStream.writeObject(mostViewed);
                    objectOutputStream.flush();
                }
                //executes to load and provide personalized recommendations
                else if(incomingRequest.getReqType().equals(String.valueOf(Constant.PERSONAL_RECOMMENDS))) {
                    RecommendsRequest recommendsRequest = (RecommendsRequest) incomingRequest;
                    RecommendsResponse personalRecommends = LoadProfile.getRecommends(recommendsRequest.getUserID());

                    //Send back personalized selection
                    objectOutputStream.writeObject(personalRecommends);
                    objectOutputStream.flush();
                }
                //executes to load Browsing section of songs
                else if(incomingRequest.getReqType().equals(String.valueOf(Constant.SONG_BROWSE))) {
                    BrowseResponse browseResponse = BrowseSongs.getSongs();

                    //Send back filtered collection of songs
                    objectOutputStream.writeObject(browseResponse);
                    objectOutputStream.flush();
                }
                //executes to give apt songs for searched category
                else if(incomingRequest.getReqType().equals(String.valueOf(Constant.SONG_SEARCH))) {
                    SongSearchRequest songSearchRequest = (SongSearchRequest) incomingRequest;
                    SongSearchResponse searchResult = SearchSongs.getSearchedSongs(songSearchRequest.getSearchType(), songSearchRequest.getSearchKey());

                    //Send back selection based on search result
                    objectOutputStream.writeObject(searchResult);
                    objectOutputStream.flush();
                }
                //executes to give song-names to display on history
                else if(incomingRequest.getReqType().equals(String.valueOf(Constant.HISTORY_INFO))) {
                    HistoryInfoRequest historyInfoRequest = (HistoryInfoRequest) incomingRequest;
                    ArrayList<String> historyResult = SearchSongs.getSongNames(historyInfoRequest.historySongID);

                    //Send back names of songs on history
                    objectOutputStream.writeObject(new HistoryInfoResponse(historyResult));
                    objectOutputStream.flush();
                }
                //executes to give playlists to display for a user
                else if(incomingRequest.getReqType().equals(String.valueOf(Constant.PERSONAL_PLAYLISTS_SET))) {
                    PersonalPlaylistsRequest playlistsRequest = (PersonalPlaylistsRequest) incomingRequest;
                    ArrayList<Playlist> playlistsResult = PlaylistPick.getPersonalPlaylist(playlistsRequest.getUserID());

                    //Send back names of songs on history
                    objectOutputStream.writeObject(new PersonalPlaylistsResponse(playlistsResult));
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
