package framework;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import mediacontent.BookInfo;
import mediacontent.FilmInfo;
import mediacontent.GameInfo;
import mediacontent.MusicInfo;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TimeZone;

public class APIOperations {

    //Find books on Google Books
   public static LinkedList<BookInfo> bookGetInfo(String name, String ISBN, String max_result, String orderBy) throws UnirestException {
       String name_request=name.replace(" ","%20").trim();
       String ISBN_request=ISBN.replace(" ","");
       String market_language="us";
       String orderBy_request=orderBy.trim();
       if (orderBy_request.equals("")) orderBy_request="relevance";
       if (ISBN_request.length()!=13) ISBN_request="";
       LinkedList<BookInfo> lis=new LinkedList<BookInfo>();
       HttpResponse<JsonNode> jsonResponse = null;
       String urlRequest="https://www.googleapis.com/books/v1/volumes?q=" + name_request + "&projection=lite&orderBy="+orderBy_request+"&key="+ MISConfig.getGoogle_api()+"&country="+market_language;
       //Check if isbn was provided
       if(max_result.equals("all")) {
           if (ISBN_request.length()!=13)
               jsonResponse = Unirest.get("https://www.googleapis.com/books/v1/volumes?q=" + name_request + "&projection=lite&orderBy="+orderBy_request+"&key="+ MISConfig.getGoogle_api()+"&country="+market_language).asJson();
           else jsonResponse = Unirest.get("https://www.googleapis.com/books/v1/volumes?q=isbn:" + ISBN_request+"&projection=lite&orderBy="+orderBy_request+"&key="+ MISConfig.getGoogle_api()+"&country="+market_language).asJson();
       }
       else {
           if(ISBN.equals("")) jsonResponse = Unirest.get("https://www.googleapis.com/books/v1/volumes?q="+name_request+"&maxResults="+max_result+"&projection=lite&orderBy="+orderBy_request+"&key="+ MISConfig.getGoogle_api()+"&country="+market_language).asJson();
           else jsonResponse = Unirest.get("https://www.googleapis.com/books/v1/volumes?q=isbn:" + ISBN_request+"&maxResults="+max_result+"&projection=lite&orderBy="+orderBy_request+"&key="+ MISConfig.getGoogle_api()+"&country="+market_language).asJson();
       }

       JSONObject jsonObject= new JSONObject(jsonResponse.getBody());
       //ystem.out.println(jsonObject);
       //Generate List of results
       if(jsonObject.has("array")){
           JSONArray restArray=jsonObject.getJSONArray("array");
           for (int k=0; k<restArray.length();k++){
               jsonObject=restArray.getJSONObject(k);
               if(jsonObject.has("items")) {
                   JSONArray jArray = jsonObject.getJSONArray("items");
                   for (int i = 0; i < jArray.length(); i++) {
                       StringBuilder author = new StringBuilder();
                       BookInfo b = new BookInfo();
                       b.setISBN(jArray.getJSONObject(i).getString("id"));
                       JSONObject volumeInfo = jArray.getJSONObject(i).getJSONObject("volumeInfo");
                       if (volumeInfo.has("title")) {
                           b.setTitle(volumeInfo.getString("title"));
                           if (volumeInfo.has("authors")){
                               //Create a string containing all authors of the book
                               JSONArray authors = volumeInfo.getJSONArray("authors");
                               for (int j = 0; j < authors.length(); j++) {
                                   author.append(authors.getString(j));
                                   if (authors.length() > 1 && j < author.length() - 1) author.append(", ");

                               }

                           }
                           if (volumeInfo.has("description")) b.setOverview(volumeInfo.getString("description"));

                           if (volumeInfo.has("publisher")) b.setPublisher(volumeInfo.getString("publisher"));
                           if (volumeInfo.has("publishedDate")) b.setReleaseDate(volumeInfo.getString("publishedDate"));
                           if (volumeInfo.has("imageLinks")) {
                               JSONObject images=volumeInfo.getJSONObject("imageLinks");
                               if (images.has("thumbnail")) b.setLinkImage(MediaOperations.forceHTTPS(images.getString("thumbnail")).replace("&zoom=1","&zoom=0"));
                           }
                           //System.out.println(b.getOverview());
                           b.setAuthor(author.toString());
                           lis.add(b);
                       }
                       if (!max_result.equals("all") && i == Integer.parseInt(max_result)-1) break;

                   }
               }


           }
       }

       return lis;


   }

