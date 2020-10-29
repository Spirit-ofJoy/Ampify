package sample.SongBufferTimeEvents;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import java.util.EventListener;
import java.util.EventObject;

import javax.swing.event.EventListenerList;


class MyEvent extends EventObject {
    public MyEvent(Object source) {
        super(source);
    }
}

interface MyEventListener extends java.util.EventListener {
    public void myEventOccurred(MyEvent evt);
}

class MyClass {
    protected EventListenerList listenerList = new EventListenerList();

    public void addMyEventListener(MyEventListener listener) {
        listenerList.add(MyEventListener.class, listener);
    }

    public void removeMyEventListener(MyEventListener listener) {
        listenerList.remove(MyEventListener.class, listener);
    }

    void fireMyEvent(MyEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i+2) {
            if (listeners[i] == MyEventListener.class) {
                ((MyEventListener) listeners[i+1]).myEventOccurred(evt);
            }
        }
    }
}

public class newEventListener {
    public static void main(String[] argv) throws Exception {
        MyClass c = new MyClass();
        c.addMyEventListener(new MyEventListener() {
            public void myEventOccurred(MyEvent evt) {
                System.out.println("fired");
            }
        });

    }
}


