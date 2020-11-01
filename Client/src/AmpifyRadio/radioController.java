package AmpifyRadio;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static AmpifyRadio.functionsConstants.*;

public class radioController {

    @FXML
    public TextField Currently_Playing;

    @FXML
    public Slider VolumeSlider;

    @FXML
    public Button stopRadio;

    @FXML
    public Button startRadio;

    @FXML
    public Button loadRadio;


    String URL;
    private Duration radio_time;
    private Duration player_time;
    String song;

    //@FXML
    //private Label currentlyPlaying;

    Media media = null;
    MediaPlayer mp3player =null;

    /**
     * @param playSong Give the name of the song ( no need to give the format )
     *                 and it starts playing the specific song through online service
     * @param seek this is for advanced functionality Group playlist to start the song from a specific time seek
     */
    public void startStreaming(String playSong, Duration seek) throws InterruptedException, MalformedURLException {
        //stopPlayback();//stops playback if a music service was already running

        URL = useURL(query_url_song_server, use_song_name(playSong));//preparing an Http query for requesting the song
        URL url = new URL(URL);
        byte[] temp = url.toString().getBytes();
        String s = new String(temp);

        mp3player = null;//emptying the mp3player

        //http://localhost:8800/ampify?song=S%23007.mp3

        //seektovalue(mp3player,new Duration(2));
        media = new Media(s);//useURL(query_url_song_server, use_song_name(playSong)));
        //media = new Media(useURL(query_url_song_server, use_song_name(playSong)));
        mp3player = new MediaPlayer(media);//setting up the mp3 player with the URL of the requested song

        //mediaview.setMediaPlayer(mp3player);//GUI of media player is set here

        mp3player.setAutoPlay(false);//media started playing

        //currentlyPlaying.setText("LOL");//setting the song name heading
        song_properties(mp3player);//setting up song's properties
    }

    public void song_properties(MediaPlayer mp3player) {
        /*
         * Here we are setting up the test fields Like Elapsed Duration and Total Duration.
         * Also, using that elapsed time data, we are calculating where should we change the song or restart the
         * playback of the same song if "Repeat" is on.
         */
        mp3player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {

                //setting up the text fields @elapsedTime and @totalTime
                Duration elapsedDuration = mp3player.getCurrentTime();
                Duration totalDuration = mp3player.getTotalDuration();

                /*
                 * we calculate the elapsed duration slightly ahead of the total duration time to confirm our success
                 * before the playback stops
                 */
                if(totalDuration.lessThan(elapsedDuration.add(new Duration(50)))) {
                        try {play_next();} catch (InterruptedException | IOException e) { e.printStackTrace(); }
                }

            }
        });

        /*
         * Here we set the title of the main pane as  ( "Title"   from   Album   by   Artist )
         */
        mp3player.getMedia().getMetadata().addListener(new MapChangeListener<String, Object>() {
            @Override
            public void onChanged(Change<? extends String, ?> change) {
                String Title = (String)mp3player.getMedia().getMetadata().get("title");
                String Album = (String)mp3player.getMedia().getMetadata().get("album");
                String Artist = (String)mp3player.getMedia().getMetadata().get("artist");
                Currently_Playing.setText("\""+Title+"\""+" from "+Album+" by "+Artist);
            }
        });

        /**
         * Volume Slider active
         */
        VolumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mp3player.setVolume(VolumeSlider.getValue()/100);

            }
        });
    }


    /**
     * Load Radio Button loads radio mp3
     */
    @FXML
    public void loadRadio(ActionEvent actionEvent) throws InterruptedException, IOException {
        setRadioData();
        startStreaming(song,Duration.millis(0));
    }

    /**
     * seeks to the currentl y position
     */
    void startplaying() {
        double seekto = System.currentTimeMillis();
        player_time = new Duration(seekto);
        mp3player.seek((player_time.subtract(radio_time)));
        mp3player.play();
    }

    /**
     * @param actionEvent Starts radio
     */
    public void startRadio(ActionEvent actionEvent) {
        startplaying();
    }

    /**
     * This function sets the currently playing data to the radio_client
     * @throws IOException
     */
    public void setRadioData() throws IOException {
        URLConnection conn = new URL(query_url_radio_server+"radio1").openConnection();
        InputStream is = conn.getInputStream();
        byte[] currently_playing_radio_data = new byte[128];
        is.read(currently_playing_radio_data);

        int index,index_space = 0;
        String timestamp;
        String complete;

        for(index=0;index<currently_playing_radio_data.length;index++){
            if((char)currently_playing_radio_data[index]==' '){
                index_space=index;
            }
        }

        complete=new String(currently_playing_radio_data);
        song=complete.substring(index_space+1,complete.length());
        timestamp=complete.substring(0,index_space);
        radio_time= Duration.millis(Double.parseDouble(timestamp));
    }

    public void stopRadio(ActionEvent actionEvent) throws IOException, InterruptedException {
        setRadioData();
        stopPlayback();
        startStreaming(song,Duration.millis(0));

    }

    void play_next() throws InterruptedException, IOException {
            stopPlayback();
            setRadioData();
            startStreaming(song,Duration.millis(0));
            startplaying();
    }

    private void stopPlayback() {
        if (mp3player != null)
            mp3player.pause();
    }


}
