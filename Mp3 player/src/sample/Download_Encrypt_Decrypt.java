package sample;
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
import static sample.Constants.*;
import static sample.functions.*;

public class Download_Encrypt_Decrypt {

    //here we download the songs and make them inaccessable to the users through other media players
    /**
     * @param song_name without song format
     * @throws IOException
     * save the specified song in the @DESTINATION_DOWNLOAD_FOLDER
     */
    public static void encrypt_and_download_song(String song_name) throws IOException {

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
        try(OutputStream outputStream = new FileOutputStream(DESTINATION_DOWNLOAD_FOLDER+song_name+FILE_EXTENSION)) {
            encrypted_song.writeTo(outputStream);
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

        System.out.println("Made the Song "+file.getName()+" accessable to the user temperorily in .buffer.mp3");

        try(OutputStream outputStream = new FileOutputStream(song_buffer_location)) {
            decrypted.writeTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //
    //Files.createDirectories(Paths.get("/Your/Path/Here"));

    public static void main(String[] args) throws FileNotFoundException, IOException {
        encrypt_and_download_song("S#003");
        decrypt_downloaded_song(DESTINATION_DOWNLOAD_FOLDER+"S#003.mp3");

    }

}