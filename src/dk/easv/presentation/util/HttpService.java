package dk.easv.presentation.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/*
HTTPService klassens formål er at organisere API-relateret kode det samme sted, vi behandler api-responserne med "org.json"
librarien, og bruger TMDB's free to use api.
 */
public class HttpService {

    public HttpService()
    {}
/*
searchMovie tager en query som parameter og sender en GET request afsted til et endpoint der søger databasen for film.
I starten er der lige en lille stringmanipulation da i mange tilfæller er titlerne fra vores datasæt så specifikke at api-en ikke kan
finde nogen resultater. efter requesten returnerer metoden jsonresponsen som et string.
 */
    public String searchMovie(String query) throws IOException, InterruptedException {
        String searchQuery = query;

        int colonIndex = query.indexOf(":");
        if (colonIndex != -1) {
            searchQuery = query.substring(0, colonIndex);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/search/movie?query=" + searchQuery.replace(" ", "%20") + "&include_adult=false&language=en-US&page=1"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiYTgwZjA2MmFhNzViNWUzOGU4M2U2M2QwMDBkOGZkZSIsInN1YiI6IjY1YTUxMjQ2MWEzMjQ4MDEyZjA0ODUxYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.vt7OhyBXRi7txZOUqnh8d7sBurgQsoa9HN69goTkfG0")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonObject = new JSONObject(response.body());
        JSONArray resultsArray = jsonObject.getJSONArray("results");
        if (resultsArray.isEmpty()) {
            return null;
        }

        return response.body();
    }
/*
getPosterPath tager en String som parameter og laver med det samme dette string om til et JSONObject
herefter bruger vi nogen af de indbygget JSONArray metoder til at få fat i information fra resultatsættet som vi har brug for.
I vores tilfælle er det slutningen på et url hvor det fulde url er linket til et boxcover billede.
 */
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
/*
Det samme som searchMovies men bare for serier, da api'en ikke tillader at søge begge dele med et endpoint.
 */
    public String searchSeries(String query) throws IOException, InterruptedException {
        String searchQuery = query;

        int colonIndex = query.indexOf(":");
        if (colonIndex != -1) {
            searchQuery = query.substring(0, colonIndex);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/search/tv?query=" + searchQuery.replace(" ", "%20") + "&include_adult=false&language=en-US&page=1"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiYTgwZjA2MmFhNzViNWUzOGU4M2U2M2QwMDBkOGZkZSIsInN1YiI6IjY1YTUxMjQ2MWEzMjQ4MDEyZjA0ODUxYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.vt7OhyBXRi7txZOUqnh8d7sBurgQsoa9HN69goTkfG0")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response.body();
    }

}
