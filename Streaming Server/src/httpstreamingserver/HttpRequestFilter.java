package httpstreamingserver;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;


@SuppressWarnings("restriction")
public class HttpRequestFilter extends Filter {

    private static final String FILTER_DESC = "HttpRequestFilter creates a String from the request parameters and pass it to HttpRequestHandler";

    private static final String SONG_NAME = "song";

    private static final int NAME_IDX = 0;
    private static final int VALUE_IDX = 1;

    private static final String AND_DELIMITER = "&";
    private static final String EQUAL_DELIMITER = "=";

    private static final String DESTINATION_FOLDER = "assets/songs/";

    @Override
    public String description() {
        return FILTER_DESC;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {

        // Create a string form the request query parameters
        URI uri = exchange.getRequestURI();
        String paramString = createStringFromQuery(uri);

        paramString = DESTINATION_FOLDER + paramString;
        // Set paramString as a request attribute
        exchange.setAttribute("paramString", paramString);

        // Chain the request to HttpRequestHandler
        chain.doFilter(exchange);

    }

    private String createStringFromQuery(URI uri) {

        String song = "";
        // Get the request query
        String query = uri.getQuery();
        if (query != null) {
            System.out.println("Query: " + query);
            String[] queryParams = query.split(AND_DELIMITER);
            if (queryParams.length > 0) {
                for (String qParam : queryParams) {
                    String[] param = qParam.split(EQUAL_DELIMITER);
                    if (param.length > 0) {
                        for (int i = 0; i < param.length; i++) {
                            if (SONG_NAME.equals(param[NAME_IDX])) {
                                song = param[VALUE_IDX];// song name stored
                            }
                        }
                    }
                }
            }
        }

        return song;
    }

}