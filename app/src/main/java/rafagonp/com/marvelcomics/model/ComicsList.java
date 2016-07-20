package rafagonp.com.marvelcomics.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafa on 15/07/2016.
 */
public class ComicsList {

    public static final String CONSTANT_STATUS = "status";
    public static final String CONSTANT_MESSAGE = "code";
    public static final String CONSTANT_DATA = "data";
    public static final String CONSTANT_RESULTS = "results";

    private List<Comic> comics;
    private String responseCode;
    private String message;

    public List<Comic> getComics() {
        return comics;
    }

    public void setComics(List<Comic> comics) {
        this.comics = comics;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ComicsList create (JSONObject value) throws JSONException {
        ComicsList returnValue = new ComicsList();

        if (value.has(CONSTANT_STATUS) && !value.get(CONSTANT_STATUS).toString().equals("null")) {
            returnValue.setResponseCode(value.get(CONSTANT_STATUS).toString());
        }

        if (value.has(CONSTANT_MESSAGE) && !value.get(CONSTANT_MESSAGE).toString().equals("null")) {
            returnValue.setMessage(value.get(CONSTANT_MESSAGE).toString());
        }

        ArrayList<Comic> comicsList = new ArrayList<>();
        if (value.has(CONSTANT_DATA) && !value.get(CONSTANT_DATA).toString().equals("null")) {
            if (value.get(CONSTANT_DATA) instanceof JSONObject) {
                JSONObject dataObject = (JSONObject) value.get(CONSTANT_DATA);
                if(dataObject.has(CONSTANT_RESULTS)&& !dataObject.get(CONSTANT_RESULTS).toString().equals("null")) {
                    JSONArray comicsArray = (JSONArray)dataObject.get(CONSTANT_RESULTS);
                    for (int i = 0; i != comicsArray.length(); i++) {
                        JSONObject obj = (JSONObject) comicsArray.get(i);
                        //We add it to our list in android
                        Comic Comic = new Comic();
                        comicsList.add(Comic.create(obj));
                    }
                }else {
                    Comic Comic = new Comic();
                    comicsList.add(Comic.create((JSONObject) value.get(CONSTANT_RESULTS)));
                }
            }
        }
        returnValue.setComics(comicsList);

        return returnValue;
    }

}
