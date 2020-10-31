package ampify_player;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is responsible for reading an SRT file.
 *
 * @author Sayed Zameer Qasim
 */

public class SRTReader {

    private static class BufferedLineReader {
        private final BufferedReader reader;
        private long lineNumber;

        public BufferedLineReader(BufferedReader reader) {
            this.reader = reader;
        }

        public String readLine() throws IOException {
            String line = reader.readLine();
            lineNumber++;
            // remove the BOM
            if (line != null && lineNumber == 1) {
                return line.replace("\uFEFF", "");
            }
            return line;
        }

        public long getLineNumber() {
            return lineNumber;
        }
    }
    /**
     * Reads an SRT file and transforming it into SRT object.
     *
     * @param srtFile SRT file
     * @return the SRTInfo object
     * @throws ampify_player.InvalidSRTException thrown when the SRT file is invalid
     * @throws SRTReaderException thrown while reading SRT file
     */
    public static ampify_player.SRTInfo read(File srtFile) throws ampify_player.InvalidSRTException, SRTReaderException {
    if (!srtFile.exists()) {
            throw new SRTReaderException(srtFile.getAbsolutePath() + " does not exist");
        }
        if (!srtFile.isFile()) {
            throw new SRTReaderException(srtFile.getAbsolutePath() + " is not a regular file");
        }

        ampify_player.SRTInfo srtInfo = new ampify_player.SRTInfo();
        try (BufferedReader br = new BufferedReader(new FileReader(srtFile))) {
            BufferedLineReader reader = new BufferedLineReader(br);
            while (true) {
                srtInfo.add(parse(reader));
            }
        } catch (EOFException e) {
            // Do nothing
        } catch (IOException e) {
            throw new SRTReaderException(e);
        }

        return srtInfo;
    }

    public static SRT parse(BufferedLineReader reader) throws IOException, EOFException {
        String nString = reader.readLine();
        // ignore all empty lines
        while (nString != null && nString.isEmpty()) {
            nString = reader.readLine();
        }

        if (nString == null) {
            throw new EOFException();
        }

        int subtitleNumber = -1;
        try {
            subtitleNumber = Integer.parseInt(nString);
        } catch (NumberFormatException e) {
            throw new ampify_player.InvalidSRTException(
                    String.format(
                            "[Line: %d] %s has an invalid subtitle number",
                            reader.getLineNumber(),
                            nString));
        }

        String tString = reader.readLine();
        if (tString == null) {
            throw new ampify_player.InvalidSRTException(
                    String.format(
                            "[Line: %d] Start time and end time information is not present",
                            reader.getLineNumber()));
        }
        String[] times = tString.split(SRTTimeFormat.TIME_DELIMITER);
        if (times.length != 2) {
            throw new ampify_player.InvalidSRTException(
                    String.format(
                            "[Line: %d] %s needs to be separated with %s",
                            reader.getLineNumber(),
                            tString,
                            SRTTimeFormat.TIME_DELIMITER));
        }
        Date startTime = null;
        try {
            startTime = SRTTimeFormat.parse(times[0]);
        } catch (ParseException e) {
            throw new ampify_player.InvalidSRTException(String.format(
                    "[Line: %d] %s has an invalid start time format",
                    reader.getLineNumber(),
                    times[0]));
        }

        Date endTime = null;
        try {
            endTime = SRTTimeFormat.parse(times[1]);
        } catch (ParseException e) {
            throw new ampify_player.InvalidSRTException(String.format(
                    "[Line: %d] %s has an invalid end time format",
                    reader.getLineNumber(),
                    times[1]));
        }

        List<String> subtitleLines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                break;
            }
            subtitleLines.add(line);
        }

        if (subtitleLines.size() == 0) {
            throw new ampify_player.InvalidSRTException(String.format(
                    "[Line: %d] Missing subtitle text information",
                    reader.getLineNumber()));
        }

        return new SRT(subtitleNumber, startTime, endTime, subtitleLines);
    }
}

