package core;

import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import framework.APIOperations;
import framework.BadStatus;
import mediacontent.BookInfo;
import mediacontent.FilmInfo;
import mediacontent.GameInfo;
import mediacontent.MusicInfo;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public @ResponseBody ResponseEntity searchRequest(@RequestParam(value="type") String type, @RequestParam(value="query") String name, @RequestParam(value="max_result",required = false, defaultValue="all") String max_result,
                                                      @RequestParam(value="music_provider",required = false, defaultValue="itunes") String service) {
        String ret;
        try {
        if(type.equals("book")) {
            ret=new Gson().toJson(APIOperations.bookGetInfo(name,"0",max_result,"relevance"));
        }
        else if (type.equals("film")) ret = new Gson().toJson(APIOperations.filmGetInfo(name, max_result,"","",""));

        else if (type.equals("music")) ret=new Gson().toJson(APIOperations.itunesGetInfo(name,max_result,"relevance","relevance",""));

        else if (type.equals("game")) ret=new Gson().toJson(APIOperations.gameGetInfo(name,max_result,""));
        else  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson(new BadStatus("Illegal type value")));


        } catch (UnirestException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Gson().toJson(new BadStatus("Internal Error")));
        }

        catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Gson().toJson(new BadStatus("Internal Error")));
        }

        catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson(new BadStatus("Illegal value for max_result")));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ret);

    }


    //Endpoint /film/search with parameters query (required), max_result(option,default=all), language(optional, must use ISO 639-1 value)
    @RequestMapping(value="/film/search", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity searchFilmRequest(@RequestParam(value="query") String name,
                                                  @RequestParam(value="max_result",required = false, defaultValue="all") String max_result,
                                                  @RequestParam(value="language",required = false, defaultValue="") String language,
                                                          @RequestParam(value="release_year",required = false, defaultValue="") String year,
                                                          @RequestParam(value="orderBy",required = false, defaultValue="") String orderBy) {
        LinkedList<FilmInfo> lis;
        try {
            lis= APIOperations.filmGetInfo(name,max_result,language,year,orderBy);
        } catch (UnirestException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Gson().toJson(new BadStatus("Internal Error")));
        }

        catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Gson().toJson(new BadStatus("Internal Error")));
        }

        catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson(new BadStatus("Illegal value for max_result")));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Gson().toJson(lis));
    }

    //Endpoint /music/search with parameters query (required), max_result(option,default=all), type(optional, default:FILE,MP3,Single)
    @RequestMapping(value="/music/search", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity searchMusicRequest(@RequestParam(value="query") String name, @RequestParam(value="max_result",required = false, defaultValue="all") String max_result,
                                                   @RequestParam(value="type",required = false,defaultValue = "FILE,MP3,Single") String type,
                                                           @RequestParam(value="release_year",required = false,defaultValue = "") String year,
                                                           @RequestParam(value="artist",required = false,defaultValue = "") String artist,
                                                           @RequestParam(value="orderBy",required = false,defaultValue = "popularity") String orderBy,
                                                           @RequestParam(value="service",required = false,defaultValue = "itunes") String service) {
        String ret;
        LinkedList<MusicInfo> lis;
        try {
            if (service.toLowerCase().equals("itunes")) lis= APIOperations.itunesGetInfo(name,max_result,orderBy,artist,year);
            else if (service.toLowerCase().equals("discogs")) lis= APIOperations.musicGetInfo(name,max_result,type,orderBy,artist,year);
            else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson(new BadStatus("Bad Request")));
        } catch (UnirestException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Gson().toJson(new BadStatus("Internal Error")));
        }

        catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson(new BadStatus("Bad Request")));
        }

        catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson(new BadStatus("Illegal value for max_result")));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Gson().toJson(lis));

    }

    //Endpoint /book/search with parameters query (required), max_result(option,default=all), ISBN(optional, default:0), orderBy(optional,default: relevance)
    @RequestMapping(value="/book/search", method = RequestMethod.GET, produces = "application/json")
    public  @ResponseBody ResponseEntity searchBookRequest(@RequestParam(value="query",required = false,defaultValue="") String name, @RequestParam(value="max_result",required = false, defaultValue="all") String max_result,
                                                           @RequestParam(value="isbn",required = false,defaultValue = "")String isbn,
                                                           @RequestParam(value="orderBy",required = false,defaultValue = "relevance") String orderBy ) {
        String ret;
        LinkedList<BookInfo> lis;
        if (name.equals("")&&isbn.equals("")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson(new BadStatus(
                "Name and isbn can not be both empty")));
        if (!orderBy.equals("relevance")&&!orderBy.equals("newest")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson(new BadStatus(
                "Invalid orderBy value")));
        try {
            lis= APIOperations.bookGetInfo(name,isbn,max_result,orderBy);
        } catch (UnirestException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Gson().toJson(new BadStatus("Internal Error")));
        }

        catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Gson().toJson(new BadStatus("Internal Error")));
        }

        catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson(new BadStatus("Illegal value for max_result")));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Gson().toJson(lis));

    }
    //Endpoint /game/search with parameters query (required), max_result(option,default=all), orderBy(optional,default: default IGDB order)
    @RequestMapping(value="/game/search", method = RequestMethod.GET, produces = "application/json")
    public  @ResponseBody ResponseEntity searchGameRequest(@RequestParam(value="query") String name, @RequestParam(value="max_result",required = false, defaultValue="all") String max_result,
                                                   @RequestParam(value="orderBy",required = false, defaultValue="") String orderBy) {
        LinkedList<GameInfo> lis;


        try {
            lis= APIOperations.gameGetInfo(name,max_result,orderBy);
        } catch (UnirestException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Gson().toJson(new BadStatus("Internal Error")));
        }
        catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson(new BadStatus("Bad Request")));
        }

        catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson(new BadStatus("Illegal value for max_result")));
        }


        return ResponseEntity.status(HttpStatus.OK).body(new Gson().toJson(lis));

    }



}
