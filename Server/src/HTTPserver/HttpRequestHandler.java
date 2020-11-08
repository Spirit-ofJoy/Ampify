package HTTPserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

@SuppressWarnings("restriction")
public class HttpRequestHandler implements HttpHandler {

    private static final String PARAM_STRING = "paramString";

    private static final int HTTP_OK_STATUS = 200;

    /**
     * handle handle the http request and send the files the user is asking for
     * @param exchange
     * @throws IOException
     */
    public void handle(HttpExchange exchange) throws IOException {

        // Get the @PARAM_STRING from the request. Here we know where is the song in server that the user is asking for
        // response is the file_destionation of the requested song
        String response = exchange.getAttribute(PARAM_STRING).toString();
        System.out.println("[HTTP Server] Response: " + response);
        File file = new File(response);
        String format = response.substring(response.length()-3);

        /*
         * Changes the response headers of an audio file
         */
        if(format.equals("mp3")) {
            exchange.getResponseHeaders().put("Content-Type", Collections.singletonList(("audio/mpeg"))); //for a audio file
            exchange.getResponseHeaders().put("Accept-Ranges", Collections.singletonList("bytes"));
            exchange.getResponseHeaders().put("Content-Length", Collections.singletonList(String.valueOf(file.length())));
            exchange.getResponseHeaders().put("Allow", Collections.singletonList("GET"));
            exchange.getResponseHeaders().put("IM", Collections.singletonList("feed"));
        }

        /*
         * Changes the response headers of an srt/Subtitles/Lyrics file
         */
        if(format.equals("srt")) {
            exchange.getResponseHeaders().put("Content-Type", Collections.singletonList(("text/srt"))); //for a srt file
            exchange.getResponseHeaders().put("Accept-Ranges", Collections.singletonList("bytes"));
            exchange.getResponseHeaders().put("Content-Length", Collections.singletonList(String.valueOf(file.length())));
            exchange.getResponseHeaders().put("Allow", Collections.singletonList("GET"));
            exchange.getResponseHeaders().put("IM", Collections.singletonList("feed"));
        }

        /*
         * Changes the response headers of an Video file
         *
         * PS - This feature probably won't be used, but does it here matter though? NOPE
         */
        if(format.equals("mp4")) {
            exchange.getResponseHeaders().put("Content-Type", Collections.singletonList(("video/mp4"))); //for a video file
            exchange.getResponseHeaders().put("Accept-Ranges", Collections.singletonList("bytes"));
            exchange.getResponseHeaders().put("Content-Length", Collections.singletonList(String.valueOf(file.length())));
            exchange.getResponseHeaders().put("Allow", Collections.singletonList("GET"));
            exchange.getResponseHeaders().put("IM", Collections.singletonList("feed"));
        }

        exchange.sendResponseHeaders(HTTP_OK_STATUS, file.length());        // Set the response header status and length

        FileInputStream stream = new FileInputStream(file);                   // Opening a input stream to read the file
        OutputStream os = exchange.getResponseBody();
        byte[] buff = new byte[1024];                           // Creating a small buffer to hold bytes as we read them
        int read = 0;

        while((read = stream.read(buff)) > 0) {                // sending bytes to the client
            os.write(buff, 0, read);
        }

        os.flush();
        os.close();                                            // Closing the streams
        stream.close();

    }
}
