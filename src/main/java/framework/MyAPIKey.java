package framework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MyAPIKey {
    private static String discogs_api;
    private static String themoviedb_api;
    private static String igdb_api;
    private static String drive_id;
    private static String drive_secret;
    private static String dropbox_secret;
    private static String dropbox_id;
    private static String google_api;
    private static String googlebook_api;
    public MyAPIKey(String file){
        Properties prop = new Properties();
        FileInputStream input = null;
        try {
            //Get keys from property file
            input = new FileInputStream("redacted_api.cfg");
            prop.load(input);
            googlebook_api=prop.getProperty("gbook");
            themoviedb_api=prop.getProperty("moviedb");
            discogs_api=prop.getProperty("discogs");
            igdb_api=prop.getProperty("igdb");
            drive_id=prop.getProperty("drive_id");
            drive_secret=prop.getProperty("drive_secret");
            dropbox_id=prop.getProperty("dropbox_id");
            dropbox_secret=prop.getProperty("dropbox_secret");
            google_api=prop.getProperty("google_api");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static String getDropbox_secret() {
        return dropbox_secret;
    }

    public static String getDropbox_id() {
        return dropbox_id;
    }

    public static String getGooglebook_api() {
        return googlebook_api;
    }

    public static String getIgdb_api() {
        return igdb_api;
    }

    public static String getDrive_id() {
        return drive_id;
    }

    public static String getDrive_secret() {
        return drive_secret;
    }

    public static String getGoogle_api() {
        return google_api;
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
