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

@Controller
public class MainController {

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        Greeting b= new Greeting();
        ApiOperations.gameGetInfoTest("Destiny_2");
        model.addAttribute("greeting", b);
        return "greeting";
    }

    @RequestMapping("/search")
    public Greeting greeting(@RequestParam(value="type") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
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
        MediaInfo a=ApiOperations.bookGetInfo(media.getTitle(),media.getISBN());
        model.addAttribute("media_info", a);
        return "result_media";
    }


    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Greeting greeting) {
        if (greeting.getContent().equals("Ciao ciao")) return "redirect:/";
        return "result";
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
        String client_id_web="662415523952-dt6g147rh7h9euohb22tjh3mi7f2uphf.apps.googleusercontent.com";
        String client_secret_web="Os0NmmxZGRTVwtkXab_1LWZK";

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







