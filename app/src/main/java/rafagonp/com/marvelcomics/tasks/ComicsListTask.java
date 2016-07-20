package rafagonp.com.marvelcomics.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rafagonp.com.marvelcomics.Constants;
import rafagonp.com.marvelcomics.helpers.AsyncResponse;
import rafagonp.com.marvelcomics.helpers.MD5Digest;
import rafagonp.com.marvelcomics.logic.BaseLogic;
import rafagonp.com.marvelcomics.model.Comic;
import rafagonp.com.marvelcomics.model.ComicsList;

/**
 * Created by Rafa on 15/07/2016.
 */
public class ComicsListTask  extends AsyncTask<HashMap<String, String>, Object, Object> {

    private AsyncResponse asyncResponse = null;
    BaseLogic baseLogic = new BaseLogic();
    String response;
    ComicsList comicsList;

    public ComicsListTask(AsyncResponse asyncResponse){
        this.asyncResponse = asyncResponse;
    }

    @Override
    public final Object doInBackground(HashMap<String, String>... params) {
        String url = Constants.BASE_URL + "/v1/public/characters/1009368/comics";

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        String md5 = MD5Digest.md5(ts + Constants.PRIVATE_KEY + Constants.PUBLIC_KEY);
        int offset = 0;
        if (params[0].containsKey("Offset")) {
            offset = Integer.parseInt(params[0].get("Offset"));
        }

        url = url + "?ts=" + ts + "&apikey=" + Constants.PUBLIC_KEY + "&hash=" + md5 + "&offset=" + offset;

        if (System.getProperty("DEBUG") == null || "true".equalsIgnoreCase(System.getProperty("DEBUG"))) {
            System.out.println(ComicsListTask.class.getName() + " SENT:" + url);
        }

        try {
            response = baseLogic.downloadUrl(url, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        comicsList = new ComicsList();
        
        try {
            Log.d("RESPONSE", response);
            JSONObject jsonObject = new JSONObject(response);
            comicsList = comicsList.create(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return comicsList;
    }

    @Override
    protected void onPostExecute(Object o) {
        if(comicsList != null && comicsList.getComics() != null)
            Log.d("comicsList", "Number: " + comicsList.getComics().size());
        asyncResponse.processFinish(o);
    }
}
