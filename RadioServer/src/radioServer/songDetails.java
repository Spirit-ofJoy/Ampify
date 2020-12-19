package radioServer;

import org.tritonus.share.sampled.file.TAudioFileFormat;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class songDetails {

    /**
     * @param file the song that we want to know the total duration value of
     * @return the duration of the selected song in file variable
     * @throws UnsupportedAudioFileException When audio is not supported
     * @throws IOException when file is not found
     */
    public static int getDuration (File file) throws UnsupportedAudioFileException, IOException {
        int milli;
        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
        if (fileFormat instanceof TAudioFileFormat) {
            Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
            String key = "duration";
            Long microseconds = (Long) properties.get(key);
            milli = (int) (microseconds / 1000);
            return milli;
        } else {
            throw new UnsupportedAudioFileException();
        }
    }

    /**
     * @param file The music file that you want to find the info about
     * @return the Title of the music file
     * @throws UnsupportedAudioFileException
     */
    public static String getTitle (File file) throws UnsupportedAudioFileException {

        AudioFileFormat fileFormat = null;
        String title = null;

        try {
            fileFormat = AudioSystem.getAudioFileFormat(file);
        } catch (IOException e) {
            System.out.println("Error in finding a file, hence skipping it");
        }

        try {
            if (fileFormat instanceof TAudioFileFormat) {
                Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
                String key = "title";
                title = (String) properties.get(key);

            } else {
                throw new UnsupportedAudioFileException();
            }
        } catch ( UnsupportedAudioFileException e){
            System.out.println("File is not supported");
        }
        return title;
    }

    /**
     * @param file The music file that you want to find the info about
     * @return the Album of the music file
     * @throws UnsupportedAudioFileException
     */
    public static String getAlbum (File file) throws UnsupportedAudioFileException {

        AudioFileFormat fileFormat = null;
        String album = null;

        try {
            fileFormat = AudioSystem.getAudioFileFormat(file);
        } catch (IOException e) {
            System.out.println("Error in finding a file, hence skipping it");
        }

        try {
            if (fileFormat instanceof TAudioFileFormat) {
                Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
                String key = "album";
                album = (String) properties.get(key);

            } else {
                throw new UnsupportedAudioFileException();
            }
        } catch ( UnsupportedAudioFileException e){
            System.out.println("File is not supported");
        }
        return album;
    }

    /**
     * @param file The music file that you want to find the info about
     * @return the Title of the music file
     * @throws UnsupportedAudioFileException
     */
    public static String getArtist (File file) throws UnsupportedAudioFileException {

        AudioFileFormat fileFormat = null;
        String artist = null;

        try {
            fileFormat = AudioSystem.getAudioFileFormat(file);
        } catch (IOException e) {
            System.out.println("Error in finding a file, hence skipping it");
        }

        try {
            if (fileFormat instanceof TAudioFileFormat) {
                Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
                String key = "artist";
                artist = (String) properties.get(key);

            } else {
                throw new UnsupportedAudioFileException();
            }
        } catch ( UnsupportedAudioFileException e){
            System.out.println("File is not supported");
        }
        return artist;
    }

}