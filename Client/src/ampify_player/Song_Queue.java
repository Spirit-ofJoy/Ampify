package ampify_player;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class Song_Queue {
    /*
    static int queue_size=4;
    static String song1filepath = "S#025";
    static String song2filepath = "S#003";
    static String song3filepath = "S#005";
    static String song4filepath = "S#007";

     */
    //Stores the song id used for calling the song from server
    static ObservableList<String> queue = FXCollections.observableArrayList();
    //Stores the song name used for display
    static ObservableList<String> nameQueue = FXCollections.observableArrayList();

    public static void addToQueue(String element, String name) {
        //queue_size=queue.size();
        queue.add(element);
        nameQueue.add(name);
    }

    public static void traverse() {
        for (String traversal : queue) {
            System.out.println(traversal);
        }
    }

    public static void removeFromQueue(int index1, int index2) {
        queue.remove(index1, index2);
        nameQueue.remove(index1, index2);
    }

    public static void swapSongElements(int index1, int index2) {
        Collections.swap(queue, index1, index2);
        Collections.swap(nameQueue, index1, index2);
    }

    public static String get_song(int index) {
        return queue.get(index);
    }

}
