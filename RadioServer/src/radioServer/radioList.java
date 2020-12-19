package radioServer;

import java.io.File;
import static radioServer.radioFile.*;

public class radioList {
    public static File RadioDirectory= new File(ASSETS_LOCATION + "../radio/");
    //List of all files int the folder

    public static String radioEndTime_from_file_name(String fileName){
        return fileName.substring(fileName.length()-13);
    }

    /**
     * @return This function returns the string of all the currently active radios
     */
    public static String activeRadios(){
        String allRadios[] = RadioDirectory.list(); //making a list of all radio files
        String allActiveRadios = ";";
        long CurrentTime = System.currentTimeMillis();
        for(int i = 0; i < allRadios.length; i++ ){
            if(CurrentTime < Long.parseLong(radioEndTime_from_file_name(allRadios[i]))){
                 allActiveRadios = allActiveRadios + allRadios[i] + ";";
            } else {
                File inactiveRadio = new File(ASSETS_LOCATION + "../radio/" + allRadios[i]);
                if(inactiveRadio.exists()){
                    inactiveRadio.delete(); //deleting inactive radios
                } else {
                    continue;
                }
            }
        }
        return allActiveRadios; //returning all the active radio in String format
    }

    /*
    public static void main(String[] args)  throws Exception {
        System.out.println(System.currentTimeMillis()+"              "+activeRadios());
    }
     */
}
