package DatabaseConnection;

import Responses.LoadGroupChatResponse;
import Responses.LoadGroupResponse;
import Responses.PersonalGroupsResponse;
import Responses.UserListResponse;
import utility.Group;
import utility.Playlist;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GroupConnect extends DatabaseConnect{

    public static UserListResponse getUsers() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            String searchQuery = "SELECT USERID, Uname FROM users";
            PreparedStatement searchPreStat = connection.prepareStatement(searchQuery);
            ResultSet resultSet = searchPreStat.executeQuery();

            ArrayList<String> allUsernames = new ArrayList<String>();
            ArrayList<String> allUserID = new ArrayList<String>();

            while(resultSet.next()) {
                allUserID.add(resultSet.getString(1));
                allUsernames.add(resultSet.getString(2));
            }

            return (new UserListResponse(allUsernames, allUserID));

        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }  catch (SQLException throwables) {
            throwables.printStackTrace();
        }  finally {
            try {
                //Closes connection to avoid any database tampering
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        //In case no viable list found
        return null;
    }

    public static void creatingGroup(String usersList, String groupName) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            //Finding new Group ID value
            String gidQuery = "SELECT COUNT(GROUP_ID) FROM user_groups;";
            PreparedStatement gidPreStat = connection.prepareStatement(gidQuery);
            ResultSet gidResultSet = gidPreStat.executeQuery();
            gidResultSet.next();
            String grpCnt = gidResultSet.getString(1);
            int groupCount = Integer.parseInt(grpCnt);
            groupCount = groupCount +1;
            grpCnt = String.format("%03d", groupCount);
            String groupID = "G#"+grpCnt;

            //Create group in Database
            String insertionQuery = "INSERT INTO user_groups (GROUP_ID, Grp_Name, Users_incl, GrpMsgs) VALUES (?, ?, ?, ?)";
            PreparedStatement insertionPreStat = connection.prepareStatement(insertionQuery);
            insertionPreStat.setString(1, groupID);
            insertionPreStat.setString(2, groupName);
            insertionPreStat.setString(3, usersList);
            insertionPreStat.setString(4, "[CHAT STARTED]%n%");
            insertionPreStat.execute();

            //Create group's playlist
            PlaylistPick.creatingPlaylist(null, groupID, groupName+"'s Playlist", 0);

        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }  catch (SQLException throwables) {
            throwables.printStackTrace();
        }  finally {
            try {
                //Closes connection to avoid any database tampering
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


    public static PersonalGroupsResponse getPersonalGroups(String id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            String searchQuery = "SELECT * FROM user_groups";
            PreparedStatement searchPreStat = connection.prepareStatement(searchQuery);
            ResultSet resultSet = searchPreStat.executeQuery();

            ArrayList<String> groupNames = new ArrayList<String>();
            ArrayList<String> groupIds = new ArrayList<String>();
            while(resultSet.next()) {
                String listOfUsers = resultSet.getString("Users_incl");

                if(listOfUsers.contains(id)){
                    groupNames.add(resultSet.getString("Grp_Name"));
                    groupIds.add(resultSet.getString("GROUP_ID"));
                }
            }

            return new PersonalGroupsResponse(groupNames, groupIds);

        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }  catch (SQLException throwables) {
            throwables.printStackTrace();
        }  finally {
            try {
                //Closes connection to avoid any database tampering
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        //In case no viable list found
        return null;
    }


    public static LoadGroupResponse getGroupInfo(String grpId) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            String searchQuery = "SELECT * FROM user_groups WHERE GROUP_ID = ?";
            PreparedStatement searchPreStat = connection.prepareStatement(searchQuery);
            searchPreStat.setString(1, grpId);
            ResultSet resultSet = searchPreStat.executeQuery();
            resultSet.next();

            ArrayList<String> memberNames = new ArrayList<String>();
            ArrayList<String> memberId = new ArrayList<String>();
            String membersString = resultSet.getString("Users_incl");
            for (String temp: membersString.split("-")){
                memberId.add(temp);
            }

            String searchUsernameQuery = "SELECT Uname FROM users WHERE USERID = ?";
            PreparedStatement searchUsernamePreStat = connection.prepareStatement(searchUsernameQuery);
            ResultSet searchUsernameResult;
            for(int i=0; i< memberId.size(); i++){
                searchUsernamePreStat.setString(1, memberId.get(i));
                searchUsernameResult = searchUsernamePreStat.executeQuery();
                searchUsernameResult.next();
                memberNames.add(searchUsernameResult.getString("Uname"));
            }


            Playlist groupPlaylist = PlaylistPick.getGroupPlaylist(grpId);
            Group searchedGroup = new Group(resultSet.getString("Grp_Name"), grpId, memberNames, groupPlaylist);
            return new LoadGroupResponse(searchedGroup);

        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }  catch (SQLException throwables) {
            throwables.printStackTrace();
        }  finally {
            try {
                //Closes connection to avoid any database tampering
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        //In case no viable group found
        return null;

    }


    public static LoadGroupChatResponse gettingGroupChat(String groupId) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            String gchatQuery = "SELECT * FROM user_groups WHERE GROUP_ID = ?";
            PreparedStatement gchatPreStat = connection.prepareStatement(gchatQuery);
            gchatPreStat.setString(1, groupId);
            ResultSet resultSet = gchatPreStat.executeQuery();
            resultSet.next();

            String chat = resultSet.getString("GrpMsgs");
            return new LoadGroupChatResponse(chat);

        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }  catch (SQLException throwables) {
            throwables.printStackTrace();
        }  finally {
            try {
                //Closes connection to avoid any database tampering
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }

    public static void saveChat(String message, String groupId) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(getSqlPath(), getSqlName(), getSqlPaswd());

            String gchatQuery = "UPDATE user_groups SET GrpMsgs = CONCAT(GrpMsgs, ?) WHERE GROUP_ID = ?";
            PreparedStatement gchatPreStat = connection.prepareStatement(gchatQuery);
            gchatPreStat.setString(1, message);
            gchatPreStat.setString(2, groupId);
            gchatPreStat.execute();


        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }  catch (SQLException throwables) {
            throwables.printStackTrace();
        }  finally {
            try {
                //Closes connection to avoid any database tampering
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


}



