package core;

import framework.ApiOperations;
import mediacontent.Authentication;
import mediacontent.Greeting;
import mediacontent.Media;
import mediacontent.MediaInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.LinkedList;
import java.util.List;

@Controller
public class MainController {

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        Greeting b= new Greeting();
        ApiOperations.gameGetInfoTest("Destiny_2");
        model.addAttribute("greeting", b);
        return "greeting";
    }

    @RequestMapping(value="/search", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody MediaInfo searchRequest(@RequestParam(value="type") String type, @RequestParam(value="query") String name) {
        LinkedList<MediaInfo> ret;
        if(type.equals("book")) ret=ApiOperations.bookGetInfo(name,"0","1");
        else if (type.equals("film"))  ret=ApiOperations.filmGetInfo(name,"1");
        else if (type.equals("music")) ret=ApiOperations.musicGetInfo(name,"1");
        else return null;
        return ret.getFirst();
    }


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
        LinkedList<MediaInfo> a=ApiOperations.bookGetInfo(media.getTitle(),media.getISBN(),"1");
        model.addAttribute("media_info", a.getFirst());
        return "result_media";
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







