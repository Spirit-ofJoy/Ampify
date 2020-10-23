package com.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public class HttpRequestHandler implements HttpHandler {

    private static final String PARAM_STRING = "paramString";

    private static final int HTTP_OK_STATUS = 200;

    public void handle(HttpExchange exchange) throws IOException {

        // Get the paramString form the request
        // response the the file_destionation of the requested song
        String response = exchange.getAttribute(PARAM_STRING).toString();
        System.out.println("Response: " + response);
        File file = new File(response);

        exchange.getResponseHeaders().put("Content-Type", Collections.singletonList(("audio/mpeg"))); //for a audio file

        exchange.sendResponseHeaders(HTTP_OK_STATUS, file.length());// Set the response header status and length

        FileInputStream stream = new FileInputStream(file);       // Opening a input stream to read the file
        OutputStream os = exchange.getResponseBody();
        byte[] buff = new byte[1024];                             // Creating a small buffer to hold bytes as you read them
        int read = 0;

        while((read = stream.read(buff)) > 0) {           // sending bytes to the client
            os.write(buff, 0, read);
        }

        os.close();                      // Closing the streams
        stream.close();
    }
}
