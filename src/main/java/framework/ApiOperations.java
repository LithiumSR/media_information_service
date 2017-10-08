package framework;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.deploy.util.SessionState;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import mediacontent.Media;
import mediacontent.MediaInfo;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

import java.net.Socket;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ApiOperations {

   /** public static void main(String [ ] args)
    {
        //gameGetInfoTest("Destiny_2");
        MediaInfo b=bookGetInfo("Harry Potter e la camera dei segreti","0");
        System.out.println(b.toString());
        try {
            Media a=filmGetInfo("Harry Potter");
            System.out.println(a.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        musicGetInfo("fall out boy centuries");

    }
    **/


    public static MediaInfo filmGetInfo(String name) throws JSONException {

        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet httpGetRequest = new HttpGet("https://api.themoviedb.org/3/search/movie?api_key="+MyAPIKey.getThemoviedb_api()+"&query="+name);

            org.apache.http.HttpResponse httpResponse = httpClient.execute(httpGetRequest);
            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getStatusLine());
            System.out.println("----------------------------------------");

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
                    System.out.println(json_string);
                    try { inputStream.close(); } catch (Exception ignore) {}
                    JSONObject jsonObject= new JSONObject(json_string);
                    System.out.println(json_string);
                    JSONArray jArray = jsonObject.getJSONArray("results");
                    StringBuilder author=new StringBuilder();
                    String title ="";
                    String summary="";
                    String release_date="";

                    for(int i = 0; i < jArray.length(); i++)
                    {
                            JSONObject filmInfo = jArray.getJSONObject(i);
                            title = filmInfo.getString("title");
                            System.out.println(title);
                            summary=filmInfo.getString("overview");
                            release_date=filmInfo.getString("release_date");
                            break;

                        }
                    MediaInfo b=new MediaInfo();
                    b.setTitle(title);
                    b.setAuthor(author.toString());
                    b.setSummary(summary);
                    b.setReleaseDate(release_date);
                    return b;
                    }



                }
            } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return new MediaInfo();
    }



    public static MediaInfo bookGetInfo(String name,String ISBN){

        java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(Level.OFF);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "ERROR");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "ERROR");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "ERROR");

        HttpClient httpClient = new DefaultHttpClient();
        String name_request=name.replace(" ","+");

        try {
            HttpGet httpGetRequest = new HttpGet("https://www.googleapis.com/books/v1/volumes?q="+name_request+"&maxResults=1&projection=lite&orderBy=relevance");

            org.apache.http.HttpResponse httpResponse = httpClient.execute(httpGetRequest);

            Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.SEVERE);
            Logger.getLogger("httpclient").setLevel(Level.SEVERE);

            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getStatusLine());
            System.out.println("----------------------------------------");

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
                    JSONArray jArray = jsonObject.getJSONArray("items");
                    StringBuilder author=new StringBuilder();
                    String title ="";
                    String summary="";
                    String publisher="";
                    String date="";
                    String GID="";
                    System.out.println(json_string);
                    for(int i = 0; i < jArray.length(); i++)
                    {
                        if (i==0) GID=jArray.getJSONObject(i).getString("id");
                        JSONObject volumeInfo = jArray.getJSONObject(i).getJSONObject("volumeInfo");
                        title = volumeInfo.getString("title");
                        JSONArray authors = volumeInfo.getJSONArray("authors");
                        if(volumeInfo.has("description")) summary=volumeInfo.getString("description");
                        publisher=volumeInfo.getString("publisher");
                        date=volumeInfo.getString("publishedDate");


                        for(int j =0; j< authors.length(); j++) {
                            author.append(authors.getString(i));
                            if(j>0) author.append(", ");
                        }

                    }
                    MediaInfo b=new MediaInfo();
                    b.setTitle(title);
                    b.setAuthor(author.toString());
                    b.setSummary(summary);
                    b.setISBN(GID);
                    b.setReleaseDate(date);
                    b.setPublisher(publisher);
                    return b;


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return new MediaInfo();
    }

    public static MediaInfo musicGetInfo(String name){
        String name_request=name.replace(" ","_");
        String genre="";
        String title="";
        String label="";
        String date="";
        try {
                HttpResponse<JsonNode> jsonResponse = Unirest.get("https://api.discogs.com/database/search?q="+name_request+"&format=FILE,MP3,Single&token="+MyAPIKey.getDiscogs_api()).asJson();
                JSONObject jsonObject= new JSONObject(jsonResponse.getBody());
                System.out.println(jsonObject);

                JSONArray jArray = jsonObject.getJSONArray("array");


            for(int i = 0; i < jArray.length(); i++) {
                JSONArray resultInfo = jArray.getJSONObject(i).getJSONArray("results");
                for(int k = 0; i < resultInfo.length(); k++) {
                    JSONObject result=resultInfo.getJSONObject(k);
                    title = result.getString("title");
                    System.out.println(title);
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
                    label = label_build.toString();
                    date=result.getString("year");
                    break;


                }
                break;
            }
            MediaInfo b=new MediaInfo();
            b.setTitle(title);
            b.setPublisher(label);
            b.setReleaseDate(date);
            return b;

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return new MediaInfo();
    }


    public static void gameGetInfoTest2(String name){
        Client client = Client.create();
        client.setFollowRedirects(true);
        WebResource webResource = client
                .resource("http://v2000.igdb.com/games?search=Destiny_2&fields=name,publishers");
        ClientResponse response = webResource.header("user-key",MyAPIKey.getIGDB()).accept("application/json")
                .get(ClientResponse.class);

        if (response.getStatus() == 200) {
            String output = response.getEntity(String.class);

            System.out.println("Output from Server .... \n");
            System.out.println(output);
        }
        else if (response.getStatus()==301){
            System.out.println("testo");
            String location = response.getHeaders().getFirst("Location");
            System.out.println(location);
            WebResource webResource2 = client
                    .resource(location);

            ClientResponse response2 = webResource.header("user-key",MyAPIKey.getIGDB()).accept("application/json")
                    .get(ClientResponse.class);
            System.out.println("secondo:" +response2.getStatus());
            System.out.println(response2.getHeaders());
            System.out.println(response.getEntity(String.class));


        }





    }
    public static void gameGetInfoTest(String name){
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("https://api-2445582011268.apicast.io/games?search=Destiny_2&fields=name,publishers");
        request.setHeader("user-key",MyAPIKey.getIGDB());
        org.apache.http.HttpResponse response = null;

        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader rd = null;
        try {
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line="";
        try {
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

        /**
        HttpClient httpClient = new DefaultHttpClient();

        try {
            HttpGet httpGetRequest = new HttpGet("https://api-2445582011268.apicast.io/games"+"?search="+ name + "&fields=name,publishers");
            httpGetRequest.setHeader("user-key",MyAPIKey.getIGDB());
            httpGetRequest.setHeader("Accept","application/json");
            httpGetRequest.setHeader("User-Agent","python-requests/0.12.1");
            System.out.println(httpGetRequest.getAllHeaders());
            System.out.println(httpGetRequest.getConfig());
            HttpResponse httpResponse = httpClient.execute(httpGetRequest);

            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getStatusLine());
            System.out.println("----------------------------------------");

            HttpEntity entity = httpResponse.getEntity();

            byte[] buffer = new byte[1024];
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                try {
                    int bytesRead = 0;
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        String chunk = new String(buffer, 0, bytesRead);
                        System.out.println(chunk);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try { inputStream.close(); } catch (Exception ignore) {}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**public static String gameGetInfo(String name) {
        System.out.println("inizio");
        String api = "https://api-2445582011268.apicast.io/games";
        String title = name.replace("_", " ");
        String request = api + "?search=" + name + "&fields=name,publishers";

        URL myURL = null;
        try {
            myURL = new URL(request);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection myURLConnection = null;

        try {
            myURLConnection = (HttpURLConnection) myURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(request);
        myURLConnection.setRequestProperty("user-key", MyAPIKey.getIGDB());
        myURLConnection.setRequestProperty("Accept", "application/json");
        try {
            myURLConnection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        myURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        myURLConnection.setUseCaches(false);
        myURLConnection.setDoInput(true);
        myURLConnection.setDoOutput(true);
        try {
            myURLConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("test");
        BufferedReader br = null;
        StringBuilder builder = new StringBuilder();
        try {
            builder.append(myURLConnection.getResponseCode())
                    .append(" ")
                    .append(myURLConnection.getResponseMessage())
                    .append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(builder.toString());
        try {
            br = new BufferedReader(new InputStreamReader((myURLConnection.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
                System.out.println(output);

            }
            System.out.println(sb.toString());
            return sb.toString();

        } catch(IOException e){
            e.printStackTrace();
        }

        return "test";
    }
     **/

}
