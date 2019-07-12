package com.example.demo.Movies;

import com.example.demo.Models.Movie;
import com.example.demo.Models.User;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Connector {
    private String API_URL;
    private LinkedList<Movie> movies = new LinkedList<>();
    private String TYPE_OF_SEARCHING;

    Connector(String TYPE_OF_SEARCHING, String REQUEST_PARAM) {
        this.API_URL = "http://www.omdbapi.com/?" + TYPE_OF_SEARCHING + "=" + REQUEST_PARAM + "&apikey=503d68ce";
        this.TYPE_OF_SEARCHING = TYPE_OF_SEARCHING;
        getResponse();
    }

    // Getting Response
    private void getResponse() {
        try {
            // Connecting to API
            URL obj = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            //Reading Data
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;

            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            String RESPONSE_STRING = response.toString();

            System.out.println(RESPONSE_STRING);

            //Checking Response from Api
            if (checkResponse(RESPONSE_STRING)) {

                //If response is true parse and save this to movies
                if (TYPE_OF_SEARCHING.equals("s")) {
                    parseToMoviesFromSearch(RESPONSE_STRING);
                } else if(TYPE_OF_SEARCHING.equals("i")) {
                    parseToMoviesWithoutSearch(RESPONSE_STRING);
                }
            } else if (!checkResponse(RESPONSE_STRING)) {

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    //Method which gives Response Param as boolean
    private boolean checkResponse(String resString) throws Exception {
        JSONObject jsonToCheck = new JSONObject(resString);

        return jsonToCheck.getBoolean("Response");
    }

    // Parsing JSON to movies
    private void parseToMoviesFromSearch(String RESPONSE_STRING) {
        try {
            JSONArray resArray = extractJSONArrayFromSearch(RESPONSE_STRING);

            for (int i = 0; i < resArray.length(); i++) {
                JSONObject movieAsJSON = resArray.getJSONObject(i);

                generateMovie(movieAsJSON);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Parsing JSON to movies
    private void parseToMoviesWithoutSearch(String RESPONSE_STRING) {
        try {
            JSONObject json = new JSONObject(RESPONSE_STRING);

            generateMovie(json);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void generateMovie(JSONObject json) {
        try {
            Movie movie = new Movie();
            List<User> Saver = new ArrayList<>();

            String year = json.getString("Year");

            year = year.replaceAll("\\D+","");

            movie.setTitle(json.getString("Title"));
            movie.setType(json.getString("Type"));
            movie.setPoster(json.getString("Poster"));
            movie.setImdbID(json.getString("imdbID"));
            movie.setYear(year);
            if (json.has("Plot")) movie.setPlot(json.getString("Plot"));
            movie.setSaver(Saver);

            movies.add(movie);
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

    public LinkedList<Movie> getMovies() {
        return movies;
    }
}
