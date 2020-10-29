package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class Song_Queue {
    static int queue_size=4;
    static String song1filepath = "S#025";
    static String song2filepath = "S#003";
    static String song3filepath = "S#005";
    static String song4filepath = "S#007";
    static ObservableList<String> queue = FXCollections.observableArrayList(song1filepath,song2filepath,song3filepath,song4filepath);

    public static void addToQueue(String element) {
        queue_size=queue.size();
        queue.add(element);
    }

    public static void traverse() {
        for (String traversal : queue) {
            System.out.println(traversal);
        }
    }

    public static void removeFromQueue(int index1,int index2) {
        queue.remove(index1,index2);
        queue_size=queue.size();;
    }

    public static void swapSongElements(int index1, int index2) {
        Collections.swap(queue, index1,index2);
    }

    public static String get_song(int index) {
        return queue.get(index);
    }

    public static void main(String args[]) {
        System.out.println("==============================================");
        traverse();
        removeFromQueue(1,2);
        System.out.println("==============================================");
        traverse();
        System.out.println("==============================================");
        traverse();
    }


    //delete,add,traverse,swap,
    /*
    list1
    list2 -> list3
    list3 -> list2
    list4
     */
}
