package framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MISConfig {
    private static String discogs_api;
    private static String themoviedb_api;
    private static String igdb_api;
    private static String drive_id;
    private static String drive_secret;
    private static String dropbox_secret;
    private static String dropbox_id;
    private static String google_api;
    private static String googlebook_api;
    private static String AMQP_URI;
    private static String drive_redirect;
    private static String dropbox_redirect;
    private static String MongoDB_URI;

    public MISConfig(String file) {
        Properties prop = new Properties();
        FileInputStream input = null;
        File fl = new File("MIS_config.cfg");
        //Get keys from property file
        if (fl.exists()) {
            try {
                input = new FileInputStream(fl);
                prop.load(input);
                googlebook_api = prop.getProperty("gbook");
                themoviedb_api = prop.getProperty("moviedb");
                discogs_api = prop.getProperty("discogs");
                igdb_api = prop.getProperty("igdb");
                drive_id = prop.getProperty("drive_id");
                drive_secret = prop.getProperty("drive_secret");
                dropbox_id = prop.getProperty("dropbox_id");
                dropbox_secret = prop.getProperty("dropbox_secret");
                google_api = prop.getProperty("google_api");
                AMQP_URI=prop.getProperty("AMQP_URI");
                drive_redirect=prop.getProperty("drive_redirect");
                dropbox_redirect=prop.getProperty("dropbox_redirect");
                MongoDB_URI=prop.getProperty("MongoDB_URI");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            googlebook_api = System.getenv("gbook");
            themoviedb_api = System.getenv("moviedb");
            discogs_api = System.getenv("discogs");
            igdb_api = System.getenv("igdb");
            drive_id = System.getenv("drive_id");
            drive_secret = System.getenv("drive_secret");
            dropbox_id = System.getenv("dropbox_id");
            dropbox_secret = System.getenv("dropbox_secret");
            google_api = System.getenv("google_api");
            AMQP_URI=System.getenv("AMQP_URI");
            drive_redirect=System.getenv("drive_redirect");
            dropbox_redirect=System.getenv("dropbox_redirect");
            MongoDB_URI=System.getenv("MongoDB_URI");
        }
    }


    public static String getDropbox_secret() {
        return dropbox_secret;
    }

    public static String getDropbox_id() {
        return dropbox_id;
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

    public static String getAMQP() { return AMQP_URI; }

    public static String getMongoDB() { return MongoDB_URI; }

    public static String getDrive_redirect() { return drive_redirect; }

    public static String getDropbox_redirect() { return dropbox_redirect; }
}
