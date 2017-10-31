package core;

import com.mashape.unirest.http.exceptions.UnirestException;
import framework.ApiOperations;
import mediacontent.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

@Controller
public class MainController {

    //Book form
    @GetMapping("/media_book")
    public String mediaBookForm(Model model,HttpServletRequest request ) {
        Media b = new Media();
        model.addAttribute("media", b);
        return "media_book";
    }

    //Music form
    @GetMapping("/media_music")
    public String mediaMusicForm(Model model) {
        Media b = new Media();
        model.addAttribute("media", b);
        return "media_music";
    }

    //Film form
    @GetMapping("/media_film")
    public String mediaFilmForm(Model model) {
        Media b = new Media();
        model.addAttribute("media", b);
        return "media_film";
    }

    //Game form
    @GetMapping("/media_game")
    public String mediaGameForm(Model model) {
        Media b = new Media();
        model.addAttribute("media", b);
        return "media_game";
    }

    //Game results
    @PostMapping("/media_game")
    public String mediaGameSubmit(@ModelAttribute Media media, Model model, HttpServletRequest request ) {
        LinkedList<GameInfo> a = null;
        try {
            a = ApiOperations.gameGetInfo(media.getTitle(),"10","");
        } catch (UnirestException e) {
            e.printStackTrace();
            return String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        RabbitSend.send("Game Request by "+request.getRemoteAddr()+" "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date())+ " : - " +"TITLE:" + media.getTitle()+"\n");
        model.addAttribute("mediaList", a);
        return "result_game";
    }

    //Book results
    @PostMapping("/media_book")
    public String mediaBookSubmit(@ModelAttribute Media media, Model model, HttpServletRequest request ) {

        System.out.println(media.getISBN());
        LinkedList<BookInfo> a = null;
        if(media.getTitle().equals("") && media.getISBN().equals("")) return "media_book";
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

    //Films results
    @PostMapping("/media_film")
    public String mediaFilmSubmit(@ModelAttribute Media media, Model model, HttpServletRequest request ) {
        LinkedList<FilmInfo> a = null;
        String maxResult= media.getMaxResult();
        if (maxResult.equals("")) maxResult="all";
        try {
            a = ApiOperations.filmGetInfo(media.getTitle(), maxResult,media.getLanguage(),media.getYear());
        } catch (UnirestException e) {
            e.printStackTrace();
            return String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        RabbitSend.send("Film Request by "+request.getRemoteAddr()+" "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date())+ " : - " +"TITLE: " + media.getTitle()+"\n");

        model.addAttribute("mediaList", a);
        return "result_film";
    }

    //Music results
    @PostMapping("/media_music")
    public String mediaMusicSubmit(@ModelAttribute Media media, Model model, HttpServletRequest request ) {
        LinkedList<MusicInfo> a = null;
        try {
            a = ApiOperations.musicGetInfo(media.getTitle(), "10","FILE,MP3,Single");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).toString();
        }
        RabbitSend.send("Music Request by "+request.getRemoteAddr()+" "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date())+ " : - " +"TITLE: " + media.getTitle()+"\n");
        model.addAttribute("mediaList", a);
        return "result_music";
    }



}







