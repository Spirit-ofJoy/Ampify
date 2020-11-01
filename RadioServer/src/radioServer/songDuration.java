package radioServer;

import org.tritonus.share.sampled.file.TAudioFileFormat;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class songDuration {


    /**
     * REQUIRES TRITONUS SHARE LIBRARY
     *          JLAYER LIBRARY
     *          MP3SPI LIBRARY
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
}