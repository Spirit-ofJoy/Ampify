package sample.SongBufferTimeEvents;

import javafx.animation.Animation;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.concurrent.*;

import static sample.Constants.BufferLeeWay;

/*
public abstract class ResponderToEvent implements SongReloadListener, Runnable{
    @Override
    public Duration WhenLimitReached(MediaPlayer mediaplayer) {
        if(mediaplayer.getTotalDuration().subtract(mediaplayer.getCurrentTime()).lessThan(Duration.millis(BufferLeeWay)))
        {
            mediaplayer.stop();
            return mediaplayer.getCurrentTime();
        }
        System.out.println("Hello there!");
        return null;
    }
}
*/
/*
abstract class RespondToEvent implements Callable {
    public Duration WhenLimitReached(MediaPlayer mediaplayer) {
        Duration seekerLocationBeforeReset;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Duration> callable = new Callable<Duration>() {
            @Override
            public Duration call() throws Exception {
                while (true) {
                    if (mediaplayer.getTotalDuration().subtract(mediaplayer.getCurrentTime()).lessThan(Duration.millis(BufferLeeWay))) {
                        mediaplayer.pause();
                        seekerLocationBeforeReset=mediaplayer.getCurrentTime();
                        return mediaplayer.getCurrentTime();
                    }
                    System.out.println("The the to switch comes");
                    return null;
                }
            }

        };
        Future<Duration> future = executor.submit(callable);
        executor.shutdown();
        return null;
    }
}

/*
class TestCallable extends Task<Integer> implements Callable<Integer> {

    private Semaphore semaphore;
    MediaPlayer mediaPlayer;

    TestCallable(List<Integer> list, Semaphore semaphore) {
        this.list = list;
        this.semaphore = semaphore;
    }

    @Override
    public Integer call(){
        System.out.println("SENDING");
        System.out.println(list);
        try {
            Thread.sleep(1000+random.nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("RECEIVED");
        semaphore.release();
        return list.size();
    }
}
*/


public class ReachedBufferLimit implements Callable<Boolean> {

    File songFile;
    String song_location;
    private static MediaPlayer mediaPlayer;
    private int count = 0;
    Media media;
    private static String absolutePath;

    /**
     * @param mediaPlayer Constructor for passing the value to the thread
     */
    public ReachedBufferLimit(MediaPlayer mediaPlayer, String song_location ) {
        this.mediaPlayer = mediaPlayer;
        this.song_location=song_location;
        this.songFile=new File(song_location);
        this.absolutePath = songFile.getAbsolutePath().replace("\\", "/");;
    }


    @Override
    public synchronized Boolean call() throws Exception {
        while (true) {
            if (mediaPlayer.getTotalDuration().subtract(mediaPlayer.getCurrentTime()).lessThan(Duration.millis(BufferLeeWay))) {
                mediaPlayer.pause();
                count=1;
                if (count == 1) {
                    System.out.println("LOL    "+mediaPlayer.getCurrentTime());
                    //mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(new Duration(3000)));
                    /*media=new Media(absolutePath);
                    mediaPlayer.play();
                    mediaPlayer=new MediaPlayer(media);
                    System.out.println("MediaPlayer Loaded");
                    mediaPlayer.seek(mediaPlayer.getTotalDuration().subtract(Duration.millis(BufferLeeWay)));
                    mediaPlayer.play();

                     */
                    return true;
                }
            }
        }

    }
}

