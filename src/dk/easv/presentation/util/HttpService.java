package dk.easv.presentation.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class HttpService {

    public HttpService()
    {}

    public String searchMovie(String query) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/search/movie?query=" + query.replace(" ", "%20") + "&include_adult=false&language=en-US&page=1"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiYTgwZjA2MmFhNzViNWUzOGU4M2U2M2QwMDBkOGZkZSIsInN1YiI6IjY1YTUxMjQ2MWEzMjQ4MDEyZjA0ODUxYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.vt7OhyBXRi7txZOUqnh8d7sBurgQsoa9HN69goTkfG0")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        // Check if the response contains results
        JSONObject jsonObject = new JSONObject(response.body());
        JSONArray resultsArray = jsonObject.getJSONArray("results");
        if (resultsArray.isEmpty()) {
            return null;
        }

        return response.body();
    }

    public String getPosterPath(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray resultsArray = jsonObject.getJSONArray("results");

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject movie = resultsArray.getJSONObject(i);
            if (movie.has("poster_path")) {
                Object posterPathObj = movie.get("poster_path");
                if (posterPathObj instanceof String) {
                    String posterPath = (String) posterPathObj;
                    System.out.println(posterPath);
                    return posterPath;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public String searchSeries(String query) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/search/tv?query=" + query.replace(" ", "%20") + "&include_adult=false&language=en-US&page=1"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiYTgwZjA2MmFhNzViNWUzOGU4M2U2M2QwMDBkOGZkZSIsInN1YiI6IjY1YTUxMjQ2MWEzMjQ4MDEyZjA0ODUxYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.vt7OhyBXRi7txZOUqnh8d7sBurgQsoa9HN69goTkfG0")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response.body();
    }

}
