package com.example.YakServer.Movies.MovieSystem;

import com.example.YakServer.Models.Movie;
import com.example.YakServer.Models.User;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connector {
    private final Logger logger = Logger.getLogger(DBGenerator.class.getName());

    public Connector() {}

    public Optional<Movie> getMovieById(String REQUEST_IMDBID) {
        String reqParam = "i";

        String API_URL = "http://www.omdbapi.com/?"
                + reqParam
                + "="
                + updateRequestParam(REQUEST_IMDBID)
                + "&apikey=503d68ce";

        String response = getResponse(API_URL);

        if(!response.equals("")) {
            Movie movie = parseMovie(response);

            return Optional.of(movie);
        } else {
            return Optional.empty();
        }
    }

    Optional<List<Movie>> getMoviesByTitle(String REQUEST_TITLE) {
        String reqParam = "s";

        String API_URL = "http://www.omdbapi.com/?"
                + reqParam
                + "="
                + updateRequestParam(REQUEST_TITLE)
                + "&apikey=503d68ce";

        String response = getResponse(API_URL);

        if(!response.equals("")) {
            List<Movie> movies = parseMovies(response);

            return Optional.of(movies);
        } else {
            return Optional.empty();
        }
    }

    private String getResponse(String API_URL) {
        try {
            URL obj = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;

            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            String RESPONSE_STRING = response.toString();

            if (checkResponse(RESPONSE_STRING)) {
               return RESPONSE_STRING;
            } else {
                logger.log(Level.WARNING, "Search JSON doesn't have Response");

                return "";
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkResponse(String resString) throws Exception {
        JSONObject jsonToCheck = new JSONObject(resString);

        return jsonToCheck.getBoolean("Response");
    }

    private List<Movie> parseMovies(String RESPONSE_STRING) {
        try {
            List<Movie> movies = new ArrayList<>();

            JSONArray resArray = extractJSONArrayFromSearch(RESPONSE_STRING);

            for (int i = 0; i < resArray.length(); i++) {
                JSONObject movieAsJSON = resArray.getJSONObject(i);

                movies.add(generateMovie(movieAsJSON));
            }

            return movies;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Movie parseMovie(String RESPONSE_STRING) {
        try {
            JSONObject json = new JSONObject(RESPONSE_STRING);

            return generateMovie(json);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Movie generateMovie(JSONObject json) {
        try {
            Movie movie = new Movie();
            List<User> Saver = new ArrayList<>();

            String year = json.getString("Year");

            year = year.replaceAll("\\D+","");

            movie.setTitle(json.getString("Title"));
            movie.setType(json.getString("Type"));
            movie.setPoster(json.getString("Poster"));
            movie.setImdbID(json.getString("imdbID"));
            movie.setYear(Integer.parseInt(year));
            if (json.has("Plot")) movie.setPlot(json.getString("Plot"));
            movie.setSaver(Saver);

            return movie;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JSONArray extractJSONArrayFromSearch(String resString) {
        try {
            JSONObject resObj = new JSONObject(resString);

            return (JSONArray) resObj.get("Search");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String updateRequestParam(String request_param) {
        return request_param.replaceAll(" ", "+");
    }

}
