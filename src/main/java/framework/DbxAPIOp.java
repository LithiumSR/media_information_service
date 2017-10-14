package framework;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import mediacontent.Greeting;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;

public class DbxAPIOp {

    public static List<String> dropboxGetFiles(String code) {

        DbxRequestConfig config = new DbxRequestConfig("Media Information Service Configuration");
        DbxClientV2 client = new DbxClientV2(config, code);
        ListFolderResult result = null;
        List<String> elements = new LinkedList<String>();


        try {
            result = client.files().listFolderBuilder("/media").withRecursive(true).start();
            while (true) {
                for (Metadata metadata : result.getEntries()) {
                    if (metadata instanceof FileMetadata) {
                        System.out.println(metadata.toString());
                        elements.add(metadata.getName());
                    }
                }

                if (!result.getHasMore()) {
                    break;
                }

                result = client.files().listFolderContinue(result.getCursor());
            }

            System.out.println(elements.toString());
        } catch (DbxException e) {
            e.printStackTrace();
        }


        return elements;


    }
}
