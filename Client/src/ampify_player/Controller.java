package ampify_player;

import com.sun.media.jfxmediaimpl.platform.Platform;
import javafx.application.Application;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.io.InputStream;
import javafx.concurrent.Task;

import javax.sound.sampled.AudioInputStream;
import java.lang.Object;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.*;
import static ampify_player.Constants.*;
import static ampify_player.functions.*;
import ampify_player.Song_Queue;
import utility.CommonElements;

public class Controller<e> implements Initializable {

    @FXML
    public MediaPlayer mp3player = null;

    @FXML
    private VBox modifyingPanel;

    @FXML
    private JFXButton queueGoBackBtn;

    @FXML
    private JFXButton queueAddSongBtn;

    @FXML
    private JFXButton queueDeleteSongBtn;

    @FXML
    private Button playprevbtn;

    @FXML
    private Button playbtn;

    @FXML
    private Button playnextbtn;

    @FXML
    private TextField songtxt;

    @FXML
    private TextField lyricstxt;

    @FXML
    private Slider volumeslider;

    @FXML
    private Slider mp3seeker;

    @FXML
    private MediaView mediaview;

    @FXML
    private Button volumeBtn;

    @FXML
    private Button previousSongBtn;

    @FXML
    private Button nextSongBtn;

    @FXML
    private Button repeatBtn;

    @FXML
    private Button shuffleBtn;

    @FXML
    private Button OpenQueuebtn;

    @FXML
    private Button moveUpQueueBtn;

    @FXML
    private Button moveDownQueueBtn;

    @FXML
    private JFXButton queuePlayQueueBtn;

    @FXML
    private Button equalizerFreqResetBtn;

    @FXML
    private JFXButton queueClearQueueBtn;

    @FXML
    private ImageView musicIcon;

    @FXML
    private AnchorPane mediaPane;

    @FXML
    private AnchorPane queuePane;

    @FXML
    private AnchorPane equalizerPane;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Label elapsedTime;

    @FXML
    private Label totalTime;

    @FXML
    private JFXSlider equalizerSlider1;

    @FXML
    private JFXSlider equalizerSlider2;

    @FXML
    private JFXSlider equalizerSlider3;

    @FXML
    private JFXSlider equalizerSlider4;

    @FXML
    private JFXSlider equalizerSlider5;

    @FXML
    private JFXSlider equalizerSlider6;

    @FXML
    private JFXSlider equalizerSlider7;

    @FXML
    private JFXSlider equalizerSlider8;

    @FXML
    private JFXSlider equalizerSlider9;

    @FXML
    private JFXSlider equalizerSlider10;

    @FXML
    private ListView<String> queueListView;

    boolean equalizerState = false;
    List<File> fileListbackup = new ArrayList<File>();
    Vector<File> currentsonglist = new Vector<>(1);
    final double START_FREQ = 32.0;
    final int BAND_COUNT = 10;

    private String URL;
    public static String CURRENTLY_PLAYING = "S#017";
    public static int CURRENTLY_PLAYING_INDEX = 0;
    public String metadata_song_name;
    public static boolean change_again=false;

    String filepath;
    File file = new File("");
    Media media = null;
    private Duration seekPosition = Duration.millis(0);
    boolean queue_playing = false;
    boolean is_repeat = false;
    boolean is_shuffle = false;


    /**
     * @param playSong Give the name of the song ( no need to give the format )
     *                 and it starts playing the specific song through online service
     * @param seek this is for advanced functionality Group playlist to start the song from a specific time seek
     */
    public void startStreaming(String playSong, Duration seek) throws InterruptedException {
        showMainPane();//shows media pane if a song is changed or started
        stopPlayback();//stops playback if a music service was already running

        Controller.CURRENTLY_PLAYING = playSong;//we set the CURRENTLY_PLAYING SONG
        URL = useURL(query_url, use_song_name(CURRENTLY_PLAYING));//preparing an Http query for requesting the song

        //Records playing is song is unrepeated
        if (!is_repeat) {
            CommonElements.songPlayed(playSong);
        }

        mp3player = null;//emptying the mp3player

        media = new Media(URL);
        mp3player = new MediaPlayer(media);//setting up the mp3 player with the URL of the requested song

        mediaview.setMediaPlayer(mp3player);//GUI of media player is set here

        mp3player.setAutoPlay(true);//media started playing

        songtxt.setText(file.getName());//setting the song name heading
        createEqBands();//creating equalizer bands
        song_properties(mp3player);//setting up song's properties
    }

