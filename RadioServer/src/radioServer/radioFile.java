package radioServer;

import java.io.*;

public class radioFile {

    //fixing some constants
    final static char separator=';';
    final static String ASSETS_LOCATION = "assets/songs/";
    final static long LeeWay = 5000;


    /**
     * @param create_songs_string This function creates a radio file out of the song string it is given
     *                     in the folder assets/songs
     */
    public static void create_radio_file(String create_songs_string){
        //storing the bytes of the string in byte array
        byte[] radio_file_bytes = create_songs_string.getBytes();
        //writing the bytes into a file that can be read later
        try(OutputStream outputStream = new FileOutputStream(ASSETS_LOCATION+"../radio/radio1")) {
            outputStream.write(radio_file_bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param file Takes the radio file
     * @return the String of the radio file
     * @throws IOException when the requested radio file is not found
     */
    public static String read_radio_file(File file) throws IOException {
        //Creating an InputStream object
        InputStream inputStream = new FileInputStream(file);
        //creating an InputStreamReader object
        InputStreamReader isReader = new InputStreamReader(inputStream);
        //Creating a character array
        char charArray[] = new char[(int) file.length()];
        //Reading the contents of the reader
        isReader.read(charArray);
        //Converting character array to a String
        String contents = new String(charArray);
        return contents;
    }

    /**
     * @param radio_file String of the radio file through @read_radio_file(File file)
     * @param index index where the Currently playing details begins
     * @return Currently playing song on radio
     */
    public static String current_song(String radio_file, int index){
        return radio_file.substring(index+14,next_separator(radio_file,index));
    }

    /**
     * @param radio_file String of the radio file through @read_radio_file(File file)
     * @param index index where the Currently playing details begins
     * @return the starting time of the song on radio
     */
    public static String get_start_time(String radio_file,int index){
        return radio_file.substring(index,index+13);
    }

    /**
     * @param compareThis Take one long parsable string
     * @param with take another long parsable string
     * @return boolean if compareThis>with
     */
    public static boolean GreaterThan(String compareThis,String with){
        if(compareThis.compareTo(with)>0)
            return true;
        else return false;
    }

    /**
     * @param radio_file String of the radio file through @read_radio_file(File file)
     * @param index index where the Currently playing details begins
     * @return the location of the next song detail separator, using while true
     */
    public static int next_separator(String radio_file,int index){
        byte[] radio_file_array = radio_file.getBytes();
        index++;
        while(true){
            if((char)radio_file_array[index]==separator)
                return index;
            index++;
        }
    }

    /**
     * @param radio_file String of the radio file through @read_radio_file(File file)
     * @param index index where the Currently playing details begins
     * @return the http response a client will get in the format "(long)TIME (mp3)TITLE"
     */
    public static String respond_by(String radio_file,int index){
        return radio_file.substring(index,next_separator(radio_file,index));
    }

    /**
     * @param radio_file String of the radio file through @read_radio_file(File file)
     * @return @respond_by(String radio_file,int index) or the responce the http client will get
     *         after going on the link of his radio file
     */
    public static String response_parser(String radio_file){
        //getting local time to compare the time starting time of the radio songs
        String local_time = String.valueOf((System.currentTimeMillis()));
        //file index locations for one song detail
        int separator_index=1;
        int next_separator=next_separator(radio_file,separator_index);

        while(true){
            /*
            System.out.println(get_start_time(radio_file,next_separator+1));
            System.out.println(GreaterThan(get_start_time(radio_file,next_separator+1),local_time));
             */
            //comparing the time of stream start and local time and responding if the time for song stream has come
            if(GreaterThan(get_start_time(radio_file,next_separator+1),local_time)){
                return respond_by(radio_file,separator_index);
            }
            //else change the index values of the separators
            else {
                separator_index = next_separator+1;
                next_separator = next_separator(radio_file,separator_index);
            }
        }
    }


}
