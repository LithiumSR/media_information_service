package framework;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import mediacontent.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class ApiOperations {

   public static void main(String [ ] args)
    {
        //new MyAPIKey("redacted_api.cfg");
        //System.out.println(MyAPIKey.getIGDB());
       // gameGetInfoTest3("Destiny 2");
        //new MyAPIKey("redacted_api.cfg");
        //gameGetInfoTest2("Destiny_2");
        //LinkedList<MediaInfo> b=bookGetInfo("Harry Potter","0","5");
        //System.out.println(b.toString());
            //List<MediaInfo> a=filmGetInfo("love","5");
            //System.out.println(a.toString());
        //List<MediaInfo> c = musicGetInfo("Adriano Celentano","5");
        //System.out.println(c);

    }



   public static LinkedList<BookInfo> bookGetInfo(String name, String ISBN, String max_result){

        java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(Level.OFF);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "ERROR");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "ERROR");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "ERROR");

        HttpClient httpClient = new DefaultHttpClient();
        String name_request=name.replace(" ","+");
        LinkedList<BookInfo> lis=new LinkedList<BookInfo>();

        try {
            HttpGet httpGetRequest = null;
            if(max_result.equals("all")) {
                if (ISBN.equals(""))
                    httpGetRequest = new HttpGet("https://www.googleapis.com/books/v1/volumes?q=" + name_request + "&projection=lite&orderBy=relevance"+"&key="+MyAPIKey.getGoogle_api());
                else httpGetRequest = new HttpGet("https://www.googleapis.com/books/v1/volumes?q=isbn:" + ISBN+"&projection=lite&orderBy=relevance"+"&key="+MyAPIKey.getGoogle_api());
            }
            else {
                if(ISBN.equals("")) httpGetRequest= new HttpGet("https://www.googleapis.com/books/v1/volumes?q="+name_request+"&maxResults="+max_result+"&projection=lite&orderBy=relevance"+"&key="+MyAPIKey.getGoogle_api());
                else httpGetRequest = new HttpGet("https://www.googleapis.com/books/v1/volumes?q=isbn:" + ISBN+"&maxResults="+max_result+"&projection=lite&orderBy=relevance"+"&key="+MyAPIKey.getGoogle_api());

            }
            org.apache.http.HttpResponse httpResponse = httpClient.execute(httpGetRequest);

            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getStatusLine());
            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getEntity().toString());

            HttpEntity entity = httpResponse.getEntity();
            StringBuilder sb=new StringBuilder();
            byte[] buffer = new byte[1024];
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                try {
                    int bytesRead = 0;
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        sb.append(new String(buffer, 0, bytesRead));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    String json_string=sb.toString();
                    try { inputStream.close(); } catch (Exception ignore) {}
                    JSONObject jsonObject= new JSONObject(json_string);
                    if(jsonObject.has("items")) {
                        JSONArray jArray = jsonObject.getJSONArray("items");

                        System.out.println(json_string);
                        for (int i = 0; i < jArray.length(); i++) {
                            StringBuilder author = new StringBuilder();
                            BookInfo b = new BookInfo();
                            b.setISBN(jArray.getJSONObject(i).getString("id"));
                            JSONObject volumeInfo = jArray.getJSONObject(i).getJSONObject("volumeInfo");
                            if (volumeInfo.has("title")) {
                                b.setTitle(volumeInfo.getString("title"));
                                if (volumeInfo.has("authors")){

                                    JSONArray authors = volumeInfo.getJSONArray("authors");
                                    for (int j = 0; j < authors.length(); j++) {
                                        author.append(authors.getString(j));
                                        if (authors.length() > 1 && j < author.length() - 1) author.append(", ");

                                    }

                                }
                                if (volumeInfo.has("description")) b.setOverview(volumeInfo.getString("description"));
                                if (volumeInfo.has("publisher")) b.setPublisher(volumeInfo.getString("publisher"));
                                if (volumeInfo.has("publishedDate")) b.setReleaseDate(volumeInfo.getString("publishedDate"));
                                System.out.println(b.getOverview());
                                b.setAuthor(author.toString());
                                lis.add(b);
                            }
                            if (!max_result.equals("all") && i == Integer.parseInt(max_result)) break;

                        }
                    }
                    return lis;


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return lis;
    }

    public static LinkedList<MusicInfo> musicGetInfo(String name, String max_result) throws Exception{
        String name_request=name.replace(" ","_");
        LinkedList<MusicInfo> lis=new LinkedList<MusicInfo>();
        HttpResponse<JsonNode> jsonResponse = Unirest.get("https://api.discogs.com/database/search?q="+name_request+"&format=FILE,MP3,Single&token="+MyAPIKey.getDiscogs_api()).asJson();
        JSONObject jsonObject= new JSONObject(jsonResponse.getBody());
        System.out.println(jsonObject);

        JSONArray jArray = jsonObject.getJSONArray("array");

        int iteration=0;
        for(int i = 0; i < jArray.length(); i++) {
            JSONArray resultInfo = jArray.getJSONObject(i).getJSONArray("results");
            for(int k = 0; k < resultInfo.length(); k++) {
                JSONObject result=resultInfo.getJSONObject(k);
                MusicInfo b=new MusicInfo();
                b.setTitle(result.getString("title"));
                JSONArray genres = result.getJSONArray("genre");
                StringBuilder genre_build = new StringBuilder();
                for (int j = 0; j < genres.length(); j++) {
                    genre_build.append(genres.getString(i));
                    if (j > 0) genre_build.append(", ");
                }

                JSONArray labels = result.getJSONArray("label");
                StringBuilder label_build = new StringBuilder();
                for (int j = 0; j < labels.length(); j++) {
                    label_build.append(labels.getString(i));
                    if (j > 0) label_build.append(", ");
                }
                b.setLabels(label_build.toString());
                b.setGenre(genre_build.toString());
                b.setReleaseDate(result.getString("year"));
                lis.add(b);
                if(iteration==Integer.parseInt(max_result)&& !max_result.equals("all")) break;
                iteration++;


            }
        }


        return lis;
    }


    public static LinkedList<FilmInfo> filmGetInfo(String name, String max_result) throws Exception {
        LinkedList<FilmInfo> lis = new LinkedList<FilmInfo>();
        int iteration=0;
        HttpClient httpClient = new DefaultHttpClient();
        String name_request = name.replace(" ", "%20");
        HttpResponse<JsonNode> jsonResponse = Unirest.get("https://api.themoviedb.org/3/search/movie?api_key=" + MyAPIKey.getThemoviedb_api() + "&query=" + name_request).asJson();
        JSONObject jsonObject = new JSONObject(jsonResponse.getBody());
        System.out.println(jsonObject);
        JSONArray array = jsonObject.getJSONArray("array");
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

                if (iteration == Integer.parseInt(max_result) && !max_result.equals("all")) break;
                iteration++;

            }
        }

        return lis;
    }


    public static LinkedList<GameInfo> gameGetInfo(String name,String max_result){
       LinkedList<GameInfo> lis=new LinkedList<GameInfo>();
       String name_requested=name.replace(" ","%20");
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://api-2445582011268.apicast.io/games/?search="+name_requested+"&fields=name,summary,aggregated_rating,websites,pegi,first_release_date")
                    .header("user-key", MyAPIKey.getIGDB())
                    .header("Accept", "application/json")
                    .asJson();
            System.out.println(response.getBody());
            JSONObject jsonObject=new JSONObject(response.getBody());
            JSONArray jarray= jsonObject.getJSONArray("array");
            for (int i=0;i<jarray.length();i++){
                JSONObject gameInfo=jarray.getJSONObject(i);
                GameInfo b =new GameInfo();
                if(gameInfo.has("name")) b.setTitle(gameInfo.getString("name"));
                if(gameInfo.has("aggregated_rating")) b.setVote(String.valueOf(gameInfo.getDouble("aggregated_rating")));
                if(gameInfo.has("summary")) b.setOverview(gameInfo.getString("summary"));
                MediaOperations.parsePEGI(b,gameInfo);
                MediaOperations.parseWEB(b,gameInfo);
                if(gameInfo.has("first_release_date")){
                    java.util.Date time=new java.util.Date((long)gameInfo.getDouble("first_release_date")*1000);
                    String dt=time.toString();
                    String[] split = dt.split("CEST");
                    String firstSubString = split[0];
                    b.setReleaseDate(firstSubString);
                }
                lis.add(b);
                if(!max_result.equals("all") && i== Integer.parseInt(max_result)) break;
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return lis;




    }











}