    /**
     * @param actionEvent Add song to queue event. Most probably, it will never be used insied media palyer.
     */
    public void addSongsToQueue(ActionEvent actionEvent) {
    }


    /**
     * This function puts the queue pane and and Equalizer pane behind media pane
     */
    void showMainPane(){
        equalizerPane.setVisible(false);
        mainPane.setVisible(true);
        queuePane.setVisible(false);
    }

    /**
     * Repeats the playback of the currently playing song. (NOT QUEUE)
     * @throws InterruptedException
     */
    void repeat_song() throws InterruptedException {
        startStreaming(CURRENTLY_PLAYING, Duration.millis(0));
    }
    /**
     * Stop playback if song is playing
     */
    private void stopPlayback() {
        if (mp3player != null)
            mp3player.stop();
    }

    /**
     * This function reset the seekPosition to zero
     */
    private void ResetSeekPosition() {
        seekPosition = Duration.millis(0);
    }

    /**
     * function to load the queue list view in the application Graphical Interface
     */
    void load_Queue() {
        queueListView.setItems((ObservableList<String>) Song_Queue.nameQueue);
    }

    /**
     * this function sets the properties of the song.
     * the Properties being set are-----
     * Total Duration,
     * Elapsed Duration,
     * Volume Slider to 30% (for now),
     * mp3seeker minimum,
     * mp3 seeker maximum,
     * mp3 seeker Current position,
     * mp3 seek on mouse event
     * And Lyrics synced with the music
     */
    public void song_properties(MediaPlayer mp3player) {

        //here we start the thread for downloading SRT files if they do not already exist on user side
        File srtpresent = new File(BUFFER_FOLDER+CURRENTLY_PLAYING+".srt");
        if(!srtpresent.exists()){
            downloadSRT srt = new downloadSRT(CURRENTLY_PLAYING);
            srt.start();//downloadSRT thread starts
        }

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
                elapsedTime.setText(formatDuration(elapsedDuration));
                totalTime.setText(formatDuration(totalDuration));
                
                /*
                 * we calculate the elapsed duration slightly ahead of the total duration time to confirm our success
                 * before the playback stops
                 */
                if(totalDuration.lessThan(elapsedDuration.add(new Duration(50)))) {
                    if(is_repeat){ //repeating the song of @is_repeat is True.
                        try { repeat_song(); } catch (InterruptedException e) { e.printStackTrace();}
                    }
                    else if(is_shuffle) {
                        try { play_shuffle(); } catch  (InterruptedException e) { e.printStackTrace(); }
                    }
                    else{ // else playing the next song in the queue
                        try { play_next(); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
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
                songtxt.setText("\""+Title+"\""+"   from   "+Album+"   by   "+Artist);
                metadata_song_name=Title;
            }
        });

        //Volume Slider is set to a specific amount here
        volumeslider.setValue(mp3player.getVolume() * 30);

        /*
         * Here we set up the seekbar's min and max Value
         */
        mp3player.setOnReady(new Runnable() {
            @Override
            public void run() {
                mp3seeker.setMin(0);
                mp3seeker.setMax(mp3player.getMedia().getDuration().toSeconds());
                mp3seeker.setValue(0);
            }
        });

        /*
         * Here we dynamically change the seekbar's position as the song progresses
         */
        mp3player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                Duration d = mp3player.getCurrentTime();
                mp3seeker.setValue(d.toSeconds());
            }
        });

        /*
         * Here we take the song duration on the point the mouse is pressed
         */
        mp3seeker.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mp3player.seek(Duration.seconds(mp3seeker.getValue()));// the value is sent to the next function
            }
        });

        /*
         * Here wee seek to the mouse's position on the seekbar
         */
        mp3seeker.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (mp3seeker.isPressed()) {
                    double value = mp3seeker.getValue();
                    mp3player.seek(new Duration(value * 1000));
                }
            }
        });

        /*
         * Setting the lyrics and syncronizing it.
         */
        mp3player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                SRTInfo info = SRTReader.read(new File(BUFFER_FOLDER+CURRENTLY_PLAYING+".srt"));
                SRT[] test = new SRT[info.info.size()];
                for(int i=0;i<test.length;i++) {
                    test[i] = info.info.get(i);
                }
                String[] starttimeenhancedarray = new String[test.length];
                String[] endtimeenhancedarray = new String[test.length];
                for(int i=0;i<starttimeenhancedarray.length;i++) {
                    String starttime = String.valueOf(test[i].startTime);
                    starttimeenhancedarray[i] = starttime.substring(14,19);
                }
                for (int i=0;i<test.length;i++) {
                    Duration currentplayertime = mp3player.getCurrentTime();
                    if (starttimeenhancedarray[i].equals(formatDuration(currentplayertime))) {
                        lyricstxt.setText(String.valueOf(test[i].text));
                    }
                }
            }
        });
    }

    /**
     * This function creates equalizer bands for the mp3player
     */
    private void createEqBands() {
        final ObservableList<EqualizerBand> bands = mp3player.getAudioEqualizer().getBands();
        bands.clear();
        double min_gain = EqualizerBand.MIN_GAIN; // Max gain of Equalizer band is set.
        double max_gain = EqualizerBand.MAX_GAIN; // Max gain of Equalizer band is set.
        double mid_gain = (max_gain - min_gain) / 2; // setting mid range
        double freq = START_FREQ;
        /*
         * Setting the eq bands to a Cosine Curve
         */
        for (int j = 0; j < BAND_COUNT; j++) {
            double theta = (double) j / (double) (BAND_COUNT - 1) * (2 * Math.PI);
            double scale = 0.4 * (1 + Math.cos(theta));
            double gain = min_gain + mid_gain + (mid_gain * scale);
            bands.add(new EqualizerBand(freq, freq / 2, gain));
            if (freq == 64) {
                freq = (freq * 2) - 3;
                continue;
            }
            freq = freq * 2;
        }
        /*
         * Eq bands values set
         */
        equalizerSlider1.valueProperty().bindBidirectional(bands.get(0).gainProperty());
        equalizerSlider2.valueProperty().bindBidirectional(bands.get(1).gainProperty());
        equalizerSlider3.valueProperty().bindBidirectional(bands.get(2).gainProperty());
        equalizerSlider4.valueProperty().bindBidirectional(bands.get(3).gainProperty());
        equalizerSlider5.valueProperty().bindBidirectional(bands.get(4).gainProperty());
        equalizerSlider6.valueProperty().bindBidirectional(bands.get(5).gainProperty());
        equalizerSlider7.valueProperty().bindBidirectional(bands.get(6).gainProperty());
        equalizerSlider8.valueProperty().bindBidirectional(bands.get(7).gainProperty());
        equalizerSlider9.valueProperty().bindBidirectional(bands.get(8).gainProperty());
        equalizerSlider10.valueProperty().bindBidirectional(bands.get(9).gainProperty());
        /*
         * Kinda resetting the whole eq p\setting to flat.
         * back to being simple. LOL XD
         */
        equalizerSlider1.valueProperty().set(-6);
        equalizerSlider2.valueProperty().set(-6);
        equalizerSlider3.valueProperty().set(-6);
        equalizerSlider4.valueProperty().set(-6);
        equalizerSlider5.valueProperty().set(-6);
        equalizerSlider6.valueProperty().set(-6);
        equalizerSlider7.valueProperty().set(-6);
        equalizerSlider8.valueProperty().set(-6);
        equalizerSlider9.valueProperty().set(-6);
        equalizerSlider10.valueProperty().set(-6);
    }

    /**
     * Plays the next song in the queue.
     * @throws InterruptedException
     */
    void play_next() throws InterruptedException {
        if( queue_playing && CURRENTLY_PLAYING_INDEX!= Song_Queue.queue.size()-1 ){
            CURRENTLY_PLAYING_INDEX++;                                   //Index increased by 1.
            String playnow=Song_Queue.queue.get(CURRENTLY_PLAYING_INDEX);//getting the next song in the queue
            startStreaming(playnow, Duration.millis(0));                 //started playback
            CURRENTLY_PLAYING=playnow;                                   //setting CURRENTLY_PLAYING
        }
        else{
            //message to send that we have reached the end of the queue
            JOptionPane.showMessageDialog(null,"Reached the End of the Queue");

        }
    }

    void play_shuffle() throws InterruptedException {
        Random random = new Random();
        int randomShuffle = Math.abs(random.nextInt());
        CURRENTLY_PLAYING_INDEX = randomShuffle%Song_Queue.queue.size();
        System.out.println(CURRENTLY_PLAYING_INDEX);
        String playnow=Song_Queue.queue.get(CURRENTLY_PLAYING_INDEX);//getting the next song in the queue
        startStreaming(playnow, Duration.millis(0));                 //started playback
        CURRENTLY_PLAYING=playnow;
    }

    /**
     * Plays the previous song in the queue.
     * @throws InterruptedException
     */
    void play_previous() throws InterruptedException {
        if( queue_playing && CURRENTLY_PLAYING_INDEX!=0 ){
            CURRENTLY_PLAYING_INDEX--;                                   //Index decreased by 1.
            String playnow=Song_Queue.queue.get(CURRENTLY_PLAYING_INDEX);//getting the previous song in the queue
            startStreaming(playnow, Duration.millis(0));                 //started playback
            CURRENTLY_PLAYING=playnow;                                   //setting CURRENTLY_PLAYING
        }
        else{
            //message to send that we are already at the beginning of the queue
            JOptionPane.showMessageDialog(null,"Already at the beginning of the Queue");
        }
    }

    /**
     * Loads the Song_Queue in GUI
     */
    void loadQueue() {
        queueListView.setItems((ObservableList<String>) Song_Queue.nameQueue);
    }


    /**
     * @param event Plays a song. There is not a lot to it.
     */
    @FXML
    void playTestSong(ActionEvent event) {
        try {
            System.out.println(seekPosition);
            startStreaming(CURRENTLY_PLAYING, Duration.millis(0));
            //startChecking();
            System.out.println(seekPosition);

        } catch (RuntimeException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param event Resume - Pause Button Click
     * @throws FileNotFoundException
     * Resume/Pause the media playback. P.S. that play pause button transition.
     */
    @FXML
    void resume_pause(ActionEvent event) throws FileNotFoundException {
        MediaPlayer.Status status = mp3player.getStatus();
        if (status == MediaPlayer.Status.PLAYING) {
            mp3player.pause();//pause playback
            playbtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION + "baseline_play_arrow_white_18dp.png"))));
        } else {
            mp3player.play();//resume playback
            mp3player.setRate(1);
            playbtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION + "baseline_pause_white_18dp.png"))));
        }
    }

    /**
     * @param event Skip ten button Click.
     *              Skips to 10 seconds ahead in the song.
     */
    @FXML
    void skipten(ActionEvent event) {
        double time = mp3player.getCurrentTime().toSeconds();
        time = time + 10;
        mp3player.seek(new Duration(time * 1000));
    }

    /**
     * @param event Go back button click.
     *              Goes back 10 seconds in the song.
     */
    @FXML
    void gobackten(ActionEvent event) {
        double time = mp3player.getCurrentTime().toSeconds();
        time = time - 10;
        mp3player.seek(new Duration(time * 1000));
    }

    /**0
     * @param event Exit button Click.
     *              Exits the player.
     */
    @FXML
    void exitFromPlayer(ActionEvent event) {
        int exit = JOptionPane.showConfirmDialog(null, "Are you sure that you want to exit ?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION);
        if (exit == 0) {
            System.exit(0);
        }
    }

    /**
     * @param event About button click.
     *              Shows about the media player.
     */
    @FXML
    void aboutPlayer(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "Simplistic Media Player created in JavaFx." + "\n" + "BY - Team Baracuda Viktara");
    }

    /**
     * @param event Mute button Click or Volume Zero.
     *              Shows graphic of audio muted. Sets volume to 0 if mute.
     *              30 if Unmute.
     */
    @FXML
    void mute(ActionEvent event) {
        double volumesliderValue = volumeslider.getValue();
        double volume = mp3player.getVolume();
        try {
            if (volumesliderValue > 0) {
                volumeslider.setValue(mp3player.getVolume() * 0);//mute. Volume =0;
                volumeBtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION + "ic_volume_off_128_28773.png"))));
            } else if (volumesliderValue == 0) {
                volumeslider.setValue(30);//Unmute. Volume = 30;
                volumeBtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION + "ic_volume_up_128_28772.png"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param event Repeat Button Click.
     * @throws FileNotFoundException
     * This function sets @is_repeat value to true if repeat is enabled and false if repeat is disabled.
     */
    @FXML
    void repeat(ActionEvent event) throws FileNotFoundException {
        if (!is_repeat) {
            repeatBtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION + "baseline_repeat_green_18dp.png"))));
            is_repeat=true;

        } else {
            repeatBtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION + "baseline_repeat_white_18dp.png"))));
            is_repeat=false;
        }
    }

    @FXML
    void shuffle(ActionEvent event) throws FileNotFoundException {
        if (!is_shuffle) {
            shuffleBtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION + "baseline_shuffle_green_18dp.png"))));
            is_shuffle=true;

        } else {
            shuffleBtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION + "baseline_shuffle_white_18dp.png"))));
            is_shuffle=false;
        }
    }

    /**
     * @param event - Show Equalizer butoon click.
     *  Hides media pane and queue pane behind Equalizer pane.
     */
    @FXML
    void showEqualizerPane(ActionEvent event) {
        equalizerPane.setVisible(true);
        mainPane.setVisible(false);
        queuePane.setVisible(false);
    }

    /**
     * @param event -  Exit Equalizer button click.
     *  This function puts the queue pane and and Equalizer pane behind media pane.
     */
    @FXML
    void exitEqualizerPane(ActionEvent event) {
        equalizerPane.setVisible(false);
        mainPane.setVisible(true);
        queuePane.setVisible(false);
    }

    /**
     * @param event Reset Equalizer Button.
     *              Set all Eq band values to mid position
     */
    @FXML
    void resetFreqBands(ActionEvent event) {
        equalizerSlider1.valueProperty().set(-6);
        equalizerSlider2.valueProperty().set(-6);
        equalizerSlider3.valueProperty().set(-6);
        equalizerSlider4.valueProperty().set(-6);
        equalizerSlider5.valueProperty().set(-6);
        equalizerSlider6.valueProperty().set(-6);
        equalizerSlider7.valueProperty().set(-6);
        equalizerSlider8.valueProperty().set(-6);
        equalizerSlider9.valueProperty().set(-6);
        equalizerSlider10.valueProperty().set(-6);
    }

    /**
     * @param actionEvent Click "Save Currently Playing" Button.
     * @throws IOException
     * Save the song the user s Currently listening in the @DESTINATION_DOWNLOAD_FOLDER
     * and that song has been made inaccessable to the user by manipulating Songs byte Data.
     */
    @FXML
    public void save_song(ActionEvent actionEvent) throws IOException {
        JOptionPane.showMessageDialog(null,"Current Song is being downloaded in "+DESTINATION_DOWNLOAD_FOLDER);
        String save_as=null;
        //save_as = functions.reverse(metadata_song_name);
        //System.out.println(save_as);
        //new Thread(download_song).start(); // A thread that downloads song in Background without disturbing the GUI.
        DownloadSong download_song = new DownloadSong(CURRENTLY_PLAYING,Song_Queue.nameQueue.get(CURRENTLY_PLAYING_INDEX));
        download_song.start();
    }

    /**
     * @param event Show Queue Pane Button Click. Shows the Queue Pane
     */
    @FXML
    void showViewQueuePane(ActionEvent event) {
        queuePane.setVisible(true);
        equalizerPane.setVisible(false);
        mainPane.setVisible(false);
        queueAddSongBtn.setVisible(false);
        queueDeleteSongBtn.setVisible(false);
        queueClearQueueBtn.setVisible(false);
    }

    /**
     * @param event Exit queue Pane buttopn Click. Exit's or hide's queue.
     */
    @FXML
    void exitQueuePane(ActionEvent event) {
        queuePane.setVisible(false);
        mainPane.setVisible(true);
    }

    /**
     * @param event Modify queue button Click. Modifies queue.
     */
    @FXML
    void modifyExistingQueue(ActionEvent event) {
        queuePane.setVisible(true);
        equalizerPane.setVisible(false);
        mainPane.setVisible(false);
        queueAddSongBtn.setVisible(true);
        queueDeleteSongBtn.setVisible(true);
        queueClearQueueBtn.setVisible(true);
    }

    /**
     * @param event Clear queue Button Click.
     * @throws FileNotFoundException
     * This function clears the currently playing Queue.
     */
    @FXML
    void clearQueue(ActionEvent event) throws FileNotFoundException {
        try {
            Song_Queue.queue.clear();  //cleared the queue
            Song_Queue.nameQueue.clear();
            queueListView.setItems(Song_Queue.nameQueue);
            OpenQueuebtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/add-to-queue-button_icon-icons.com_72877.png"))));
        }catch (UnsupportedOperationException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * @param event Delete song from queue option. Removes the selected song from the queue.
     */
    @FXML
    void deleteParticularSong(ActionEvent event) {
        final int selectedIdx = queueListView.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
            String itemToRemove = String.valueOf(queueListView.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * @param actionEvent Play Ampify Downloaded songs Button Click.
     * @throws IOException
     * This function convert the presently inaccessable song to the user and let's him play it until he wants.
     * The temprorily accessable song is stored in @song_buffer_location is a .ampify extension with proper mp3 media
     * encoding and that file is deleted when the user exits the media player (for now, that seams reasonable)
     */
    @FXML
    public void play_downloaded_song(ActionEvent actionEvent) throws IOException {

        /*
         * The file chooser part will be removed shortly.
         */
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Ampify Song to Load");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Downloaded Ampify Music Files", "*.amp"));
        File file = fc.showOpenDialog(null);

        stopPlayback();                                             // stop playback. if any.

        String filepath = file.getAbsolutePath();                   //getiing file path
        filepath = filepath.replace("\\", "/");
        Media media = null;
        Download_Encrypt_Decrypt.decrypt_downloaded_song(filepath); // decrypting the downloaded song
        try {
            media = new Media(new File(song_buffer_location).toURI().toURL().toString());//loaded the media
        } catch (MalformedURLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Malformed URL!");
        }
        String filename = file.getName();                          //setting up the song name text field
        int filelastindex = filename.length() - 1;

        /*
         * Starting media playback
         */
        mp3player = new MediaPlayer(media);
        mediaview.setMediaPlayer(mp3player);
        mp3player.setAutoPlay(true);
        songtxt.setText(file.getName());
        createEqBands();                                           //Creating Eq. Bands
        song_properties(mp3player);                                //song properties set.
    }

    // Playing local songs.
    @FXML
    public void openLocalSongMenu(ActionEvent actionEvent) {

        /*
         * The file chooser part will be removed shortly.
         */
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Ampify Song to Load");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Downloaded Ampify Music Files", "*.mp3","*.mp4"));
        File file = fc.showOpenDialog(null);

        stopPlayback();                                             // stop playback. if any.

        String filepath = file.getAbsolutePath();                   //getiing file path

        filepath = filepath.replace("\\", "/");
        Media media = null;
        try {
            media = new Media(new File(filepath).toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Malformed URL!");
        }

        String filename = file.getName();
        int filelastindex = filename.length() - 1;

        /*
         * Starting media playback
         */
        mp3player = new MediaPlayer(media);
        mediaview.setMediaPlayer(mp3player);
        mp3player.setAutoPlay(true);


        songtxt.setText(file.getName());
        createEqBands();                                           //Creating Eq. Bands
        song_properties(mp3player);                                //song properties set.
    }

    /**
     * @param event Queue Move up button click.
     *              Moves the selected song up in the queue.
     */
    @FXML
    void moveSongUp(ActionEvent event) {
        int listviewindex = queueListView.getSelectionModel().getSelectedIndex();
        if(listviewindex == 0) {
            JOptionPane.showMessageDialog(null,"Cannot be moved up further !");
        }
        else {
            Song_Queue.swapSongElements(listviewindex-1, listviewindex);//swapping occurs here
            queueListView.setItems(Song_Queue.nameQueue);//Show the reset queue again
            queueListView.getSelectionModel().select(listviewindex-1);
        }
    }

    /**
     * @param event Queue Move down button click.
     *              Moves the selected song down in the queue.
     */
    @FXML
    void moveSongDown(ActionEvent event) {
        int listviewindex = queueListView.getSelectionModel().getSelectedIndex();
        if(listviewindex == (Song_Queue.queue.size()-1)) {
            JOptionPane.showMessageDialog(null,"Cannot be moved down further !");
        }
        else {
            Song_Queue.swapSongElements(listviewindex,listviewindex+1);//swapping occurs here
            queueListView.setItems(Song_Queue.nameQueue);//Show the reset queue again
            queueListView.getSelectionModel().select(listviewindex+1);
        }
    }

    /**
     * @param event Delete particular Song1 button. Deletes a selected song from the queue.
     */
    @FXML
    void deleteparticularSong1(ActionEvent event) {
        int selectedIdx = queueListView.getSelectionModel().getSelectedIndex();
        Song_Queue.removeFromQueue(selectedIdx, selectedIdx+1);
        queueListView.setItems(Song_Queue.nameQueue);
    }

    /**
     * @param url
     * @param resourceBundle
     * Thid Initializes and sets all button images on their respective bottons and sliders are set here.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            /*
             * Setting Images on buttons
             */
            playbtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION+"baseline_pause_white_18dp.png"))));
            playprevbtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION+"Button-Backward-icon.png"))));
            playnextbtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION+"Button-Forward-icon.png"))));
            volumeBtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION+"ic_volume_up_128_28772.png"))));
            previousSongBtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION+"baseline_skip_previous_white_18dp.png"))));
            nextSongBtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION+"baseline_skip_next_white_18dp.png"))));
            repeatBtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION+"baseline_repeat_white_18dp.png"))));
            shuffleBtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION+"baseline_shuffle_white_18dp.png"))));
            OpenQueuebtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION+"add-to-queue-button_icon-icons.com_72877.png"))));
            moveUpQueueBtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION+"arrow-up-button-icon.png"))));
            moveDownQueueBtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION+"arrow-down-button-icon.png"))));

            /*
             * Setting ID's on buttons
             */
            playprevbtn.setId("playprevbtn");
            playbtn.setId("playbtn");
            playnextbtn.setId("playnextbtn");

            /*
             * Showing only Main media pane
             */
            showMainPane();

            /*
             * Setting Elaped time and total time to zero minutes zero seconds 00:00
             */
            elapsedTime.setText("00:00");
            totalTime.setText("00:00");

            //queue is loaded here
            loadQueue();

        }catch(Exception e)
        {
            e.printStackTrace();
        }

        /*
         * Volume Slider is set here. Mutes button display if sound output is zero.
         */
        volumeslider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mp3player.setVolume(volumeslider.getValue()/100);//setting volume levels as specified.
                if (volumeslider.getValue() == 0) {
                    try {
                        volumeBtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION+"ic_volume_off_128_28773.png"))));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else if (volumeslider.getValue() > 0) {
                    try {
                        volumeBtn.setGraphic(new ImageView(new Image(new FileInputStream(ASSETS_LOCATION+"ic_volume_up_128_28772.png"))));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * @param actionEvent Play Queue button Click. Starts playing the queue from the First song in the Queue.
     * @throws InterruptedException
     */
    @FXML
    public void playQueue(ActionEvent actionEvent) throws InterruptedException {
        CURRENTLY_PLAYING=Song_Queue.queue.get(0);//setting the Currently playing.
        CURRENTLY_PLAYING_INDEX =0;//Index set to Zero.
        startStreaming(CURRENTLY_PLAYING, Duration.millis(0));//started streamimg.
        queue_playing=true;//set the condition @queue_playing to true.
    }

    /**
     * Play next song button plays next song from the queue. LOL XD
     * @throws InterruptedException
     */
    @FXML
    void PlayNextSong() throws InterruptedException {
        play_next();
    }

    /**
     * Play previous song button plays previous song from the queue. AND YOU KNEW THAT!!
     * @throws InterruptedException
     */
    @FXML
    void  PlayPreviousSong() throws InterruptedException {
        play_previous();
    }

    /*
     * This was previously being used to download songs. But because of bugs it's limitations, it has now been scrapped and now we
     *                                              are using a better Service Approach
     *
     *
     *
     *                      //A task @download_song to download the @CURRENTLY_PLAYING song in @DESTINATION_FOLDER
    Task download_song = new Task() {
        @Override
        protected Void call() throws Exception {
            Download_Encrypt_Decrypt.encrypt_and_download_song(CURRENTLY_PLAYING,metadata_song_name);//This method decrypt the song
            return null;
        }

    };
     */

    /**
     * @param duration Input is in the form of milliseconds.
     * @return String in the form of the Duration in format Minutes:Seconds.
     */
    private String formatDuration(Duration duration) {
        double millis = duration.toMillis();
        int seconds = (int) ((millis / 1000) % 60);
        int minutes = (int) (millis / (1000 * 60));
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Triggers file chooser
     */
    void triggerdialog() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Song to Load");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Music Files", "*.mp3", "*.mp4"));
        File file = fc.showOpenDialog(null);
        String filepath = file.getAbsolutePath();
        filepath = filepath.replace("\\", "/");
        Media media = null;
        try {
            media = new Media(new File(filepath).toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mp3player = new MediaPlayer(media);
        mediaview.setMediaPlayer(mp3player);
        mp3player.setAutoPlay(true);
        songtxt.setText(file.getName());
        createEqBands();
    }

    /*
     *                      This was being used when online play back was not working perfectly.
     *                                 Now, there is probably no need for it.
     *                                 Online Streaming is working just fine.
     *
     Task streambuffer = new Task() {
    @Override
    protected synchronized Void call() throws Exception {

    return null;
    }

    @Override protected void succeeded() {
    super.succeeded();
    updateMessage("Done!");
    }

    @Override protected void cancelled() {
    super.cancelled();
    updateMessage("Cancelled!");
    }

    @Override protected void failed() {
    super.failed();
    updateMessage("Failed!");
    }
    };
     */

}