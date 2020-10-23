package httpstreamingserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


@SuppressWarnings("restriction")
public class HttpStreamingServer {

    private HttpServer httpServer;

    /**
     * Instantiates a new streaming http server with the
     * @param port - the port
     * @param context - the context
     * @param handler - the handler
     */

    public HttpStreamingServer(int port, String context, Filter filter, HttpHandler handler) {
        try {

            // Create HttpServer which is listening on the given port
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);

            // Create a new context for the given context and handler
            HttpContext httpContext = httpServer.createContext(context, handler);

            // Add HttpRequestFilter to the context
            httpContext.getFilters().add(filter);

            // Create a default executor
            httpServer.setExecutor(null);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Start server.
     */
    public void start() {
        this.httpServer.start();
    }

}
