package framework;

import mediacontent.BookInfo;
import mediacontent.FilmInfo;
import mediacontent.MusicInfo;

import java.util.List;

public class MediaOperations {

    public static void findMediaInfo(List<String> lis, List<BookInfo> books, List<FilmInfo> films, List<MusicInfo> songs) throws Exception {
        for (String name : lis ){
            if(name.contains(".avi")||name.contains(".mp4")||name.contains(".mkv")||name.contains(".mov")){
                List<FilmInfo> info=ApiOperations.filmGetInfo(name,"1");
                if(info.size()>=1){
                    films.add(info.get(0));
                }
            }
            else if (name.contains(".epub")||name.contains(".mobi")||name.contains(".pdf")){
                List<BookInfo> info=ApiOperations.bookGetInfo(name,"","1");
                if(info.size()>=1){
                    books.add(info.get(0));
                }
            }
            else if (name.contains(".mp3")||name.contains(".aac")||name.contains(".flac")){
                List<MusicInfo> info=ApiOperations.musicGetInfo(name,"1");
                if(info.size()>=1){
                    songs.add(info.get(0));
                }
            }

        }


    }
}
