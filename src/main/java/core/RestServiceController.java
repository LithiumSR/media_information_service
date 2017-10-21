package core;

import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import framework.ApiOperations;
import framework.BadStatus;
import mediacontent.BookInfo;
import mediacontent.FilmInfo;
import mediacontent.GameInfo;
import mediacontent.MusicInfo;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;

@Controller
public class RestServiceController {

    //Endpoint search with parameter query (required)
    @RequestMapping(value="/search", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String searchRequest(@RequestParam(value="type") String type, @RequestParam(value="query") String name) {
        String ret;

        try {
        if(type.equals("book")) {
            ret=new Gson().toJson(ApiOperations.bookGetInfo(name,"0","all","relevance"));
        }
        else if (type.equals("film")) ret = new Gson().toJson(ApiOperations.filmGetInfo(name, "all",""));

        else if (type.equals("music")) ret=new Gson().toJson(ApiOperations.musicGetInfo(name,"all","FILE,MP3,Single"));
        else if (type.equals("game")) ret=new Gson().toJson(ApiOperations.gameGetInfo(name,"all",""));
        else return null;
        return ret;
        } catch (UnirestException e) {
            e.printStackTrace();
            return new Gson().toJson(new BadStatus("Internal error"));
        }

    }


    //Endpoint /film/search with parameters query (required), max_result(option,default=all), language(optional, must use ISO 639-1 value)
    @RequestMapping(value="/film/search", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String searchFilmRequest(@RequestParam(value="type") String type, @RequestParam(value="query") String name,
                                                  @RequestParam(value="max_result",required = false, defaultValue="all") String max_result,
                                                  @RequestParam(value="language",required = false, defaultValue="") String language
    ) {
        LinkedList<FilmInfo> lis;
        try {
            lis=ApiOperations.filmGetInfo(name,max_result,language);
        } catch (UnirestException e) {
            return new Gson().toJson(new BadStatus("Internal Error"));
        } catch (JSONException e) {
            return new Gson().toJson(new BadStatus("Bad request"));
        }
        return new Gson().toJson(lis);

    }

    //Endpoint /music/search with parameters query (required), max_result(option,default=all), type(optional, default:FILE,MP3,Single)
    @RequestMapping(value="/music/search", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String searchMusicRequest(@RequestParam(value="query") String name, @RequestParam(value="max_result",required = false, defaultValue="all") String max_result,
                                                   @RequestParam(value="type",required = false,defaultValue = "FILE,MP3,Single") String type) {
        String ret;
        LinkedList<MusicInfo> lis;
        try {
            lis=ApiOperations.musicGetInfo(name,max_result,type);
        } catch (UnirestException e) {
            e.printStackTrace();
            return new Gson().toJson(new BadStatus("Internal Error"));
        }
        catch (JSONException e) {
            e.printStackTrace();
            return new Gson().toJson(new BadStatus("Bad Request"));
        }
        return new Gson().toJson(lis);

    }

    //Endpoint /book/search with parameters query (required), max_result(option,default=all), ISBN(optional, default:0), orderBy(optional,default: relevance)
    @RequestMapping(value="/book/search", method = RequestMethod.GET, produces = "application/json")
    public  @ResponseBody String searchBookRequest(@RequestParam(value="query") String name, @RequestParam(value="max_result",required = false, defaultValue="all") String max_result,
                                                   @RequestParam(value="isbn",required = false,defaultValue = "")String isbn, @RequestParam(value="orderBy",required = false,defaultValue = "relevance") String orderBy ) {
        String ret;
        LinkedList<BookInfo> lis;
        System.out.println(orderBy);
        try {
            lis=ApiOperations.bookGetInfo(name,isbn,max_result,orderBy);
        } catch (UnirestException e) {
            e.printStackTrace();
            return new Gson().toJson(new BadStatus("Internal Error"));
        }

        catch (JSONException e) {
        e.printStackTrace();
        return new Gson().toJson(new BadStatus("Bad Request"));
    }
        return new Gson().toJson(lis);

    }
    //Endpoint /game/search with parameters query (required), max_result(option,default=all), orderBy(optional,default: default IGDB order)
    @RequestMapping(value="/game/search", method = RequestMethod.GET, produces = "application/json")
    public  @ResponseBody String searchGameRequest(@RequestParam(value="query") String name, @RequestParam(value="max_result",required = false, defaultValue="all") String max_result,
                                                   @RequestParam(value="orderBy",required = false, defaultValue="") String orderBy) {
        LinkedList<GameInfo> lis;
        try {
            lis=ApiOperations.gameGetInfo(name,max_result,orderBy);
        } catch (UnirestException e) {
            e.printStackTrace();
            return new Gson().toJson(new BadStatus("Internal error"));
        }
        catch (JSONException e) {
            e.printStackTrace();
            return new Gson().toJson(new BadStatus("Bad request"));
        }

        return new Gson().toJson(lis);

    }



}
