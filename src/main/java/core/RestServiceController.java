package core;

import com.google.gson.Gson;
import framework.ApiOperations;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestServiceController {

    @RequestMapping(value="/search", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String searchRequest(@RequestParam(value="type") String type, @RequestParam(value="query") String name) {
        String ret;

        if(type.equals("book")) {
            System.out.println("QUERY: "+name);
            ret=new Gson().toJson(ApiOperations.bookGetInfo(name,"0","all"));
        }
        else if (type.equals("film"))  ret= new Gson().toJson(ApiOperations.filmGetInfo(name,"all"));
        else if (type.equals("music")) ret=new Gson().toJson(ApiOperations.musicGetInfo(name,"all"));
        else return null;
        return ret;
    }
}
