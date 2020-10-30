package HTTPserver;

public class StreamingStart {

    private static final String CONTEXT = "/ampify";
    private static final int HTTP_PORT = 8800;

    public static void startHttpServer() throws Exception {

        // Create a new StreamingHttpServer with HttpRequestFilter and HttpRequestHandler
        HttpStreamingServer httpServer = new HttpStreamingServer(HTTP_PORT, CONTEXT,
                new HttpRequestFilter(), new HttpRequestHandler());

        // Start the server
        httpServer.start();
        System.out.println("[HTTP Server] Started and listening on port " + HTTP_PORT);

        /*
         **UPDATE THE DESTINATION_FOLDER in RequestFilter before plying songs
         */

         /*
             STREAM       http://localhost:8800/ampify?song=SONG_NAME.mp3
         */
    }
}