    //Find music on discogs
    public static LinkedList<MusicInfo> musicGetInfoDiscogs(String name, String max_result, String type,String orderBy, String artist, String year) throws UnirestException {
        String name_request = name.replace(" ", "%20");
        String artist_request = artist.trim().replace(" ", "%20");
        LinkedList<MusicInfo> lis = new LinkedList<MusicInfo>();
        String urlRequest = "https://api.discogs.com/database/search?q=" + name_request + "&format=" + type + "&token=" + MISConfig.getDiscogs_api();
        if (!year.equals("")) urlRequest = urlRequest + "&year=" + year;
        if (!artist_request.equals("")) urlRequest = urlRequest + "&artist=" + artist_request;
        HttpResponse<JsonNode> jsonResponse = Unirest.get(urlRequest).asJson();
        JSONObject jsonObject = new JSONObject(jsonResponse.getBody());
        //System.out.println(jsonObject);
        //System.out.println(urlRequest);
        JSONArray jArray = jsonObject.getJSONArray("array");

        int iteration = 0;
        //Generate list of results
        for (int i = 0; i < jArray.length(); i++) {
            JSONArray resultInfo = jArray.getJSONObject(i).getJSONArray("results");
            for (int k = 0; k < resultInfo.length(); k++) {
                JSONObject result = resultInfo.getJSONObject(k);
                MusicInfo b = new MusicInfo();
                b.setTitle(result.getString("title"));
                JSONArray genres = new JSONArray();
                if (result.has("genre")) genres = result.getJSONArray("genre");
                StringBuilder genre_build = new StringBuilder();
                //Create a string containing all genres
                for (int j = 0; j < genres.length(); j++) {
                    genre_build.append(genres.getString(i));
                    if (j > 0) genre_build.append(", ");
                }
                JSONArray labels = new JSONArray();
                if (result.has("label")) labels = result.getJSONArray("label");
                StringBuilder label_build = new StringBuilder();
                //Create a string containing all labels
                for (int j = 0; j < labels.length(); j++) {
                    label_build.append(labels.getString(i));
                    if (j > 0) label_build.append(", ");
                }
                b.setLabels(label_build.toString());
                b.setGenre(genre_build.toString());
                if (result.has("year")) b.setReleaseDate(result.getString("year"));
                lis.add(b);
                if ((!max_result.equals("all")) && iteration == Integer.parseInt(max_result) - 1) break;
                iteration++;


            }
        }

        if (lis.size() > 0 && (!orderBy.equals("relevance") && !orderBy.equals(""))) {
            Collections.sort(lis, new Comparator<MusicInfo>() {
                @Override
                public int compare(final MusicInfo object1, final MusicInfo object2) {
                    if (orderBy.equals("newest")) return object2.getReleaseDate().compareTo(object1.getReleaseDate());
                    else return object1.getReleaseDate().compareTo(object2.getReleaseDate());
                }
            });
        }

        return lis;
    }

