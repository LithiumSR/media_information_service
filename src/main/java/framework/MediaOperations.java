package framework;

import mediacontent.BookInfo;
import mediacontent.FilmInfo;
import mediacontent.MusicInfo;

import java.util.List;

public class MediaOperations {

    public static void findMediaInfo(List<String> lis, List<BookInfo> books, List<FilmInfo> films, List<MusicInfo> songs) throws Exception {
        for (String name : lis ){
            if(name.contains(".avi")||name.contains(".mp4")||name.contains(".mkv")||name.contains(".mov")){
                List<FilmInfo> info=ApiOperations.filmGetInfo(trimFileExtension(name),"1");
                if(info.size()>=1){
                    films.add(info.get(0));
                }
            }
            else if (name.contains(".epub")||name.contains(".mobi")||name.contains(".pdf")){
                List<BookInfo> info=ApiOperations.bookGetInfo(trimFileExtension(name),"","1");
                if(info.size()>=1){
                    books.add(info.get(0));
                }
            }
            else if (name.contains(".mp3")||name.contains(".aac")||name.contains(".flac")){
                List<MusicInfo> info=ApiOperations.musicGetInfo(trimFileExtension(name),"1");
                if(info.size()>=1){
                    songs.add(info.get(0));
                }
            }

        }


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

}
