package utility;

import Requests.HistoryInfoRequest;
import Responses.HistoryInfoResponse;
import main.ClientMain;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MoodSelection {

    public static ArrayList<String> moodRecommends = new ArrayList<String>();
    private static synchronized void addToMoodRecom(String id) {
        moodRecommends.add(id);
    }

    public static synchronized ArrayList<String> getMoodRecommends() throws IOException, ClassNotFoundException {
        ActiveProfile currProfile = ActiveProfile.getProfile();

        ArrayList<String> historySongID = new ArrayList<String>();
        ArrayList<String> historySongTime = new ArrayList<String>();

        ArrayList<String> moodSongsId = new ArrayList<String>();

        String temp, temp2;

        for(int i = 0; i< currProfile.History.size(); i++) {
            temp = currProfile.History.get(i).substring(7);
            temp2 = currProfile.History.get(i).substring(1, 6);

            historySongID.add(temp);
            historySongTime.add(temp2);
        }

        /*
           The 4 different Time Sets are
           00:00-06:00 -> Midnight,
           06:00-12:00 -> Dawn,
           12:00-18:00 -> Midday,
           18:00-00:00 -> Dusk
         */
        String currentTime = new SimpleDateFormat("HH:mm").format(new Date());  //Current Time

        //Midnight
        if(currentTime.compareTo("06:00")<0){
            for(int i=0; i< historySongTime.size(); i++) {
                if(historySongTime.get(i).compareTo("06:00")<0){
                    if(!moodSongsId.contains(historySongID.get(i))){
                        moodSongsId.add(historySongID.get(i));
                    }
                }
            }
        }
        //Morning
        else if(currentTime.compareTo("12:00")<0){
            for(int i=0; i< historySongTime.size(); i++) {
                if((historySongTime.get(i).compareTo("12:00")<0)&&(historySongTime.get(i).compareTo("06:00")>=0)){
                    if(!moodSongsId.contains(historySongID.get(i))){
                        moodSongsId.add(historySongID.get(i));
                    }
                }
            }
        }
        //Midday
        else if(currentTime.compareTo("18:00")<0){
            for(int i=0; i< historySongTime.size(); i++) {
                if((historySongTime.get(i).compareTo("18:00")<0)&&(historySongTime.get(i).compareTo("12:00")>=0)){
                    if(!moodSongsId.contains(historySongID.get(i))){
                        moodSongsId.add(historySongID.get(i));
                    }
                }
            }
        }
        //Evening
        else if(currentTime.compareTo("24:00")<0){
            for(int i=0; i< historySongTime.size(); i++) {
                if((historySongTime.get(i).compareTo("24:00")<0)&&(historySongTime.get(i).compareTo("18:00")>=0)){
                    if(!moodSongsId.contains(historySongID.get(i))){
                        moodSongsId.add(historySongID.get(i));
                    }
                }
            }
        }

        ClientMain.clientOutputStream.writeObject(new HistoryInfoRequest(moodSongsId));
        ClientMain.clientOutputStream.flush();
        HistoryInfoResponse historyInfoResponse = (HistoryInfoResponse) ClientMain.clientInputStream.readObject();

        for (int i = 0; i<moodSongsId.size(); i++) {
            MoodSelection.addToMoodRecom(moodSongsId.get(i));
        }
        return historyInfoResponse.historySongNames;

    }
}
