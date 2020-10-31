package utility;

import Requests.SongPlayedRequest;
import main.ClientMain;

import java.io.IOException;

public class CommonElements {
    private static ActiveProfile currProfile = ActiveProfile.getProfile();

    public static void songPlayed(String songId) {
        currProfile.History.add(Time.getCurrentTimeStamp()+songId);

        String historyChange = "";
        for (String temp: currProfile.History) {
            historyChange +=temp + "-";
        }

        String finalHistoryChange = historyChange;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ClientMain.clientOutputStream.writeObject(new SongPlayedRequest(currProfile.getUserID(),
                            finalHistoryChange, songId));
                    ClientMain.clientOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
