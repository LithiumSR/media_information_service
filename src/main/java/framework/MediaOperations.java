package framework;

import mediacontent.BookInfo;
import mediacontent.FilmInfo;
import mediacontent.GameInfo;
import mediacontent.MusicInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.print.Book;
import java.util.List;

public class MediaOperations {

    public static void findMediaInfo(List<String> lis, List<BookInfo> books, List<FilmInfo> films, List<MusicInfo> songs) throws Exception {
        for (String name : lis ){
            if(name.contains(".avi")||name.contains(".mp4")||name.contains(".mkv")||name.contains(".mov")){
                List<FilmInfo> info=ApiOperations.filmGetInfo(trimFileExtension(name),"1","");
                if(info.size()>=1){
                    films.add(info.get(0));
                }
            }
            else if (name.contains(".epub")||name.contains(".mobi")||name.contains(".pdf")){
                List<BookInfo> info=ApiOperations.bookGetInfo(trimFileExtension(name),"","1","relevance");
                if(info.size()>=1){
                    books.add(info.get(0));
                }
            }
            else if (name.contains(".mp3")||name.contains(".aac")||name.contains(".flac")){
                List<MusicInfo> info=ApiOperations.musicGetInfo(trimFileExtension(name),"1","FILE,MP3,Single");
                if(info.size()>=1){
                    songs.add(info.get(0));
                }
            }

        }


    }

    public static String generateHTMLBook(List<BookInfo> lis){
        StringBuilder sb=new StringBuilder();

        for(BookInfo b: lis){
            sb.append("<strong>Title:</strong> "+b.getTitle()+"<br>");
            sb.append("<strong>Author:</strong> "+b.getAuthor()+"<br>");
            sb.append("<strong>Publisher:</strong> "+b.getPublisher()+"<br>");
            sb.append("<strong>Overview:</strong> "+ b.getOverview()+"<br>");
            sb.append("<strong>Release Date:</strong> "+b.getReleaseDate()+"<br>");
            sb.append(("<strong>Google Book ID:</strong> "+b.getISBN()+"<br>"));
            sb.append("<br>");
        }

    return sb.toString();
    }


    public static String generateHTMLFilm(List<FilmInfo> lis) {
        StringBuilder sb = new StringBuilder();
        for (FilmInfo b : lis) {
            sb.append("<strong>Title:</strong> " + b.getTitle() + "<br>");
            sb.append("<strong>Overview:</strong> " + b.getOverview() + "<br>");
            sb.append("<strong>Vote average:</strong> " + b.getVote() + "<br>");
            sb.append("<strong>Year of release:</strong> " + b.getReleaseDate() + "<br>");
            sb.append("<br>");
        }
        return sb.toString();
    }


    public static String generateHTMLMusic(List<MusicInfo> lis) {
        StringBuilder sb = new StringBuilder();
        for (MusicInfo b : lis) {
            sb.append("<strong>Title:</strong> " + b.getTitle() + "<br>");
            sb.append("<strong>Labels:</strong> " + b.getLabels() + "<br>");
            sb.append("<strong>Release Date:</strong> " + b.getReleaseDate() + "<br>");
            sb.append("<br>");

        }
        return sb.toString();
    }

    public static void parseWEB(GameInfo b, JSONObject gameInfo) {
        if(gameInfo.has("websites")){
            JSONArray jarray=gameInfo.getJSONArray("websites");
            for (int i=0;i<jarray.length();i++){
                JSONObject obj=jarray.getJSONObject(i);
                if(obj.has("category")&&obj.getInt("category")==1){
                    b.setWebSite(obj.getString("url"));

                }
            }

        }

    }
    public static void parsePEGI(GameInfo b, JSONObject gameInfo) {
        if (gameInfo.has("pegi")) {
            JSONObject pegi = gameInfo.getJSONObject("pegi");
            if (pegi.has("rating")) {
                int numb = pegi.getInt("rating");
                if (numb == 1) {
                    if(pegi.has("synopsis")) b.setPegi(pegi.getString("synopsis"));
                    b.setAgeRequired("3+");

                } else if (numb == 2) {

                    if(pegi.has("synopsis")) b.setPegi(pegi.getString("synopsis"));
                    b.setAgeRequired("7+");

                } else if (numb == 3) {
                    if(pegi.has("synopsis")) b.setPegi(pegi.getString("synopsis"));
                    b.setAgeRequired("12+");

                } else if (numb == 4) {
                    if(pegi.has("synopsis")) b.setPegi(pegi.getString("synopsis"));
                    b.setAgeRequired("16+");
                } else if (numb == 5) {
                    if(pegi.has("synopsis")) b.setPegi(pegi.getString("synopsis"));
                    if(pegi.has("synopsis")) b.setPegi(pegi.getString("synopsis"));
                    b.setAgeRequired("18+");

                }
            }
        }
    }




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



    private static String trimFileExtension(String s){

        String separator = System.getProperty("file.separator");
        String filename;

        // Remove the path upto the filename.
        int lastSeparatorIndex = s.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = s;
        } else {
            filename = s.substring(lastSeparatorIndex + 1);
        }

        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1)
            return filename;

        return filename.substring(0, extensionIndex);
    }

    public static void parseStatus(GameInfo b, JSONObject gameInfo) {


    }
}