        //Find music on iTunes
        public static LinkedList<MusicInfo> musicGetInfoItunes(String name, String max_result, String orderBy, String artist, String year) throws UnirestException
        {
            String name_request = name.replace(" ", "%20");
            String artist_request = artist.trim().replace(" ", "%20");
            LinkedList<MusicInfo> lis = new LinkedList<MusicInfo>();
            String urlRequest = "";
            if (!artist_request.equals("")) urlRequest="https://itunes.apple.com/search" + "?term=" + name_request+"%20"+artist_request+"&media=music";
            else urlRequest="https://itunes.apple.com/search" + "?term=" + name_request+"&media=music";
            String year_request=year;
            if (year_request.trim().equals("") || year.length()<4) year_request="";
            HttpResponse<JsonNode> jsonResponse = Unirest.get(urlRequest).asJson();
            //System.out.println(jsonResponse.getBody());
            JSONObject jsonObject = new JSONObject(jsonResponse.getBody());
            JSONArray jArray = jsonObject.getJSONArray("array");

            int iteration = 0;
            //Generate list of results
            for (int i = 0; i < jArray.length(); i++) {
                JSONArray resultInfo = jArray.getJSONObject(i).getJSONArray("results");
                for (int k = 0; k < resultInfo.length(); k++) {
                    JSONObject result = resultInfo.getJSONObject(k);
                    MusicInfo b = new MusicInfo();
                    b.setTitle(result.getString("trackName"));
                    if (result.has("artistName")) b.setArtist(result.getString("artistName"));
                    if (result.has("primaryGenreName")) b.setGenre(result.getString("primaryGenreName"));
                    if (result.has("releaseDate")) {
                        String y=result.getString("releaseDate");
                        b.setReleaseDate(y.substring(0,y.indexOf("T")));
                    }
                    if(result.has("artworkUrl100")) {
                        String link_reverse=StringUtils.reverse(result.getString("artworkUrl100"));
                        link_reverse=StringUtils.replace(link_reverse,"001","215",2);
                        b.setLinkImage(StringUtils.reverse(link_reverse));
                        //iTunes cover link doesn't support HTTPS
                    }
                    if (result.has("collectionName")) b.setCollection(result.getString("collectionName"));
                    if (result.has("description")) b.setOverview(result.getString("description"));
                    if (result.has("previewUrl")) b.setLinkpreview(result.getString("previewUrl"));
                    if(b.getReleaseDate().substring(0,4).equals(year) || year_request.equals("")) lis.add(b);
                    //System.out.println(b);
                    if (!max_result.equals("all") && iteration == Integer.parseInt(max_result) - 1) break;
                    iteration++;
                }
            }

        if (lis.size() > 0 && (!orderBy.equals("relevance") && !orderBy.equals(""))) {
            Collections.sort(lis, new Comparator<MusicInfo>() {
                @Override
                public int compare(final MusicInfo object1, final MusicInfo object2) {
                    if (orderBy.equals("newest")) return object2.getReleaseDate().compareTo(object1.getReleaseDate());
                    else return object1.getReleaseDate().compareTo(object2.getReleaseDate());
                }
            });
        }
        return lis;
    }

    //Find films on TheMovieDB
    public static LinkedList<FilmInfo> filmGetInfo(String name, String max_result, String language, String year, String orderBy) throws UnirestException {
        LinkedList<FilmInfo> lis = new LinkedList<FilmInfo>();
        int iteration=0;
        String name_request = name.replace(" ", "%20");
        HttpResponse<JsonNode> jsonResponse;
        String urlRequest="https://api.themoviedb.org/3/search/movie?api_key=" + MISConfig.getThemoviedb_api() + "&query=" + name_request;
        if(!language.equals("")) urlRequest=urlRequest+"&language="+language.toLowerCase();
        if(!year.equals("")) urlRequest=urlRequest+"&primary_release_year="+year;
        jsonResponse=Unirest.get(urlRequest).asJson();
        JSONObject jsonObject = new JSONObject(jsonResponse.getBody());
        //System.out.println(jsonObject);
        JSONArray array = jsonObject.getJSONArray("array");
        //Generate list of results
        for (int k = 0; k < array.length(); k++) {
            JSONArray jArray = array.getJSONObject(k).getJSONArray("results");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject filmInfo = jArray.getJSONObject(i);
                FilmInfo b = new FilmInfo();
                if(filmInfo.has("vote_average")) {
                    b.setVote(String.valueOf(filmInfo.getDouble("vote_average")));
                }
                if(filmInfo.has("poster_path")){
                    Object o= filmInfo.get("poster_path");
                    String link="https://image.tmdb.org/t/p/w185/"+o.toString();
                    if (!link.contains("null")) b.setLinkImage("https://image.tmdb.org/t/p/w185/"+o.toString());
                }
                b.setTitle(filmInfo.getString("title"));
                b.setOverview(filmInfo.getString("overview"));
                b.setReleaseDate(filmInfo.getString("release_date"));
                lis.add(b);

                if (!max_result.equals("all") && iteration == Integer.parseInt(max_result)-1) break;
                iteration++;

            }
        }
        if (lis.size() > 0 && (orderBy.equals("newest") )) {
            Collections.sort(lis, new Comparator<FilmInfo>() {
                @Override
                public int compare(final FilmInfo object1, final FilmInfo object2) {
                    return object2.getReleaseDate().compareTo(object1.getReleaseDate());
                }
            });
        }

