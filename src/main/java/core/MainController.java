package core;

import com.mashape.unirest.http.exceptions.UnirestException;
import framework.APIOperations;
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
        if (maxResult.equals("")) maxResult="10";
        try {
            a = APIOperations.gameGetInfo(media.getTitle(),maxResult,media.getOrderBy());
        } catch (UnirestException e) {
            e.printStackTrace();
            return String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        RabbitSend.sendMediaRequest(media.getTitle(),"Game",request);
        if (a.size()==0) return "no_result";
        model.addAttribute("mediaList", a);
        return "result_game";
    }

    //Book results
    @PostMapping("/result_book")
    public String mediaBookSubmit(@ModelAttribute Media media, Model model, HttpServletRequest request ) {
        //System.out.println(media.getISBN());
        LinkedList<BookInfo> a = null;
        String maxResult= media.getMaxResult();
        if (maxResult.equals("")) maxResult="10";
        if (media.getTitle().trim().equals("") && media.getISBN().trim().equals("")) return "media_book";
        else if (media.getTitle().equals("") && media.getISBN().length()!=13) return "media_book";
        try {
            a = APIOperations.bookGetInfo(media.getTitle().trim(), media.getISBN().trim(), maxResult, media.getOrderBy());
        } catch (UnirestException e) {
            e.printStackTrace();
            return String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);

        }
        RabbitSend.sendMediaRequest(media.getTitle(),"Book",request);
        if (a.size()==0) return "no_result";
        model.addAttribute("mediaList", a);
        return "result_book";
    }

    //Films results
    @PostMapping("/result_film")
    public String mediaFilmSubmit(@ModelAttribute Media media, Model model, HttpServletRequest request ) {
        LinkedList<FilmInfo> a = null;
        String maxResult= media.getMaxResult();
        if(media.getTitle().equals("")) return "media_film";
        if (maxResult.equals("")) maxResult="10";
        String languagecode=media.getLanguage();
        if (languagecode.length()!=2) languagecode="";
        try {
            a = APIOperations.filmGetInfo(media.getTitle(), maxResult,languagecode,media.getYear(),media.getOrderBy());
        } catch (UnirestException e) {
            e.printStackTrace();
            return String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        RabbitSend.sendMediaRequest(media.getTitle(),"Film",request);
        if (a.size()==0) return "no_result";
        model.addAttribute("mediaList", a);
        return "result_film";
    }

    //Music results
    @PostMapping("/result_music")
    public String mediaMusicSubmit(@ModelAttribute Media media, Model model, HttpServletRequest request ) {
        LinkedList<MusicInfo> a = null;
        String maxResult= media.getMaxResult();
        if (maxResult.equals("")) maxResult="10";
        if(media.getTitle().equals("") && media.getAuthor().equals("")) return "media_music";
        try {
            if (media.getService().equals("itunes")) a = APIOperations.musicGetInfoItunes(media.getTitle(), maxResult,media.getOrderBy(),media.getAuthor(),media.getYear());
            else {
                a = APIOperations.musicGetInfoDiscogs(media.getTitle(), maxResult,"FILE,MP3,Single",media.getOrderBy(),media.getAuthor(),media.getYear());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).toString();
        }
        RabbitSend.sendMediaRequest(media.getTitle(),"Music",request);
        if (a.size()==0) return "no_result";
        model.addAttribute("mediaList", a);
        if (media.getService().equals("itunes")) return "result_music_itunes";
        else return "result_music";
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







