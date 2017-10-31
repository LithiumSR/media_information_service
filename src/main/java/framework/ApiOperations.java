package framework;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import mediacontent.BookInfo;
import mediacontent.FilmInfo;
import mediacontent.GameInfo;
import mediacontent.MusicInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.TimeZone;
import java.util.logging.Level;

public class ApiOperations {

    /**
   public static void main(String [ ] args)
    {

        //Test code
        new MyAPIKey("redacted_api.cfg");
        System.out.println(MyAPIKey.getIGDB());
        LinkedList<MediaInfo> b=bookGetInfo("Harry Potter","0","5");
        System.out.println(b.toString());
        List<MediaInfo> a=filmGetInfo("love","5");
        List<MediaInfo> c = musicGetInfo("","5");
        System.out.println(c);

    }
     **/


    //Find books on Google Books
   public static LinkedList<BookInfo> bookGetInfo(String name, String ISBN, String max_result, String orderBy) throws UnirestException {
        String name_request=name.replace(" ","%20");
        LinkedList<BookInfo> lis=new LinkedList<BookInfo>();
       HttpResponse<JsonNode> jsonResponse = null;
       String urlRequest="https://www.googleapis.com/books/v1/volumes?q=" + name_request + "&projection=lite&orderBy="+orderBy+"&key="+MyAPIKey.getGoogle_api();
       //Check if isbn was provided
       if(max_result.equals("all")) {
           if (ISBN.equals(""))
               jsonResponse = Unirest.get("https://www.googleapis.com/books/v1/volumes?q=" + name_request + "&projection=lite&orderBy="+orderBy+"&key="+MyAPIKey.getGoogle_api()).asJson();
           else jsonResponse = Unirest.get("https://www.googleapis.com/books/v1/volumes?q=isbn:" + ISBN+"&projection=lite&orderBy="+orderBy+"&key="+MyAPIKey.getGoogle_api()).asJson();
       }
       else {
           if(ISBN.equals("")) jsonResponse = Unirest.get("https://www.googleapis.com/books/v1/volumes?q="+name_request+"&maxResults="+max_result+"&projection=lite&orderBy="+orderBy+"&key="+MyAPIKey.getGoogle_api()).asJson();
           else jsonResponse = Unirest.get("https://www.googleapis.com/books/v1/volumes?q=isbn:" + ISBN+"&maxResults="+max_result+"&projection=lite&orderBy="+orderBy+"&key="+MyAPIKey.getGoogle_api()).asJson();
       }

       JSONObject jsonObject= new JSONObject(jsonResponse.getBody());
       System.out.println(jsonObject);
       System.out.println("title: "+name+"  isbn: "+ISBN+ " max_result:"+max_result+" orderBy:"+orderBy);
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
    public static LinkedList<MusicInfo> musicGetInfo(String name, String max_result, String type, String artist, String year) throws UnirestException {
        String name_request=name.replace(" ","%20");
        String artist_request=artist.replace(" ","%20");
        LinkedList<MusicInfo> lis=new LinkedList<MusicInfo>();
        String urlRequest="https://api.discogs.com/database/search?q="+name_request+"&format="+type+"&token="+MyAPIKey.getDiscogs_api();
        if(!year.equals("")) urlRequest=urlRequest+"&year="+year;
        if(!artist.equals("")) urlRequest=urlRequest+"&artist="+artist_request;
        HttpResponse<JsonNode> jsonResponse = Unirest.get(urlRequest).asJson();
        JSONObject jsonObject= new JSONObject(jsonResponse.getBody());
        //System.out.println(jsonObject);
        System.out.println(urlRequest);
        JSONArray jArray = jsonObject.getJSONArray("array");

        int iteration=0;
        //Generate list of results
        for(int i = 0; i < jArray.length(); i++) {
            JSONArray resultInfo = jArray.getJSONObject(i).getJSONArray("results");
            for(int k = 0; k < resultInfo.length(); k++) {
                JSONObject result=resultInfo.getJSONObject(k);
                MusicInfo b=new MusicInfo();
                b.setTitle(result.getString("title"));
                JSONArray genres = result.getJSONArray("genre");
                StringBuilder genre_build = new StringBuilder();
                //Create a string containing all genres
                for (int j = 0; j < genres.length(); j++) {
                    genre_build.append(genres.getString(i));
                    if (j > 0) genre_build.append(", ");
                }

                JSONArray labels = result.getJSONArray("label");
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
                if(!max_result.equals("all") && iteration==Integer.parseInt(max_result)-1) break;
                iteration++;


            }
        }


        return lis;
    }

    //Find films on TheMovieDB
    public static LinkedList<FilmInfo> filmGetInfo(String name, String max_result, String language, String year) throws UnirestException {
        LinkedList<FilmInfo> lis = new LinkedList<FilmInfo>();
        int iteration=0;
        String name_request = name.replace(" ", "%20");
        HttpResponse<JsonNode> jsonResponse;
        String urlRequest="https://api.themoviedb.org/3/search/movie?api_key=" + MyAPIKey.getThemoviedb_api() + "&query=" + name_request+"&sort_by=vote_average.desc";
        System.out.println(urlRequest);
        if(!language.equals("")) urlRequest=urlRequest+"&language="+language;
        if(!year.equals("")) urlRequest=urlRequest+"&primary_release_year="+year;
        System.out.println(urlRequest);
        jsonResponse=Unirest.get(urlRequest).asJson();
        JSONObject jsonObject = new JSONObject(jsonResponse.getBody());
        System.out.println(jsonObject);
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
                b.setTitle(filmInfo.getString("title"));
                b.setOverview(filmInfo.getString("overview"));
                b.setReleaseDate(filmInfo.getString("release_date"));
                lis.add(b);

                if (!max_result.equals("all") && iteration == Integer.parseInt(max_result)-1) break;
                iteration++;

            }
        }

        return lis;
    }

    //Find games on IGDB
    public static LinkedList<GameInfo> gameGetInfo(String name,String max_result, String orderBy) throws UnirestException {
        LinkedList<GameInfo> lis=new LinkedList<GameInfo>();
        String name_requested=name.replace(" ","%20");
        HttpResponse<JsonNode> response;
        if(orderBy.equals("")) {
            response = Unirest.get("https://api-2445582011268.apicast.io/games/?search=" + name_requested + "&fields=name,summary,aggregated_rating,websites,pegi,first_release_date").header("user-key", MyAPIKey.getIGDB())
                    .header("Accept", "application/json")
                    .asJson();
        }
        else {
            System.out.println("I'm here with "+ orderBy);
            response = Unirest.get("https://api-2445582011268.apicast.io/games/?search=" + name_requested + "&fields=name,summary,aggregated_rating,websites,pegi,first_release_date&order=" + orderBy).header("user-key", MyAPIKey.getIGDB())
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
            if(gameInfo.has("aggregated_rating")) b.setVote(String.valueOf(gameInfo.getDouble("aggregated_rating")));
            if(gameInfo.has("summary")) b.setOverview(gameInfo.getString("summary"));
            MediaOperations.parsePEGI(b,gameInfo);
            MediaOperations.parseWEB(b,gameInfo);
            if(gameInfo.has("first_release_date")){
                //Convert Unix epoch to DD:MM:YYYY format
                Long unixEpoch=(long)gameInfo.getLong("first_release_date");
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
