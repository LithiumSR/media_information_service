package core;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import framework.*;
import mediacontent.*;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Controller
public class MainController {

    @GetMapping("/media_book")
    public String mediaBookForm(Model model) {
        Media b = new Media();
        model.addAttribute("media", b);
        return "media_book";
    }

    @GetMapping("/media_music")
    public String mediaMusicForm(Model model) {
        Media b = new Media();
        model.addAttribute("media", b);
        return "media_music";
    }

    @GetMapping("/media_film")
    public String mediaFilmForm(Model model) {
        Media b = new Media();
        model.addAttribute("media", b);
        return "media_film";
    }


    @PostMapping("/media_book")
    public String mediaBookSubmit(@ModelAttribute Media media, Model model) {
        System.out.println(media.getISBN());
        LinkedList<BookInfo> a = ApiOperations.bookGetInfo(media.getTitle(), media.getISBN(), "4");
        model.addAttribute("mediaList", a);
        return "result_book";
    }


    @PostMapping("/media_film")
    public String mediaFilmSubmit(@ModelAttribute Media media, Model model) {
        LinkedList<FilmInfo> a = null;
        try {
            a = ApiOperations.filmGetInfo(media.getTitle(), "4");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).toString();
        }

        model.addAttribute("mediaList", a);
        return "result_film";
    }

    @PostMapping("/media_music")
    public String mediaMusicSubmit(@ModelAttribute Media media, Model model) {
        LinkedList<MusicInfo> a = null;
        try {
            System.out.println("music found");
            a = ApiOperations.musicGetInfo(media.getTitle(), "4");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).toString();
        }
        model.addAttribute("mediaList", a);
        return "result_music";
    }

    @ResponseBody
    @RequestMapping(value = "/drivecallback", method = {RequestMethod.GET, RequestMethod.POST})
    public String driveFlow(@RequestParam(value = "code", defaultValue = "nope") String code) {
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
        ClientResponse response1 = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData);
        String json = response1.getEntity(String.class);
        System.out.println(json);
        JSONObject jsonObj = new JSONObject(json);
        String token=jsonObj.getString("access_token");
        System.out.println(token);
        List<FilmInfo> films=new LinkedList<FilmInfo>();
        List<BookInfo> books=new LinkedList<BookInfo>();
        List<MusicInfo> songs=new LinkedList<MusicInfo>();

        try {
            List<String> names= GDrvApiOp.retrieveAllFiles(token,"media");
            films=new LinkedList<FilmInfo>();
            books=new LinkedList<BookInfo>();
            songs=new LinkedList<MusicInfo>();
            MediaOperations.findMediaInfo(names,books,films,songs);
            System.out.println(films.toString());
            System.out.println(books.toString());
            System.out.println(songs.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "<h2 style=\"color: #2e6c80;\">These are the results of the files in the media folder:</h2>\n" +
                "<br>"+
                "<h3><span style=\"color: #ff0000;\"><strong>Films:</strong></span></h3>"+
                MediaOperations.generateHTMLFilm(films)+
                "<h3><span style=\"color: #ff0000;\"><strong>Books:</strong></span></h3>"+
                MediaOperations.generateHTMLBook(books)+
                "<h3><span style=\"color: #ff0000;\"><strong>Songs:</strong></span></h3>"+
                MediaOperations.generateHTMLMusic(songs);

    }

    @ResponseBody
    @RequestMapping(value = "/dropboxcallback", method = {RequestMethod.GET, RequestMethod.POST})
    public String dropboxFlow(@RequestParam(value = "code", defaultValue = "nope") String code) {
        System.out.println(code);
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
        ClientResponse response1 = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData);
        String json = response1.getEntity(String.class);
        JSONObject jsonObj = new JSONObject(json);
        System.out.println(jsonObj.toString());
        String token=jsonObj.getString("access_token");
        System.out.println(token);
        List<String> names=DbxAPIOp.dropboxGetFiles(token);
        List<FilmInfo> films=new LinkedList<FilmInfo>();
        List<BookInfo> books=new LinkedList<BookInfo>();
        List<MusicInfo> songs=new LinkedList<MusicInfo>();
        try {
            MediaOperations.findMediaInfo(names,books,films,songs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(films.toString());
        System.out.println(books.toString());
        System.out.println(songs.toString());

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







