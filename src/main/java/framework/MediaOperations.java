package framework;

import com.mashape.unirest.http.exceptions.UnirestException;
import core.RabbitSend;
import mediacontent.*;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MediaOperations {

    //Use the APIOperations to find info about some files retrieved from Dropbox and GDrive
    public static void findMediaInfo(List<String> lis, List<BookInfo> books, List<FilmInfo> films, List<MusicInfo> songs) throws Exception {
        for (String file : lis ){
            String name=file.replace("-"," ").replace("_"," ");
            //System.out.println(name);
            if(name.endsWith(".avi")||name.endsWith(".mp4")||name.endsWith(".mkv")||name.endsWith(".mov")){
                List<FilmInfo> info= APIOperations.filmGetInfo(trimFileExtension(name),"1","","","");
                if(info.size()>=1){
                    films.add(info.get(0));
                }
            }
            else if (name.endsWith(".epub")||name.endsWith(".mobi")||name.endsWith(".pdf")){
                List<BookInfo> info= APIOperations.bookGetInfo(trimFileExtension(name),"","1","relevance");
                if(info.size()>=1){
                    books.add(info.get(0));
                }
            }
            else if (name.endsWith(".mp3")||name.endsWith(".aac")||name.endsWith(".flac") || name.endsWith(".m4a") ){
                List<MusicInfo> info= APIOperations.musicGetInfoItunes(trimFileExtension(name),"1","relevance","","");
                if(info.size()>=1){
                    songs.add(info.get(0));
                }
            }

        }


    }

    public static void parseWEB(GameInfo b, JSONObject gameInfo) {
        if(gameInfo.has("websites")){
            JSONArray jarray=gameInfo.getJSONArray("websites");
            for (int i=0;i<jarray.length();i++){
                JSONObject obj=jarray.getJSONObject(i);
                if(obj.has("category")&&obj.getInt("category")==1){
                    b.setWebSite(obj.getString("url"));

                }
                if (obj.has("category")&&obj.getInt("category")==2){
                    b.setWiki(obj.getString("url"));
                }
            }

        }

    }

    //Find the correct PEGI value
    public static void parsePEGI(GameInfo b, JSONObject gameInfo) {
        if (gameInfo.has("pegi")) {
            JSONObject pegi = gameInfo.getJSONObject("pegi");
            if (pegi.has("rating")) {
                int numb = pegi.getInt("rating");
                switch (numb) {
                    case 1:
                        if (pegi.has("synopsis")) b.setPegi(pegi.getString("synopsis"));
                        b.setAgeRequired("3+");
                        break;
                    case 2:
                        if (pegi.has("synopsis")) b.setPegi(pegi.getString("synopsis"));
                        b.setAgeRequired("7+");
                        break;
                    case 3:
                        if (pegi.has("synopsis")) b.setPegi(pegi.getString("synopsis"));
                        b.setAgeRequired("12+");
                        break;
                    case 4:
                        if (pegi.has("synopsis")) b.setPegi(pegi.getString("synopsis"));
                        b.setAgeRequired("16+");
                        break;
                    case 5:
                        if (pegi.has("synopsis")) b.setPegi(pegi.getString("synopsis"));
                        if (pegi.has("synopsis")) b.setPegi(pegi.getString("synopsis"));
                        b.setAgeRequired("18+");
                        break;
                }
            }
        }
    }
    //Get platforms' name
    public static void parsePlatforms(GameInfo b, JSONObject gameInfo) {
        StringBuilder str= new StringBuilder();
        if (gameInfo.has("platforms")){
            JSONArray jarray=gameInfo.getJSONArray("platforms");
            int i=0;
            for (; i<jarray.length();i++){
                JSONObject el=jarray.getJSONObject(i);
                if (el.has("name")){
                    str.append(el.getString("name"));
                    if (jarray.length()>1 && i!=jarray.length()-1)str.append(", ");
                }
            }
        }
        String result=str.toString();
        if (!result.equals("")) b.setPlatforms(result);
    }

    //Generate a list
    public static String getFilesName(List<String> lis ){
        StringBuilder sb= new StringBuilder();
        int iterations=0;
        for(String s: lis){
            if(iterations>0) sb.append(", ");
            sb.append(s);
            iterations++;
        }
        sb.append(".");
        return sb.toString();

    }


    public static LinkedList<MediaRequest> parseMessage(String message){
        ArrayList<Integer> array=new ArrayList<Integer>();
        LinkedList<String> lis=new LinkedList<String>();
        LinkedList<MediaRequest> mr= new LinkedList<MediaRequest>();

        String str=message;
        int index = message.indexOf("~");

        while (index >= 0) {
            array.add(index);
            index = message.indexOf("~", index + 1);
        }

        int it=0;
        if(array.size()>=2){
            int len=array.size();
            if (len%2!=0) len--;
            while(it<len){
                int start=array.get(it)+1;
                int end=array.get(it+1);
                String adding=message.substring(start,end);
                if (!adding.equals(""))lis.add(message.substring(start,end));
                it+=2;
            }
        }

        for(String el:lis){
            int index_space=el.indexOf(" ");
            if (index_space!=-1){
                String type=el.substring(0,index_space);
                String title= el.substring(index_space+1);
                mr.add(new MediaRequest(type.substring(type.indexOf(":")+1),title));
                }
            }


        return mr;

    }

    public static String generateResponse(LinkedList<MediaRequest> lis, String from) throws UnirestException{
        //System.out.println(lis);
        StringBuilder response= new StringBuilder(" \n");
        for(MediaRequest mr: lis){
            List<String> typesWithDuplicates = Stream.of(mr.getType().toLowerCase().split("&"))
                    .collect(Collectors.toList());
            List<String> types = new ArrayList<>(new HashSet<>(typesWithDuplicates));
            for(String type:types) {
                switch (type) {
                    case "book":
                        LinkedList<BookInfo> book = APIOperations.bookGetInfo(mr.getTitle(), "", "1", "");
                        //System.out.println(book);
                        if (book.size() != 0) {
                            response.append("BOOK: \n");
                            response.append("Title: ").append(book.get(0).getTitle()).append(" \n");
                            response.append("Author: ").append(book.get(0).getAuthor()).append(" \n");
                            response.append("Overview: ").append(book.get(0).getOverview()).append(" \n");
                            response.append("---------" + " \n");
                        }
                        RabbitSend.sendChatRequest(from, "Book", mr.getTitle());
                        break;
                    case "game":
                        LinkedList<GameInfo> game = APIOperations.gameGetInfo(mr.getTitle(), "1", "");
                        //System.out.println(game);
                        if (game.size() != 0) {
                            response.append("GAME: \n");
                            response.append("Title: ").append(game.get(0).getTitle()).append(" \n");
                            response.append("Overview: ").append(game.get(0).getOverview()).append(" \n");
                            response.append("Platforms: ").append(game.get(0).getPlatforms()).append(" \n");
                            response.append("Vote: ").append(game.get(0).getVote()).append(" \n");
                            response.append("Release date: ").append(game.get(0).getReleaseDate()).append(" \n");
                            response.append("---------" + " \n");
                        }
                        RabbitSend.sendChatRequest(from, "Game", mr.getTitle());
                        break;
                    case "music":
                        LinkedList<MusicInfo> music = APIOperations.musicGetInfoItunes(mr.getTitle(), "1", "relevance", "", "");
                        //System.out.println(music);
                        if (music.size() != 0) {
                            response.append("MUSIC: \n");
                            response.append("Title: ").append(music.get(0).getTitle()).append(" \n");
                            response.append("Genre: ").append(music.get(0).getGenre()).append(" \n");
                            response.append("Album: ").append(music.get(0).getCollection()).append(" \n");
                            response.append("Release date: ").append(music.get(0).getReleaseDate()).append(" \n");
                            response.append("---------" + " \n");
                        }
                        RabbitSend.sendChatRequest(from, "Music", mr.getTitle());
                        break;
                    case "film":
                        LinkedList<FilmInfo> film = APIOperations.filmGetInfo(mr.getTitle(), "1", "", "", "");
                        //System.out.println(film);
                        if (film.size() != 0) {
                            response.append("FILM: \n");
                            response.append("Title: ").append(film.get(0).getTitle()).append(" \n");
                            response.append("Overview: ").append(film.get(0).getOverview()).append(" \n");
                            response.append("Aggregated rating: ").append(film.get(0).getVote()).append(" \n");
                            response.append("Release date: ").append(film.get(0).getReleaseDate()).append(" \n");
                            response.append("---------" + " \n");
                        }
                        RabbitSend.sendChatRequest(from, "Film", mr.getTitle());
                        break;
                }


            }
        }
        return response.toString();
    }


    private static String trimFileExtension(String s){
        String rev=StringUtils.reverse(s);
        if (s.contains(".")) {
            return StringUtils.reverse(rev.substring(rev.indexOf(".")));
        }
        else return s;
    }

    public static String forceHTTPS(String s){
        String https = StringUtils.replaceOnce(s,"http","https");
        return https;
    }

}
