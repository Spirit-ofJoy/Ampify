package radioServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

@SuppressWarnings("restriction")
public class radioHttpRequestHandler implements HttpHandler {

    private static final String PARAM_STRING = "paramString";

    private static final int HTTP_OK_STATUS = 200;

    /**
     * handle handle the http request and send the files the user is asking for
     * @param exchange
     * @throws IOException
     */
    public void handle(HttpExchange exchange) throws IOException {

        // Get the @PARAM_STRING from the request. Here we know where is the song in server that the user is asking for
        // response is the file_destination of the requested song
        String response=exchange.getAttribute(PARAM_STRING).toString();;
        exchange.getResponseHeaders().put("Content-Type", Collections.singletonList(("text/plain"))); //for a audio file

        System.out.println("Response: " + response);

        exchange.sendResponseHeaders(200, response.length());
        byte[] buff=response.getBytes();
        OutputStream os = exchange.getResponseBody();
        os.write(buff);
        os.close();                                            // Closing the streams
    }
}
