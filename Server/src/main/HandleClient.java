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

                switch (incomingRequest.getReqType()) {

                    //executes if Request is for Signing Up
                    case SIGNUP_START : {
                        SignUpRequest signUpRequest = (SignUpRequest) incomingRequest;
                        SignUpResponse signUpResponse = NewSignUp.createAccount(signUpRequest.getUsername(), signUpRequest.getPassword(),
                                signUpRequest.getPreferenceLanguage(), signUpRequest.getPreferenceGenre(),
                                signUpRequest.getPreferenceArtists());

                        //Sign Up request processed and eligible response sent
                        objectOutputStream.writeObject(signUpResponse);
                        objectOutputStream.flush();
                        break;
                    }

                    //executes if Request type is of login
                    case LOGIN_CHECK : {
                        LoginRequest loginRequest = (LoginRequest) incomingRequest;
                        LoginResponse loginResponse = NewLogin.checkLogin(loginRequest.getUsername(), loginRequest.getPassword());

                        //Login request processed and response returned
                        objectOutputStream.writeObject(loginResponse);
                        objectOutputStream.flush();
                        break;
                    }

                    //executes if Request is made for the top songs based on viewership
                    case TOP_HITS_LIST : {
                        TopHitsResponse mostViewed = LoadProfile.getMostViewed();

                        //Send back the most viewed songs
                        objectOutputStream.writeObject(mostViewed);
                        objectOutputStream.flush();
                        break;
                    }

                    //executes to load and provide personalized recommendations
                    case PERSONAL_RECOMMENDS : {
                        RecommendsRequest recommendsRequest = (RecommendsRequest) incomingRequest;
                        RecommendsResponse personalRecommends = LoadProfile.getRecommends(recommendsRequest.getUserID(), recommendsRequest.getLikedSongs());

                        //Send back personalized selection
                        objectOutputStream.writeObject(personalRecommends);
                        objectOutputStream.flush();
                        break;
                    }

                    //executes if Request is made for the new songs based on upload time
                    case NEW_RELEASES_LIST : {
                        NewReleasesResponse newReleasesResponse = LoadProfile.getNewReleases();

                        //Send back the newly released songs
                        objectOutputStream.writeObject(newReleasesResponse);
                        objectOutputStream.flush();
                        break;
                    }

                    //executes to load Browsing section of songs
                    case SONG_BROWSE : {
                        BrowseResponse browseResponse = BrowseSongs.getSongs();

                        //Send back filtered collection of songs
                        objectOutputStream.writeObject(browseResponse);
                        objectOutputStream.flush();
                        break;
                    }

                    //executes to give apt songs for searched category
                    case SONG_SEARCH : {
                        SongSearchRequest songSearchRequest = (SongSearchRequest) incomingRequest;
                        SongSearchResponse searchResult = SearchSongs.getSearchedSongs(songSearchRequest.getSearchType(),
                                songSearchRequest.getSearchKey());

                        //Send back selection based on search result
                        objectOutputStream.writeObject(searchResult);
                        objectOutputStream.flush();
                        break;
                    }

                    //executes to give song-names to display on history
                    case HISTORY_INFO : {
                        HistoryInfoRequest historyInfoRequest = (HistoryInfoRequest) incomingRequest;
                        ArrayList<String> historyResult = SearchSongs.getSongNames(historyInfoRequest.historySongID);

                        //Send back names of songs on history
                        objectOutputStream.writeObject(new HistoryInfoResponse(historyResult));
                        objectOutputStream.flush();
                        break;
                    }

                    //executes to give playlists to display for a user
                    case PERSONAL_PLAYLISTS_SET : {
                        PersonalPlaylistsRequest playlistsRequest = (PersonalPlaylistsRequest) incomingRequest;
                        ArrayList<Playlist> playlistsResult = PlaylistPick.getPersonalPlaylist(playlistsRequest.getUserID());

                        objectOutputStream.writeObject(new PersonalPlaylistsResponse(playlistsResult));
                        objectOutputStream.flush();
                        break;
                    }

                    //Update existing Playlist
                    case UPDATE_PLAYLIST : {
                        UpdatePlaylistRequest updatePlaylistRequest = (UpdatePlaylistRequest) incomingRequest;

                        PlaylistPick.updatingPlaylist(updatePlaylistRequest.getSongsList(), updatePlaylistRequest.getUserViewer(),
                                updatePlaylistRequest.getPlaylistID());
                        break;
                    }

                    //Create new Playlist
                    case CREATE_PLAYLIST : {
                        CreatePlaylistRequest createPlaylistRequest = (CreatePlaylistRequest) incomingRequest;

                        PlaylistPick.creatingPlaylist(createPlaylistRequest.getSongsList(), createPlaylistRequest.getOwnerID(),
                                createPlaylistRequest.getPlaylistName(), createPlaylistRequest.getVisibility());
                        break;
                    }

                    //Show shareable playlists
                    case SHARE_PLAYLISTS_SET : {
                        ShareablePlaylistsRequest playlistsRequest = (ShareablePlaylistsRequest) incomingRequest;
                        ArrayList<Playlist> playlistsResult = PlaylistPick.getShareablePlaylist(playlistsRequest.getUserID());

                        objectOutputStream.writeObject(new ShareablePlaylistsResponse(playlistsResult));
                        objectOutputStream.flush();
                        break;
                    }

                    //Import Playlist
                    case IMPORT_PLAYLIST : {
                        ImportPlaylistRequest importPlaylistRequest = (ImportPlaylistRequest) incomingRequest;
                        PlaylistPick.importingPlaylist(importPlaylistRequest.getPlaylistID(), importPlaylistRequest.getUserID());
                        break;
                    }

                    //Send back list of registered Users
                    case USERS_LIST : {
                        UserListRequest userListRequest = (UserListRequest) incomingRequest;
                        objectOutputStream.writeObject(GroupConnect.getUsers());
                        break;
                    }

                    //Records and changes database for a song played
                    case SONG_PLAYED : {
                        SongPlayedRequest songPlayedRequest = (SongPlayedRequest) incomingRequest;
                        SearchSongs.updateSongPlayed(songPlayedRequest.getUserId(), songPlayedRequest.getHistoryString(),
                                songPlayedRequest.getSongId());
                        break;
                    }

                    //Records and changes database for a song liked
                    case SONG_LIKED : {
                        LikedRequest likedRequest = (LikedRequest) incomingRequest;
                        SearchSongs.updateSongLiked(likedRequest.getUser(), likedRequest.getLikedSongs());
                        break;
                    }

                    //Executes a request to create a new group
                    case CREATE_GROUP : {
                        CreateGroupRequest createGroupRequest = (CreateGroupRequest) incomingRequest;
                        GroupConnect.creatingGroup(createGroupRequest.getUsersList(), createGroupRequest.getGrpName());
                        break;
                    }

                    //executes to give groups to display for a user
                    case PERSONAL_GROUPS_SET : {
                        PersonalGroupsRequest groupsRequest = (PersonalGroupsRequest) incomingRequest;

                        //Send back names of groups
                        objectOutputStream.writeObject(GroupConnect.getPersonalGroups(groupsRequest.getUserID()));
                        objectOutputStream.flush();
                        break;
                    }

                    //Gives Group info to Client
                    case LOAD_GROUP : {
                        LoadGroupRequest loadGroupRequest = (LoadGroupRequest) incomingRequest;

                        //Send back group's info
                        objectOutputStream.writeObject(GroupConnect.getGroupInfo(loadGroupRequest.getGrpId()));
                        objectOutputStream.flush();
                        break;
                    }

                    //Gives back Group chat for a particular group
                    case LOAD_GROUP_CHATS : {
                        LoadGroupChatRequest loadGroupChatRequest = (LoadGroupChatRequest) incomingRequest;
                        objectOutputStream.writeObject(GroupConnect.gettingGroupChat(loadGroupChatRequest.getGroupId()));
                        break;
                    }

                    //Stores Chat Received
                    case CHAT_SEND : {
                        SendChatRequest chatRequest = (SendChatRequest) incomingRequest;
                        GroupConnect.saveChat(chatRequest.getChatMsg(), chatRequest.getGroupId());
                        break;
                    }

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
            }
        }

    }
}
