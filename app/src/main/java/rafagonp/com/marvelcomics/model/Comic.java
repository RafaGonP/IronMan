package rafagonp.com.marvelcomics.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rafa on 15/07/2016.
 */
public class Comic {

    public static final String CONSTANT_ID = "id";
    public static final String CONSTANT_TITLE = "title";
    public static final String CONSTANT_ISSUENUMBER = "issueNumber";
    public static final String CONSTANT_DESCRIPTION = "description";
    public static final String CONSTANT_THUMBNAIL = "thumbnail";
    public static final String CONSTANT_PATH = "path";
    public static final String CONSTANT_EXT = "extension";
    public static final String CONSTANT_IMAGES = "images";

    private int id;
    private String title;
    private String issueNumber;
    private String description;
    private String thumbnailPath;
    private String thumbnailExt;
    private String imagePath;
    private String imageExt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getThumbnailExt() {
        return thumbnailExt;
    }

    public void setThumbnailExt(String thumbnailExt) {
        this.thumbnailExt = thumbnailExt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageExt() {
        return imageExt;
    }

    public void setImageExt(String imageExt) {
        this.imageExt = imageExt;
    }

    public Comic create (JSONObject value) throws JSONException {
        Comic returnValue = new Comic();

        if (value.has(CONSTANT_ID) && !value.get(CONSTANT_ID).toString().equals("null")) {
            returnValue.setId((int)value.get(CONSTANT_ID));
        }

        if (value.has(CONSTANT_TITLE) && !value.get(CONSTANT_TITLE).toString().equals("null")) {
            returnValue.setTitle(value.get(CONSTANT_TITLE).toString());
        }

        if (value.has(CONSTANT_ISSUENUMBER) && !value.get(CONSTANT_ISSUENUMBER).toString().equals("null")) {
            returnValue.setIssueNumber(value.get(CONSTANT_ISSUENUMBER).toString());
        }

        if (value.has(CONSTANT_DESCRIPTION) && !value.get(CONSTANT_DESCRIPTION).toString().equals("null")) {
            returnValue.setDescription(value.get(CONSTANT_DESCRIPTION).toString());
        }

        if (value.has(CONSTANT_THUMBNAIL) && !value.get(CONSTANT_THUMBNAIL).toString().equals("null")) {
            JSONObject jsonObject = (JSONObject) value.get(CONSTANT_THUMBNAIL);
            if(jsonObject.has(CONSTANT_PATH)&& !jsonObject.get(CONSTANT_PATH).toString().equals("null")) {
                returnValue.setThumbnailPath(jsonObject.get(CONSTANT_PATH).toString());
            }
            if(jsonObject.has(CONSTANT_EXT)&& !jsonObject.get(CONSTANT_EXT).toString().equals("null")) {
                returnValue.setThumbnailExt(jsonObject.get(CONSTANT_EXT).toString());
            }
        }

        if (value.has(CONSTANT_IMAGES) && !value.get(CONSTANT_IMAGES).toString().equals("null")) {
            JSONArray jsonArray = (JSONArray) value.get(CONSTANT_IMAGES);
            if(jsonArray.opt(0) != null) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                if (jsonObject.has(CONSTANT_PATH) && !jsonObject.get(CONSTANT_PATH).toString().equals("null")) {
                    returnValue.setImagePath(jsonObject.get(CONSTANT_PATH).toString());
                }
                if (jsonObject.has(CONSTANT_EXT) && !jsonObject.get(CONSTANT_EXT).toString().equals("null")) {
                    returnValue.setImageExt(jsonObject.get(CONSTANT_EXT).toString());
                }
            }
        }

        return returnValue;
    }
}
