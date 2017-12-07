package framework;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vdurmont.semver4j.Semver;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.core.UriBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateNotifier implements Runnable {
    private final String username;
    private final String repoName;
    private final String version;
    public UpdateNotifier(String u, String r, String v){
        username=u;
        repoName=r;
        version=v;
    }

    @Override
    public void run() {
        Logger logger = Logger.getLogger("mis_update_notifier");
        String output= getJSONData();
        if (output.startsWith("[")){
            JSONArray jarray= new JSONArray(output);
            if (jarray.length()>0){
                JSONObject last_release=jarray.getJSONObject(0);
                if (last_release.has("tag_name")){
                    Semver sem = new Semver(last_release.getString("tag_name"));
                    //System.out.println(last_release.getString("tag_name"));
                    if (sem.isGreaterThan(version)) {
                        logger.log(Level.SEVERE,"An update is avaiable. Please get the update at http://www.github.com/"+username+"/"+repoName+"/releases");
                    }
                    else logger.log(Level.INFO,"MIS is updated");
                }
            }
            else logger.log(Level.WARNING,"Unable to find updates on Github");
        }
        else logger.log(Level.WARNING,"Unable to find updates on Github");

    }


    private String getJSONData(){
        Client client = Client.create();
        WebResource webResource = client.resource(UriBuilder.fromUri("https://api.github.com/repos/"+username+"/"+repoName+"/releases").build());
        ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
        String output = response.getEntity(String.class);
        //System.out.println(output);
        return output;
    }
}
