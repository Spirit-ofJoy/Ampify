package ampify_player;

public class functions {

    /**
     * @param song_name must only be used when givng the song name in a URL otherwise don't
     * @return song name but changes '#' with '%23' as only then it can be used in a URL
     */
    public static String use_song_name(String song_name){
        System.out.println(song_name);
        return song_name.replace("#","%23");                          // we cant use '#' in URL directly
        //so we have to use %23 in place of #
    }

    /**
     * @param query_url give @query_url as the input
     * @param song_name (format is not needed to specify here)
     * @return Complete Usable URL
     */
    public static String useURL(String query_url, String song_name){                    //give the song name with format
        String URL = query_url+song_name+".mp3";                       //Setting the URL of the Song
        return URL;
    }

    public static void causeDelay(){
        int a =3000;
        for(int i=0;i<a;i++){
            for(int j=0;j<a;j++){
                for(int k=0;k<=0;k++)
                    System.out.print("");
            }
        }
    }

    public static String reverse(String input)
    {
        // getBytes() method to convert string into bytes[].
        byte[] strAsByteArray = input.getBytes();
        byte[] result = new byte[strAsByteArray.length];
        // Storing result in reverse order into the
        for (int i = 0; i < strAsByteArray.length; i++)
            result[i] = strAsByteArray[strAsByteArray.length - i - 1];

        return new String(result);
    }
}
