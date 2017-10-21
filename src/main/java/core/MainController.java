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
public class MainController {

    @GetMapping("/media_book")
    public String mediaBookForm(Model model,HttpServletRequest request ) {
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

    @GetMapping("/media_game")
    public String mediaGameForm(Model model) {
        Media b = new Media();
        model.addAttribute("media", b);
        return "media_game";
    }

    @PostMapping("/media_game")
    public String mediaGameSubmit(@ModelAttribute Media media, Model model, HttpServletRequest request ) {
        LinkedList<GameInfo> a = null;
        try {
            a = ApiOperations.gameGetInfo(media.getTitle(),"4","");
        } catch (UnirestException e) {
            e.printStackTrace();
            return String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        RabbitSend.send("Game Request by "+request.getRemoteAddr()+" "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date())+ " : - " +"TITLE:" + media.getTitle()+"\n");
        model.addAttribute("mediaList", a);
        return "result_game";
    }




    @PostMapping("/media_book")
    public String mediaBookSubmit(@ModelAttribute Media media, Model model, HttpServletRequest request ) {

        System.out.println(media.getISBN());
        LinkedList<BookInfo> a = null;
        try {
            a = ApiOperations.bookGetInfo(media.getTitle(), media.getISBN(), "4","relevance");
        } catch (UnirestException e) {
            e.printStackTrace();
            return String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);

        }
        model.addAttribute("mediaList", a);
        RabbitSend.send("Request by "+request.getRemoteAddr()+" "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date())+ " : - " +"TITLE: " + media.getTitle()+ ", ISBN: "+media.getISBN()+"\n");
        return "result_book";
    }


    @PostMapping("/media_film")
    public String mediaFilmSubmit(@ModelAttribute Media media, Model model, HttpServletRequest request ) {
        LinkedList<FilmInfo> a = null;
        try {
            a = ApiOperations.filmGetInfo(media.getTitle(), "4","");
        } catch (UnirestException e) {
            e.printStackTrace();
            return String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        RabbitSend.send("Film Request by "+request.getRemoteAddr()+" "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date())+ " : - " +"TITLE: " + media.getTitle()+"\n");

        model.addAttribute("mediaList", a);
        return "result_film";
    }

    @PostMapping("/media_music")
    public String mediaMusicSubmit(@ModelAttribute Media media, Model model, HttpServletRequest request ) {
        LinkedList<MusicInfo> a = null;
        try {
            a = ApiOperations.musicGetInfo(media.getTitle(), "4","FILE,MP3,Single");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).toString();
        }
        RabbitSend.send("Music Request by "+request.getRemoteAddr()+" "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date())+ " : - " +"TITLE: " + media.getTitle()+"\n");
        model.addAttribute("mediaList", a);
        return "result_music";
    }

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
        ClientResponse response1 = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData);
        String json = response1.getEntity(String.class);
        JSONObject jsonObj = new JSONObject(json);
        String token=jsonObj.getString("access_token");
        List<FilmInfo> films=new LinkedList<FilmInfo>();
        List<BookInfo> books=new LinkedList<BookInfo>();
        List<MusicInfo> songs=new LinkedList<MusicInfo>();
        try {
            List<String> names= GDrvApiOp.retrieveAllFiles(token,"media");
            RabbitSend.send("Google Drive request by "+request.getRemoteAddr()+" "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                    .format(new Date())+ " : \n - " +"Files:" + MediaOperations.getFilesName(names)+"\n");
            films=new LinkedList<FilmInfo>();
            books=new LinkedList<BookInfo>();
            songs=new LinkedList<MusicInfo>();
            MediaOperations.findMediaInfo(names,books,films,songs);
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
        ClientResponse response1 = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData);
        String json = response1.getEntity(String.class);
        JSONObject jsonObj = new JSONObject(json);
        String token=jsonObj.getString("access_token");
        List<String> names=DbxAPIOp.dropboxGetFiles(token);
        RabbitSend.send("Dropbox request by "+request.getRemoteAddr()+" "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date())+ " : \n - " +"Files:" + MediaOperations.getFilesName(names)+"\n");
        List<FilmInfo> films=new LinkedList<FilmInfo>();
        List<BookInfo> books=new LinkedList<BookInfo>();
        List<MusicInfo> songs=new LinkedList<MusicInfo>();
        try {
            MediaOperations.findMediaInfo(names,books,films,songs);
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

}







