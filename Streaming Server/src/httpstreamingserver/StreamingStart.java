package httpstreamingserver;

import httpstreamingserver.HttpRequestFilter;
import httpstreamingserver.HttpRequestHandler;
import httpstreamingserver.HttpStreamingServer;

public class StreamingStart {

    private static final String CONTEXT = "/ampify";
    private static final int PORT = 8000;

    public static void main(String[] args) throws Exception {

        // Create a new StreamingHttpServer with HttpRequestFilter and HttpRequestHandler
        HttpStreamingServer httpServer = new HttpStreamingServer(PORT, CONTEXT,
                new HttpRequestFilter(), new HttpRequestHandler());

        // Start the server
        httpServer.start();
        System.out.println("Server is started and listening on port " + PORT);

        /*
         **UPDATE THE DESTINATION_FOLDER in RequestFilter before plying songs
         */

         /*
             STREAM       http://localhost:8000/ampify?song=SONG_NAME.mp3
         */
    }
}