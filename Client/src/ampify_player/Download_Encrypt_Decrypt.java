package ampify_player;
//mediaplayer
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.*;
import java.net.URLConnection;
import java.net.URL;
import static ampify_player.Constants.*;
import static ampify_player.functions.*;

public class Download_Encrypt_Decrypt {

    //here we download the songs and make them inaccessable to the users through other media players
    /**
     * @param song_name without song format as stored in the server ( as S#001 )
     * @param save_as this name is how the song will be save in the user file data. Easy for user and us to choose between the songs.
     * @throws IOException
     * save the specified song in the @DESTINATION_DOWNLOAD_FOLDER
     */
    public static void encrypt_and_download_song(String song_name, String save_as) throws IOException {

        String name_while_downloading =  use_song_name(song_name);
        String URL = useURL(query_url,name_while_downloading);
        URLConnection conn = new URL(URL).openConnection();
        InputStream is = conn.getInputStream();
        ByteArrayOutputStream downloaded_song = new ByteArrayOutputStream();
        //writing bytes on downloaded_song ByteArrayOutputStream
        byte[] buffer = new byte[4096];
        int len;
        while ((len = is.read(buffer)) > 0) {
            downloaded_song.write(buffer,0, len);
        }                                                                                         //downloaded the songs

        /*
        //saving pure song without making it inaccessable to the user
        try(OutputStream outputStream = new FileOutputStream(DESTINATION_DOWNLOAD_FOLDER)) {
            downloaded_song.writeTo(outputStream);
        }
         */

        //Here *Corruption* XD Starts (lol)
        byte song_bytes[] = downloaded_song.toByteArray();
        System.out.println("Converting the Song "+song_name+" Starts");

        byte temperory_swap_byte;
        //here we are alternate byte swaping and rendering the mp3 useless for other audio players
        for(int index = 0; index < song_bytes.length-1; index++) {
            temperory_swap_byte=song_bytes[index];
            song_bytes[index]=song_bytes[index+1];
            song_bytes[index+1]=temperory_swap_byte;
            index++;
        }

        //Here Corruption Ends :)
        System.out.println("Made the Song "+song_name+" inaccessable to the user");

        ByteArrayOutputStream encrypted_song = new ByteArrayOutputStream();

        for (byte song_byte : song_bytes) {
            encrypted_song.write(song_byte);
        }

        //writing the inaccessable file
        try(OutputStream outputStream = new FileOutputStream(DESTINATION_DOWNLOAD_FOLDER+save_as+FILE_EXTENSION)) {
            encrypted_song.writeTo(outputStream);
            outputStream.close();
        }
    }


    //here we decrypt the downloaded songs and make them accessable to the user temperorily

    /**
     *
     * @param SONG_PATH  you need to enter the song path here (COMPLETE PATH/RELATIVE PATH)
     *                   and it loads it in @song_buffer_location
     * @throws IOException
     */
    public static void decrypt_downloaded_song(String SONG_PATH) throws IOException {

        File file = new File(SONG_PATH);
        FileInputStream stream = new FileInputStream(file);                   // Opening a input stream to read the file
        ByteArrayOutputStream encrypted_downloaded_song = new ByteArrayOutputStream();       //for storing the byte data

        byte[] buff = new byte[1024];                           // Creating a small buffer to hold bytes as we read them
        int read = 0;

        while((read = stream.read(buff)) > 0) {                                           // sending bytes to the client
            encrypted_downloaded_song.write(buff, 0, read);
        }


        byte song_bytes[] = encrypted_downloaded_song.toByteArray();
        //For Uncorrupting the File or Making File Accessable to the User temperorily
        byte temperory_swap_byte;
        //we are alternate byte swaping again to recover the original song Information
        for(int index = 0; index < song_bytes.length-1; index++) {
            temperory_swap_byte=song_bytes[index];
            song_bytes[index]=song_bytes[index+1];
            song_bytes[index+1]=temperory_swap_byte;
            index++;
        }

        ByteArrayOutputStream decrypted = new ByteArrayOutputStream();

        for (byte song_byte : song_bytes) {
            decrypted.write(song_byte);
        }

        System.out.println("Made the Song "+file.getName()+" accessable to the user temperorily in .buffer.ampify");

        try(OutputStream outputStream = new FileOutputStream(song_buffer_location)) {
            decrypted.writeTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param song_name without song format as stored in the server ( as S#001 ) and the srt is downloaded in
     *                  @BUFFER_FOLDER
     * @throws IOException
     */
    public static void download_srt(String song_name) throws IOException {

        //setting up connection
        String name_while_downloading = use_song_name(song_name);
        String URL = query_url + name_while_downloading + ".srt";
        URLConnection conn = new URL(URL).openConnection();
        InputStream is = conn.getInputStream();
        ByteArrayOutputStream downloaded_srt = new ByteArrayOutputStream();
        //writing bytes on downloaded_srt ByteArrayOutputStream
        byte[] buffer = new byte[4096];
        int len;
        while ((len = is.read(buffer)) > 0) {
            downloaded_srt.write(buffer, 0, len);
        }                                                                                         //downloaded the songs
        if(downloaded_srt==null) return;


        //saving the srt in buffer folder
        try(OutputStream outputStream = new FileOutputStream(BUFFER_FOLDER+song_name+".srt")) {
            downloaded_srt.writeTo(outputStream);
        }

    }


}