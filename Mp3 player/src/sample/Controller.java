package sample;

import com.jfoenix.controls.JFXSlider;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.InputStream;
import javafx.concurrent.Task;
import javax.sound.sampled.AudioInputStream;
import java.lang.Object;
import sample.Constants;

import static sample.Constants.*;
import static sample.functions.*;

public class Controller<e> implements Initializable{

    @FXML
    public MediaPlayer mp3player = null;

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
    private Button equalizerFreqResetBtn;

    @FXML
    private ImageView musicIcon;

    @FXML
    private AnchorPane mediaPane;

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

    boolean equalizerState=false;
    final double START_FREQ = 32.0;
    final int BAND_COUNT = 10;
    int counter = 1;
    private String URL;
    public static String SONG_NAME="S#007";

    public void song_properties(){
        mp3player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                Duration elapsedduration = mp3player.getCurrentTime();
                Duration totalduration = mp3player.getTotalDuration();Duration.millis(158000);
                System.out.println(totalduration);System.out.println();

                elapsedTime.setText(formatDuration(elapsedduration));
                totalTime.setText(formatDuration(totalduration));
            }
        });

        volumeslider.setValue(mp3player.getVolume() * 100);

        mp3player.setOnReady(new Runnable() {
            @Override
            public void run() {
                mp3seeker.setMin(0);
                mp3seeker.setMax(Duration.millis(158000).toSeconds());
                //mp3seeker.setMax(mp3player.getMedia().getDuration().toSeconds());
                mp3seeker.setValue(0);
            }
        });

        mp3player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                Duration d = mp3player.getCurrentTime();
                mp3seeker.setValue(d.toSeconds());
            }
        });

        mp3seeker.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mp3player.seek(Duration.seconds(mp3seeker.getValue()));
            }
        });

        mp3seeker.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if(mp3seeker.isPressed()) {
                    double value = mp3seeker.getValue();
                    mp3player.seek(new Duration(value*1000));
                }
            }
        });
    }

    @FXML
    void openSongMenu(ActionEvent event) {
        try {
            if (mp3player == null && counter == 1) {
                FileChooser fc = new FileChooser();
                fc.setTitle("Choose Song to Load");
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Music Files", "*.mp3", "*.mp4"));
                File file = fc.showOpenDialog(null);
                //String filepath = file.getAbsolutePath();
                String filepath;

                counter++;
                Media media = null;
                //media = new Media(new File(filepath).toURI().toURL().toString());
                URL = useURL(query_url, use_song_name(SONG_NAME));
                new Thread(streambuffer).start();
                filepath = sample.Constants.song_buffer_location;
                //filepath = filepath.replace("\\", "/");
                media = new Media(new File(filepath).toURI().toString());
                String filename = file.getName();
                int filelastindex = filename.length() - 1;
                String formattype = filename.substring(filelastindex - 2, filelastindex + 1);
                mp3player = new MediaPlayer(media);
                mediaview.setMediaPlayer(mp3player);
                mp3player.setAutoPlay(true);
                songtxt.setText(file.getName());
                createEqBands();
            }
            //tree set
            else if (counter > 1) {
                FileChooser fc = new FileChooser();
                fc.setTitle("Choose Song to Load");
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Music Files", "*.mp3", "*.mp4"));
                File file = fc.showOpenDialog(null);
                if(mp3player!=null)
                        mp3player.stop();
                String filepath = file.getAbsolutePath();
                filepath = filepath.replace("\\", "/");
                counter++;
                Media media = null;
                try {
                    media = new Media(new File(filepath).toURI().toURL().toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Malformed URL!");
                }
                String filename = file.getName();
                int filelastindex = filename.length() - 1;
                String formattype = filename.substring(filelastindex - 2, filelastindex + 1);
                mp3player = new MediaPlayer(media);
                mediaview.setMediaPlayer(mp3player);
                mp3player.setAutoPlay(true);
                songtxt.setText(file.getName());
                createEqBands();
            }
            song_properties();
        }
        catch(RuntimeException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    void playsong(ActionEvent event) throws FileNotFoundException {
        MediaPlayer.Status status = mp3player.getStatus();
        if(status == MediaPlayer.Status.PLAYING)
        {
            mp3player.pause();
            playbtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/baseline_play_arrow_white_18dp.png"))));
        }
        else
        {
            mp3player.play();
            mp3player.setRate(1);
            playbtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/baseline_pause_white_18dp.png"))));
        }
    }

    @FXML
    void skipten(ActionEvent event) {
        double time = mp3player.getCurrentTime().toSeconds();
        time = time + 10;
        mp3player.seek(new Duration(time*1000));
    }

    @FXML
    void gobackten(ActionEvent event) {
    double time = mp3player.getCurrentTime().toSeconds();
    time = time - 10;
    mp3player.seek(new Duration(time*1000));
    }

    @FXML
    void exitFromPlayer(ActionEvent event) {
        int exit = JOptionPane.showConfirmDialog(null,"Are you sure that you want to exit ?","Exit",JOptionPane.YES_NO_CANCEL_OPTION);
        if (exit == 0){
            System.exit(0);
        }
    }

    @FXML
    void aboutPlayer(ActionEvent event) {
        JOptionPane.showMessageDialog(null,"Simplistic Media Player created in JavaFx."+"\n"+"Creator : Sayed Zameer Qasim");
    }

    @FXML
    void mute(ActionEvent event) {
        double volumesliderValue = volumeslider.getValue();
        double volume = mp3player.getVolume();
        try {
            if (volumesliderValue > 0) {
                volumeslider.setValue(mp3player.getVolume() * 0);
                volumeBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/ic_volume_off_128_28773.png"))));
            }
            else if (volumesliderValue == 0) {
                volumeslider.setValue(100);
                volumeBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/ic_volume_up_128_28772.png"))));
            }
            } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void repeat(ActionEvent event) throws FileNotFoundException {
        int cycles = mp3player.getCycleCount();
        if(cycles != MediaPlayer.INDEFINITE) {
            repeatBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/baseline_repeat_green_18dp.png"))));
            mp3player.setCycleCount(MediaPlayer.INDEFINITE);
            mp3player.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mp3seeker.setValue(0);
                    mp3player.play();
                }
            });
        }
        else {
            repeatBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/baseline_repeat_white_18dp.png"))));
            mp3player.setCycleCount(0);
        }
    }

    @FXML
    void showEqualizerPane(ActionEvent event) {
        mainPane.setVisible(false);
        equalizerPane.setVisible(true);

    }

    @FXML
    void exitEqualizerPane(ActionEvent event) {
        equalizerPane.setVisible(false);
        mainPane.setVisible(true);
    }
    private String formatDuration(Duration duration) {
        double millis = duration.toMillis();
        int seconds = (int) ((millis/1000) % 60);
        int minutes = (int) (millis/(1000 * 60));
        return String.format("%02d:%02d",minutes,seconds);
    }
    private void createEqBands() {
        final ObservableList<EqualizerBand> bands = mp3player.getAudioEqualizer().getBands();
        bands.clear();
        double min_gain = EqualizerBand.MIN_GAIN;
        double max_gain = EqualizerBand.MAX_GAIN;
        double mid_gain = (max_gain - min_gain)/2;
        double freq = START_FREQ;
        for (int j=0;j<BAND_COUNT;j++) {
            double theta = (double)j/(double)(BAND_COUNT-1) * (2*Math.PI);
            double scale = 0.4 * (1+Math.cos(theta));
            double gain = min_gain + mid_gain + (mid_gain * scale);
            bands.add(new EqualizerBand(freq,freq/2,gain));
            if (freq == 64){
                freq = (freq*2)-3;
                continue;
            }
            freq = freq * 2;
        }
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
    }

    @FXML
    void resetFreqBands(ActionEvent event) {
        equalizerSlider1.valueProperty().setValue(-6);
    }
    void openFileSelector() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Song to Load");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Music Files", "*.mp3","*.mp4"));
        File file = fc.showOpenDialog(null);
    }
    void triggerdialog() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Song to Load");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Music Files", "*.mp3","*.mp4"));
        File file = fc.showOpenDialog(null);
        String filepath = file.getAbsolutePath();
        filepath = filepath.replace("\\", "/");
        Media media = null;
        try {
            media = new Media(new File(filepath).toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String filename = file.getName();
        int filelastindex = filename.length() - 1;
        String formattype = filename.substring(filelastindex - 2, filelastindex + 1);
        mp3player = new MediaPlayer(media);
        mediaview.setMediaPlayer(mp3player);
        mp3player.setAutoPlay(true);
        songtxt.setText(file.getName());
        createEqBands();
    }

    @FXML
    public void save_song(ActionEvent actionEvent) throws IOException {
        JOptionPane.showMessageDialog(null,"Current Song is being downloaded in "+DESTINATION_DOWNLOAD_FOLDER);
        new Thread(download_song).start();
    }

    @FXML
    public void play_downloaded_song(ActionEvent actionEvent) throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Ampify Song to Load");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Downloaded Ampify Music Files", "*.amp"));
        File file = fc.showOpenDialog(null);
        if(mp3player!=null)
            mp3player.stop();
        String filepath = file.getAbsolutePath();
        filepath = filepath.replace("\\", "/");
        counter++;
        Media media = null;
        Download_Encrypt_Decrypt.decrypt_downloaded_song(filepath);
        try {
            media = new Media(new File(song_buffer_location).toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Malformed URL!");
        }
        String filename = file.getName();
        int filelastindex = filename.length() - 1;
        String formattype = filename.substring(filelastindex - 2, filelastindex + 1);
        mp3player = new MediaPlayer(media);
        mediaview.setMediaPlayer(mp3player);
        mp3player.setAutoPlay(true);
        songtxt.setText(file.getName());
        createEqBands();
        song_properties();
    }

    @FXML
    public void openoffineMenu(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose Ampify Song to Load");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Downloaded Ampify Music Files", "*.amp"));
        File file = fc.showOpenDialog(null);
        if(mp3player!=null)
            mp3player.stop();
        String filepath = file.getAbsolutePath();
        filepath = filepath.replace("\\", "/");
        counter++;
        Media media = null;
        try {
            media = new Media(new File(filepath).toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Malformed URL!");
        }
        String filename = file.getName();
        int filelastindex = filename.length() - 1;
        String formattype = filename.substring(filelastindex - 2, filelastindex + 1);
        mp3player = new MediaPlayer(media);
        mediaview.setMediaPlayer(mp3player);
        mp3player.setAutoPlay(true);
        songtxt.setText(file.getName());
        createEqBands();
        song_properties();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            playbtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/baseline_pause_white_18dp.png"))));
            playprevbtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/Button-Backward-icon.png"))));
            playnextbtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/Button-Forward-icon.png"))));
            volumeBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/ic_volume_up_128_28772.png"))));
            previousSongBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/baseline_skip_previous_white_18dp.png"))));
            nextSongBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/baseline_skip_next_white_18dp.png"))));
            repeatBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/baseline_repeat_white_18dp.png"))));
            playprevbtn.setId("playprevbtn");
            playbtn.setId("playbtn");
            playnextbtn.setId("playnextbtn");
            equalizerPane.setVisible(false);
            mainPane.setVisible(true);
            elapsedTime.setText("00:00");
            totalTime.setText("00:00");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        volumeslider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mp3player.setVolume(volumeslider.getValue()/100);
                if (volumeslider.getValue() == 0) {
                    try {
                        volumeBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/ic_volume_off_128_28773.png"))));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else if (volumeslider.getValue() > 0) {
                    try {
                        volumeBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/sample/assets/ic_volume_up_128_28772.png"))));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //for creating the buffer of the song
    Task streambuffer = new Task() {

        @Override
        protected synchronized Void call() throws Exception {
            //here were are creating a buffer of the media
            URLConnection conn = new URL(URL).openConnection();
            InputStream is = conn.getInputStream();
            OutputStream outstream = new FileOutputStream(new File(song_buffer_location));
            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) > 0) {
                outstream.write(buffer, 0, len);
            }
            outstream.close();  //created buffer
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

    //A task for downloading the song
    Task download_song = new Task() {
        @Override
        protected Void call() throws Exception {
            Download_Encrypt_Decrypt.encrypt_and_download_song(SONG_NAME);
            return null;
        }

    };


}

//private syncronized void LOGSDIUGB()
//