package framework;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public class OneDrvApiOp {
    public static List<String> dropboxGetFiles(String code) throws UnirestException {
        String api="https://graph.microsoft.com/1.0";

        HttpResponse<JsonNode> jsonResponse = Unirest.get(api+"/me/drive/root/children").header("Authorization","bearer "+code).asJson();

        System.out.println(jsonResponse.getBody().toString());
        return null;



    }
}

