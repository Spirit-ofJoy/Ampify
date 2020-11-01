package radioServer;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static radioServer.radioFile.read_radio_file;
import static radioServer.radioFile.response_parser;


@SuppressWarnings("restriction")
public class HttpRequestFilter extends Filter {

    //an arbitary function is needed. Don't mind this string and @description function
    private static final String FILTER_DESC = "HttpRequestFilter creates a String from the request parameters and pass it to HttpRequestHandler";

    private static final String RADIO_NAME = "radio";

    //needed for parameter splitting and calculations
    private static final int NAME_IDX = 0;
    private static final int VALUE_IDX = 1;

    //query Delimiters
    private static final String AND_DELIMITER = "&";
    private static final String EQUAL_DELIMITER = "=";

    ////the folder where the songs are stored in the Server
    private static final String DESTINATION_FOLDER = "assets/songs/";
    private static final String RADIO_FOLDER = DESTINATION_FOLDER+"../radio/";

    /**
     * @return A necessary function though not being used currently.
     */
    @Override
    public String description() {
        return FILTER_DESC;
    }

    /**
     * @param exchange
     * @param chain
     * @throws IOException
     */
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {

        // Create a string form the request query parameters
        URI uri = exchange.getRequestURI();
        String paramString = createStringFromQuery(uri);

        paramString = RADIO_FOLDER + paramString;
        // Set paramString as a request attribute
        exchange.setAttribute("paramString", response_parser(read_radio_file(new File(paramString))));
        // Chain the request to HttpRequestHandler
        chain.doFilter(exchange);

    }

    /**
     * @param uri This function take the URI. Here we split the URI and take every parameter
     *            being requested by the Client
     * @return song location in the server
     */
    private String createStringFromQuery(URI uri) {

        String radio = "";
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
                            if (RADIO_NAME.equals(param[NAME_IDX])) {
                                radio = param[VALUE_IDX];// song name stored
                            }
                        }
                    }
                }
            }
        }

        return radio;// returning the requested song location
    }

}