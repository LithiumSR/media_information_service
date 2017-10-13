package framework;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DrvApiOp {


    public static List<String> retrieveAllFiles(String auth) throws IOException, UnirestException {
        List<String> lis= new LinkedList<String>();
        System.out.println("AUTH TOKEN" +auth);
        auxRetrieveAllFiles(lis,auth,"https://www.googleapis.com/drive/v2/files?includeTeamDriveItems=false&key="+MyAPIKey.getGoogle_api());
        return lis;
    }

    private static void auxRetrieveAllFiles(List<String> lis,String auth, String link) throws IOException, UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.get(link+"&access_token="+auth).asJson();
        JSONObject jsonObject = new JSONObject(jsonResponse.getBody());
        JSONArray array = jsonObject.getJSONArray("array");
        System.out.println(jsonObject.toString());
        for (int j=0;j<array.length();j++){
            JSONArray jarray=array.getJSONObject(j).getJSONArray("items");
            for(int i = 0; i < jarray.length(); i++)
            {
                String name= jarray.getJSONObject(i).getString("title");
                lis.add(jarray.getJSONObject(i).getString("title"));

            }
            if(array.getJSONObject(j).has("nextLink")){
                String next=array.getJSONObject(j).getString("nextLink");
                auxRetrieveAllFiles(lis,auth,next);
            }
        }


    }


    }




