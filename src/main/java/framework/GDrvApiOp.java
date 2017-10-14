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

public class GDrvApiOp {


    public static List<String> retrieveAllFiles(String auth, String folder) throws IOException, UnirestException {
        List<String> lis= new LinkedList<String>();
        System.out.println("Ho ricevuto: "+folder);
        HttpResponse<JsonNode> jsonResponse = Unirest.get("https://www.googleapis.com/drive/v2/files/root/children?q=title='"+folder+"'").header("Authorization","Bearer "+auth).asJson();
        JSONObject jsonObject= new JSONObject(jsonResponse.getBody());
        System.out.println(jsonResponse.getBody().toString());
        JSONArray array = jsonObject.getJSONArray("array");
        for(int i=0;i<array.length();i++){
            JSONArray jarray=array.getJSONObject(i).getJSONArray("items");
            if(jarray.length()>=1){
                System.out.println();
                String id=jarray.getJSONObject(0).getString("id");
                System.out.println(id);
                auxRetrieveAllFiles(lis,auth,"https://www.googleapis.com/drive/v2/files?includeTeamDriveItems=false&pageSize=500&q='"+id+"'%20in%20parents"+"&key="+MyAPIKey.getGoogle_api(),id);
                break;
            }

        }
        return lis;
    }

    private static void auxRetrieveAllFiles(List<String> lis,String auth, String link, String parents) throws IOException, UnirestException {
        System.out.println("ENTRO");
        HttpResponse<JsonNode> jsonResponse = Unirest.get(link).header("Authorization","Bearer "+auth).asJson();
        JSONObject jsonObject = new JSONObject(jsonResponse.getBody());
        System.out.println("STAMPO "+jsonObject.toString());
        JSONArray array = jsonObject.getJSONArray("array");
        for (int j=0;j<array.length();j++){
            JSONArray jarray=array.getJSONObject(j).getJSONArray("items");
            for(int i = 0; i < jarray.length(); i++)
            {
                String name= jarray.getJSONObject(i).getString("title");
                lis.add(jarray.getJSONObject(i).getString("title"));

            }
            if(array.getJSONObject(j).has("nextLink")){
                String next=array.getJSONObject(j).getString("nextLink");
                auxRetrieveAllFiles(lis,auth,next,parents);
            }
        }


    }


    }



