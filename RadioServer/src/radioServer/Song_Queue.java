package radioServer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.Collections;

import static radioServer.songDuration.getDuration;

/**
 * This class handles the song queue in the Radio Server.
 */
public class Song_Queue {
    /**
     * Defining some constants for creating a data file of Radio Music Playlist
     */
    final static char separator=';';
    final static String ASSETS_LOCATION = "../Server/assets/songs/";
    final static long LeeWay = 5000;

    //Creating an ArrayList for storing the songs in the queue
    static ObservableList<String> queue = FXCollections.observableArrayList();

    /**
     * @param element adds element to the queue
     */
    public static void addToQueue(String element) {
        queue.add(element);
    }

    /**
     * Auto Fills the Queue
     */
    public static void autofill() {
        String song = "";
        for (int index = 1; index <= 33; index++) {
            if (index % 10 != 0) {
                if (index < 10) {
                    song = "S#00" + index + ".mp3";
                    addToQueue(song);
                } else {
                    song = "S#0" + index + ".mp3";
                    addToQueue(song);
                }
            }
            System.out.println(song);
        }
    }

    /**
     * Prints the queue
     */
    public static void traverse() {
        for (String traversal : queue) {
            System.out.println(traversal);
        }
    }

    /**
     * Element is Removed from the queue
     * @param index1 Removed
     * @param index2 ///////
     */
    public static void removeFromQueue(int index1,int index2) {
        queue.remove(index1,index2);
    }

    /**
     * Elements are swapped in the queue
     * @param index1 to index 2
     * @param index2 to index 1
     */
    public static void swapSongElements(int index1, int index2) {
        Collections.swap(queue, index1,index2);
    }

    /**
     * @param index Gets a song from a particular index in the Queue
     * @return String of the queue element on the position.
     */
    public static String get_song(int index) {
        return queue.get(index);
    }

    /**
     *
     * @return Complete File String of the radio file that is to be stored for playing the online Radio
     * @throws IOException when file is not found
     * @throws UnsupportedAudioFileException when audio file is unsupported
     */
    public static String create_songs_String() throws IOException, UnsupportedAudioFileException {

        //initiallizing the radio file
        String radio_file="";

        //cummulative duration for knowing when on the local time the song will start
        long cummulativeDuration = (System.currentTimeMillis());

        System.out.println("The Radio starts at " + cummulativeDuration);

        //Storing the data in the string
        for (int index=0;index<queue.size();index++) {
            File file = new File(ASSETS_LOCATION+queue.get(index));
            String queue_song=queue.get(index);
            radio_file=radio_file+separator+cummulativeDuration+" "+queue_song;
            cummulativeDuration= cummulativeDuration+(getDuration(file))-LeeWay;
        }
        //storing the termination point of the stream
        File file=new File(ASSETS_LOCATION+queue.get(queue.size()-1));
        cummulativeDuration= (cummulativeDuration+(getDuration(file)));
        radio_file=radio_file+separator+cummulativeDuration+separator;
        //everything has been stored properly. Now return the file
        System.out.println("The radio file is  --  "+radio_file);
        return radio_file;
    }

}
