package core;

import framework.ApiOperations;
import mediacontent.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.LinkedList;
import java.util.List;

@Controller
public class MainController {

    @GetMapping("/media_book")
    public String mediaBookForm(Model model) {
        Media b= new Media();
        model.addAttribute("media", b);
        return "media_book";
    }

    @GetMapping("/media_music")
    public String mediaMusicForm(Model model) {
        Media b= new Media();
        model.addAttribute("media", b);
        return "media_music";
    }

    @GetMapping("/media_film")
    public String mediaFilmForm(Model model) {
        Media b= new Media();
        model.addAttribute("media", b);
        return "media_film";
    }



    @PostMapping("/media_book")
    public String mediaBookSubmit(@ModelAttribute Media media, Model model) {
        LinkedList<BookInfo> a=ApiOperations.bookGetInfo(media.getTitle(),media.getISBN(),"4");
        model.addAttribute("mediaList", a);
        return "result_book";
    }



    @PostMapping("/media_film")
    public String mediaFilmSubmit(@ModelAttribute Media media, Model model) {
        LinkedList<FilmInfo> a= null;
        try {
            a = ApiOperations.filmGetInfo(media.getTitle(),"4");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).toString();
        }

        model.addAttribute("mediaList", a);
        return "result_film";
    }

    @PostMapping("/media_music")
    public String mediaMusicSubmit(@ModelAttribute Media media, Model model) {
        LinkedList<MusicInfo> a= null;
        try {
            a = ApiOperations.musicGetInfo(media.getTitle(),"4");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).toString();
        }
        model.addAttribute("mediaList", a);
        return "result_music";
    }






    @GetMapping("/authentication")
    public String authForm(Model model) {
        Authentication a= new Authentication();
        model.addAttribute("authentication", a);
        return "authentication";
    }

    @PostMapping("/authentication")
    public String authSubmit(@ModelAttribute Authentication a) {
        if (a.getContent().equals("Ciao ciao")) return "redirect:/";
        return "result_auth";
    }







    @RequestMapping(value = "/oauth2callback", method = {RequestMethod.GET, RequestMethod.POST})
    public Greeting oauthCodeGet(@RequestParam(value="code", defaultValue="nope") String code){
        if(code.equals("nope")) {

            System.out.println("Errore, code vuoto");
        }
        System.out.println("Code ricevuto: "+code);
        String client_id_web="";
        String client_secret_web="";

        String url = "http://www.googleapis.com/oauth2/v4/token?code="+code+"&client_id="+client_id_web+"&client_secret="+client_secret_web+"&redirect_uri=http://localhost:3000/oauthcallback&grant_type=authorization_code";

        Socket s = null;
        try {
            s = new Socket(url, 443);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter wtr = null;
        try {
            wtr = new PrintWriter(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Prints the request string to the output stream
        wtr.println("GET "+"/oauth2/v4/token?code="+code+"&client_id="+client_id_web+"&client_secret="+client_secret_web+"&redirect_uri=http://localhost:3000/oauthcallback&grant_type=authorization_code"+ " HTTP/1.1");
        wtr.println("Host: www.googleapis.com");
        wtr.println("");
        wtr.flush();

        BufferedReader bufRead = null;
        try {
            bufRead = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String outStr;

        //Prints each line of the response
        try {
            while((outStr = bufRead.readLine()) != null){
                System.out.println(outStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new Greeting();



        }

        }