        return lis;
    }

    //Find games on IGDB
    public static LinkedList<GameInfo> gameGetInfo(String name,String max_result, String orderBy) throws UnirestException {
        LinkedList<GameInfo> lis=new LinkedList<GameInfo>();
        String name_requested=name.replace(" ","%20");
        HttpResponse<JsonNode> response;
        if(orderBy.equals("")||orderBy.equals("popularity")) {
            response = Unirest.get("https://api-endpoint.igdb.com/games/?search=" + name_requested + "&fields=name,summary,aggregated_rating,websites,pegi,first_release_date,cover,platforms.name&expand=platforms&limit="+max_result).header("user-key", MISConfig.getIGDB())
                    .header("Accept", "application/json")
                    .asJson();
        }
        else {
            response = Unirest.get("https://api-endpoint.igdb.com/games/?search=" + name_requested + "&fields=name,summary,aggregated_rating,websites,pegi,cover,first_release_date,platforms.name&expand=platforms&order=" + orderBy+"&limit="+max_result).header("user-key", MISConfig.getIGDB())
                    .header("Accept", "application/json")
                    .asJson();
        }
        //System.out.println(response.getBody());
        JSONObject jsonObject=new JSONObject(response.getBody());
        JSONArray jarray= jsonObject.getJSONArray("array");
        //Generate List of results
        for (int i=0;i<jarray.length();i++){
            JSONObject gameInfo=jarray.getJSONObject(i);
            GameInfo b =new GameInfo();
            if(gameInfo.has("name")) b.setTitle(gameInfo.getString("name"));
            if(gameInfo.has("aggregated_rating")) b.setVote(String.valueOf( new DecimalFormat("#.##").format(gameInfo.getDouble("aggregated_rating"))));
            if(gameInfo.has("summary")) b.setOverview(gameInfo.getString("summary"));
            MediaOperations.parsePEGI(b,gameInfo);
            MediaOperations.parseWEB(b,gameInfo);
            MediaOperations.parsePlatforms(b,gameInfo);
            if (gameInfo.has("cover")){
                JSONObject covers=gameInfo.getJSONObject("cover");
                if (covers.has("url")){
                    b.setLinkImage(covers.getString("url").replace("t_thumb","t_logo_med_2x"));
                }
            }
            if(gameInfo.has("first_release_date")){
                //Convert Unix epoch to DD:MM:YYYY format
                Long unixEpoch= gameInfo.getLong("first_release_date");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setTimeZone(TimeZone.getTimeZone("CET"));
                String formatted = sdf.format(unixEpoch);
                b.setReleaseDate(formatted);
            }
            lis.add(b);
            if(!max_result.equals("all") && i== Integer.parseInt(max_result)-1) break;
        }
        return lis;
    }


}
