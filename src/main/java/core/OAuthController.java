package core;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import framework.DbxAPIOp;
import framework.GDrvApiOp;
import framework.MediaOperations;
import framework.MyAPIKey;
import mediacontent.BookInfo;
import mediacontent.FilmInfo;
import mediacontent.MusicInfo;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
public class OAuthController {

    //Google Drive flow
    @ResponseBody
    @RequestMapping(value = "/drivecallback", method = {RequestMethod.GET, RequestMethod.POST})
    public String driveFlow(@RequestParam(value = "code", defaultValue = "nope") String code, HttpServletRequest request) {
        if (code.equals("nope")) {

            System.out.println("Errore, code vuoto");
        }
        String client_id_web = MyAPIKey.getDrive_id();
        String client_secret_web =MyAPIKey.getDrive_secret();

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource webResource = client.resource(UriBuilder.fromUri("https://www.googleapis.com/oauth2/v4/token").build());
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("code", code);
        formData.add("client_id", client_id_web);
        formData.add("redirect_uri", "http://localhost:8080/drivecallback");
        formData.add("client_secret",
                client_secret_web);
        formData.add("grant_type", "authorization_code");
        ClientResponse response1 = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData); //Exchange code for token
        String json = response1.getEntity(String.class);
        JSONObject jsonObj = new JSONObject(json);
        String token=jsonObj.getString("access_token");
        List<FilmInfo> films=new LinkedList<FilmInfo>();
        List<BookInfo> books=new LinkedList<BookInfo>();
        List<MusicInfo> songs=new LinkedList<MusicInfo>();
        try {
            List<String> names= GDrvApiOp.retrieveAllFiles(token,"media"); //get files name
            RabbitSend.send("Google Drive request by "+request.getRemoteAddr()+" "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                    .format(new Date())+ " : \n - " +"Files: " + MediaOperations.getFilesName(names)+"\n");
            films=new LinkedList<FilmInfo>();
            books=new LinkedList<BookInfo>();
            songs=new LinkedList<MusicInfo>();
            MediaOperations.findMediaInfo(names,books,films,songs); //Find info about files
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Generate HTML result Page
        return "<h2 style=\"color: #2e6c80;\">These are the results of the files in the media folder:</h2>\n" +
                "<br>"+
                "<h3><span style=\"color: #ff0000;\"><strong>Films:</strong></span></h3>"+
                MediaOperations.generateHTMLFilm(films)+
                "<h3><span style=\"color: #ff0000;\"><strong>Books:</strong></span></h3>"+
                MediaOperations.generateHTMLBook(books)+
                "<h3><span style=\"color: #ff0000;\"><strong>Songs:</strong></span></h3>"+
                MediaOperations.generateHTMLMusic(songs);

    }

    //Dropbox flow
    @ResponseBody
    @RequestMapping(value = "/dropboxcallback", method = {RequestMethod.GET, RequestMethod.POST})
    public String dropboxFlow(@RequestParam(value = "code", defaultValue = "nope") String code, HttpServletRequest request) {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource webResource = client.resource(UriBuilder.fromUri("https://api.dropboxapi.com/oauth2/token").build());
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("code", code);
        formData.add("client_id", MyAPIKey.getDropbox_id());
        formData.add("redirect_uri", "http://localhost:8080/dropboxcallback");
        formData.add("client_secret",
                MyAPIKey.getDropbox_secret());
        formData.add("grant_type", "authorization_code");
        ClientResponse response1 = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData); //Exchange code for token
        String json = response1.getEntity(String.class);
        JSONObject jsonObj = new JSONObject(json);
        String token=jsonObj.getString("access_token");
        List<String> names= DbxAPIOp.dropboxGetFiles(token); //Get files name
        RabbitSend.send("Dropbox request by "+request.getRemoteAddr()+" "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date())+ " : \n - " +"Files: " + MediaOperations.getFilesName(names)+"\n");
        List<FilmInfo> films=new LinkedList<FilmInfo>();
        List<BookInfo> books=new LinkedList<BookInfo>();
        List<MusicInfo> songs=new LinkedList<MusicInfo>();
        try {
            MediaOperations.findMediaInfo(names,books,films,songs); //Find info about files
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Generate HTML result page
        return "<h2 style=\"color: #2e6c80;\">These are the results of the files in the media folder:</h2>\n" +
                "<br>"+
                "<h3><span style=\"color: #ff0000;\"><strong>Films:</strong></span></h3>"+
                MediaOperations.generateHTMLFilm(films)+
                "<h3><span style=\"color: #ff0000;\"><strong>Books:</strong></span></h3>"+
                MediaOperations.generateHTMLBook(books)+
                "<h3><span style=\"color: #ff0000;\"><strong>Songs:</strong></span></h3>"+
                MediaOperations.generateHTMLMusic(songs);

    }
}
