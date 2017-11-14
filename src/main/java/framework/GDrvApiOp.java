package framework;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class GDrvApiOp {
    //Method used to get all files from a Google Drive folder (subdirectories included)
    public static List<String> retrieveAllFiles(String auth, String folder) throws IOException, UnirestException {
        List<String> lis= new LinkedList<String>();
        HttpResponse<JsonNode> jsonResponse = Unirest.get("https://www.googleapis.com/drive/v2/files/root/children?q=title='"+folder+"'").header("Authorization","Bearer "+auth).asJson();
        JSONObject jsonObject= new JSONObject(jsonResponse.getBody());
        JSONArray array = jsonObject.getJSONArray("array");
        for(int i=0;i<array.length();i++){
            JSONArray jarray=array.getJSONObject(i).getJSONArray("items");
            int j=jarray.length();
            while(j>0){
                String id=jarray.getJSONObject(0).getString("id");
                auxRetrieveAllFiles(lis,auth,"https://www.googleapis.com/drive/v2/files?includeTeamDriveItems=false&pageSize=500&q='"+id+"'%20in%20parents"+"&key="+ MISConfig.getGoogle_api(),id);
                j--;
            }

        }
        return lis;
    }

    private static void auxRetrieveAllFiles(List<String> lis,String auth, String link, String parents) throws IOException, UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.get(link).header("Authorization","Bearer "+auth).asJson();
        JSONObject jsonObject = new JSONObject(jsonResponse.getBody());
        JSONArray array = jsonObject.getJSONArray("array");
        for (int j=0;j<array.length();j++){
            JSONArray jarray=array.getJSONObject(j).getJSONArray("items");
            for(int i = 0; i < jarray.length(); i++)
            {
                if(jarray.getJSONObject(i).has("mimeType") && !jarray.getJSONObject(i).get("mimeType").equals("application/vnd.google-apps.folder")){
                    String name= jarray.getJSONObject(i).getString("title");
                    lis.add(jarray.getJSONObject(i).getString("title"));
                }
                else {
                    if(jarray.getJSONObject(i).has("id")){
                        auxRetrieveAllFiles(lis,auth,"https://www.googleapis.com/drive/v2/files?includeTeamDriveItems=false&pageSize=500&q='"+jarray.getJSONObject(i).get("id")+"'%20in%20parents"+"&key="+ MISConfig.getGoogle_api(),parents);
                    }
                }
            }
            if(array.getJSONObject(j).has("nextLink")){
                String next=array.getJSONObject(j).getString("nextLink");
                auxRetrieveAllFiles(lis,auth,next,parents);
            }
        }


    }


    }




