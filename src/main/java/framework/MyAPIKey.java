package framework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyAPIKey {
    private static String googlebook_api;
    private static String discogs_api;
    private static String themoviedb_api;
    private static String igdb_api;

    public MyAPIKey(String file){
        Properties prop = new Properties();
        FileInputStream input = null;
        try {
            input = new FileInputStream("redacted_api.cfg");
            prop.load(input);
            googlebook_api=prop.getProperty("gbook");
            themoviedb_api=prop.getProperty("moviedb");
            discogs_api=prop.getProperty("discogs");
            igdb_api=prop.getProperty("igdb");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static String getGooglebook_api() {
        return googlebook_api;
    }

    public static String getDiscogs_api() {
        return discogs_api;
    }

    public static String getThemoviedb_api() {
        return themoviedb_api;
    }

    public static String getIGDB() {
        return igdb_api;
    }
}
