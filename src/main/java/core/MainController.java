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
import org.springframework.web.servlet.view.RedirectView;

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

    @GetMapping("/webchat")
    public String chatLoad() {
        return "chat";
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
    @PostMapping("/result_game")
    public String mediaGameSubmit(@ModelAttribute Media media, Model model, HttpServletRequest request ) {
        LinkedList<GameInfo> a = null;
        String maxResult= media.getMaxResult();
        if(media.getTitle().equals("")) return "media_game";
        if (maxResult.equals("")) maxResult="all";
        try {
            a = ApiOperations.gameGetInfo(media.getTitle(),maxResult,media.getOrderBy());
        } catch (UnirestException e) {
            e.printStackTrace();
            return String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(Application.getConfig().equals("DEFAULT")||Application.getConfig().equals("HEROKU")) RabbitSend.send("Game Request by "+request.getRemoteAddr()+" "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date())+ " : - " +"TITLE:" + media.getTitle()+"\n","MSI_Info");
        model.addAttribute("mediaList", a);
        return "result_game";
    }

    //Book results
    @PostMapping("/result_book")
    public String mediaBookSubmit(@ModelAttribute Media media, Model model, HttpServletRequest request ) {

        //System.out.println(media.getISBN());
        LinkedList<BookInfo> a = null;
        String maxResult= media.getMaxResult();
        if (maxResult.equals("")) maxResult="all";
        if (media.getTitle().equals("") && media.getISBN().equals("")) return "media_book";
        else if (media.getTitle().equals("") && media.getISBN().length()!=13) return "media_book";
        try {
            a = ApiOperations.bookGetInfo(media.getTitle(), media.getISBN(), maxResult, media.getOrderBy());
        } catch (UnirestException e) {
            e.printStackTrace();
            return String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);

        }
        model.addAttribute("mediaList", a);
        if(Application.getConfig().equals("DEFAULT")||Application.getConfig().equals("HEROKU")) RabbitSend.send("Book request by "+request.getRemoteAddr()+" "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date())+ " : - " +"TITLE: " + media.getTitle()+ ", ISBN: "+media.getISBN()+"\n","MSI_Info");
        return "result_book";
    }

    //Films results
    @PostMapping("/result_film")
    public String mediaFilmSubmit(@ModelAttribute Media media, Model model, HttpServletRequest request ) {
        LinkedList<FilmInfo> a = null;
        String maxResult= media.getMaxResult();
        if(media.getTitle().equals("")) return "media_film";
        if (maxResult.equals("")) maxResult="all";
        String languagecode=media.getLanguage();
        if (languagecode.length()!=2) languagecode="";
        try {
            a = ApiOperations.filmGetInfo(media.getTitle(), maxResult,languagecode,media.getYear(),media.getOrderBy());
        } catch (UnirestException e) {
            e.printStackTrace();
            return String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(Application.getConfig().equals("DEFAULT")||Application.getConfig().equals("HEROKU")) RabbitSend.send("Film Request by "+request.getRemoteAddr()+" "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date())+ " : - " +"TITLE: " + media.getTitle()+"\n","MSI_Info");

        model.addAttribute("mediaList", a);
        return "result_film";
    }

    //Music results
    @PostMapping("/result_music")
    public String mediaMusicSubmit(@ModelAttribute Media media, Model model, HttpServletRequest request ) {
        LinkedList<MusicInfo> a = null;
        String maxResult= media.getMaxResult();
        if (maxResult.equals("")) maxResult="all";
        if(media.getTitle().equals("") && media.getAuthor().equals("")) return "media_music";
        try {
            a = ApiOperations.musicGetInfo(media.getTitle(), maxResult,"FILE,MP3,Single",media.getOrderBy(),media.getAuthor(),media.getYear());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).toString();
        }
        if(Application.getConfig().equals("DEFAULT")||Application.getConfig().equals("HEROKU")) RabbitSend.send("Music Request by "+request.getRemoteAddr()+" "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date())+ " : - " +"TITLE: " + media.getTitle()+"\n","MSI_Info");
        model.addAttribute("mediaList", a);
        return "result_music";
    }


    @GetMapping("/result_book")
    public RedirectView redirectBookForm(HttpServletRequest request ) {
        return new RedirectView("media_book");
    }

    @GetMapping("/result_game")
    public RedirectView redirectGameForm(HttpServletRequest request ) {
        return new RedirectView("media_game");
    }

    @GetMapping("/result_film")
    public RedirectView redirectFilmForm(HttpServletRequest request ) {
        return new RedirectView("media_film");
    }

    @GetMapping("/result_music")
    public RedirectView redirectMusicForm(HttpServletRequest request ) {
        return new RedirectView("media_music");
    }


}







